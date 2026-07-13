package net.evermod.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EverDefaultConfigGui extends Screen {
  private final Screen lastScreen;
  private final Map<ConfigManager.ConfigEntry, Object> pendingChanges = new HashMap<>();
  private ConfigList list;
  private final Map<AbstractWidget, List<FormattedCharSequence>> widgetTooltips = new HashMap<>();

  public EverDefaultConfigGui(Screen parent) {
    super(Component.literal("Configuración del Mod"));
    this.lastScreen = parent;
  }

  @Override
  protected void init() {
    super.init();
    this.pendingChanges.clear();
    this.widgetTooltips.clear();

    this.list = new ConfigList(this.minecraft, this.width, this.height, 32, this.height - 32, 26);
    this.addWidget(this.list);

    this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 26, 200, 20,
        Component.literal("Hecho"), (btn) -> {
          this.pendingChanges.forEach((entry, value) -> {
            try {
              ConfigManager.setAndSync(entry, value);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
          this.minecraft.setScreen(this.lastScreen);
        }));
  }

  @Override
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(poseStack);
    this.list.render(poseStack, mouseX, mouseY, partialTick);
    drawCenteredString(poseStack, this.font, this.title, this.width / 2, 12, 0xFFFFFF);
    super.render(poseStack, mouseX, mouseY, partialTick);
    this.renderCustomTooltips(poseStack, mouseX, mouseY);
  }

  private void renderCustomTooltips(PoseStack poseStack, int mouseX, int mouseY) {
    for (Map.Entry<AbstractWidget, List<FormattedCharSequence>> item : this.widgetTooltips
        .entrySet()) {
      AbstractWidget widget = item.getKey();
      List<FormattedCharSequence> tooltipLines = item.getValue();

      if (widget.visible && widget.isMouseOver(mouseX, mouseY) && !tooltipLines.isEmpty()) {
        this.renderTooltip(poseStack, tooltipLines, mouseX, mouseY + 30);
        break;
      }
    }
  }

  private String formatSmartName(EverProperty<?> property) {
    if (property.hasCustomName()) {
      return property.getName();
    }

    String rawName = property.getName();
    if (rawName == null || rawName.isEmpty()) {
      return "Propiedad Sin Nombre";
    }

    if (rawName.equals(rawName.toUpperCase())) {
      rawName = rawName.toLowerCase();
    }

    String spaceNormalized = rawName.replace('_', ' ').replace('-', ' ');
    String separated = spaceNormalized.replaceAll("(?<=[a-z])(?=[A-Z])", " ")
        .replaceAll("(?<=[A-Za-z])(?=[0-9])", " ").replaceAll("(?<=[0-9])(?=[A-Za-z])", " ");
    String[] words = separated.trim().split("\\s+");
    StringBuilder finalName = new StringBuilder();

    for (String word : words) {
      if (!word.isEmpty()) {
        finalName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1))
            .append(" ");
      }
    }

    return finalName.toString().trim();
  }

  // --- CONTENEDOR DE OPCIONES EN DOBLE COLUMNA ---

  private class ConfigList extends ContainerObjectSelectionList<ConfigList.ConfigEntryRow> {
    public ConfigList(Minecraft minecraft, int width, int height, int top, int bottom,
        int itemHeight) {
      super(minecraft, width, height, top, bottom, itemHeight);

      for (int i = 0; i < ConfigManager.ENTRIES.size(); i += 2) {
        ConfigManager.ConfigEntry leftEntry = ConfigManager.ENTRIES.get(i);
        ConfigManager.ConfigEntry rightEntry =
            (i + 1 < ConfigManager.ENTRIES.size()) ? ConfigManager.ENTRIES.get(i + 1) : null;
        this.addEntry(new ConfigEntryRow(leftEntry, rightEntry));
      }
    }

    @Override
    public int getRowWidth() {
      return 310;
    }

    @Override
    protected int getScrollbarPosition() {
      return this.width / 2 + 160;
    }

    private class ConfigEntryRow extends ContainerObjectSelectionList.Entry<ConfigEntryRow> {
      private final List<AbstractWidget> rowWidgets = new ArrayList<>();

      public ConfigEntryRow(ConfigManager.ConfigEntry left, ConfigManager.ConfigEntry right) {
        if (left != null)
          rowWidgets.add(createWidgetForEntry(left));
        if (right != null)
          rowWidgets.add(createWidgetForEntry(right));
      }

      private AbstractWidget createWidgetForEntry(ConfigManager.ConfigEntry entry) {
        Object defaultValue = entry.property.getDefaultValue();
        String displayName = EverDefaultConfigGui.this.formatSmartName(entry.property);
        AbstractWidget generatedWidget;

        if (defaultValue instanceof Boolean) {
          generatedWidget = CycleButton.onOffBuilder((Boolean) entry.property.get()).create(0, 0,
              150, 20, Component.literal(displayName), (button, value) -> {
                EverDefaultConfigGui.this.pendingChanges.put(entry, value);
              });
        } else if ((defaultValue instanceof Integer || defaultValue instanceof Double)
            && entry.property.getMin() != null && entry.property.getMax() != null) {
          generatedWidget = new PropertySlider(0, 0, 150, 20, entry);
        } else {
          EditBox editBox = new EditBox(EverDefaultConfigGui.this.font, 0, 0, 148, 18,
              Component.literal(displayName));
          editBox.setValue(String.valueOf(entry.property.get()));
          editBox.setResponder(text -> {
            if (text.isEmpty())
              return;
            try {
              if (defaultValue instanceof String) {
                EverDefaultConfigGui.this.pendingChanges.put(entry, text);
              } else if (defaultValue instanceof Integer) {
                EverDefaultConfigGui.this.pendingChanges.put(entry, Integer.parseInt(text));
              } else if (defaultValue instanceof Double) {
                EverDefaultConfigGui.this.pendingChanges.put(entry, Double.parseDouble(text));
              }
            } catch (NumberFormatException ignored) {
            }
          });
          generatedWidget = editBox;
        }

        String comment = entry.property.getComment();
        if (comment != null && !comment.isEmpty()) {
          List<FormattedCharSequence> lines =
              EverDefaultConfigGui.this.font.split(Component.literal(comment), 200);
          EverDefaultConfigGui.this.widgetTooltips.put(generatedWidget, lines);
        }

        return generatedWidget;
      }

      @Override
      public void render(PoseStack poseStack, int index, int top, int left, int rowWidth,
          int rowHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
        if (rowWidgets.size() > 0) {
          AbstractWidget leftWidget = rowWidgets.get(0);
          leftWidget.x = left;
          leftWidget.y = top;
          leftWidget.render(poseStack, mouseX, mouseY, partialTick);
        }

        if (rowWidgets.size() > 1) {
          AbstractWidget rightWidget = rowWidgets.get(1);
          rightWidget.x = left + 160;
          rightWidget.y = top;
          rightWidget.render(poseStack, mouseX, mouseY, partialTick);
        }
      }

      @Override
      public List<? extends net.minecraft.client.gui.components.events.GuiEventListener> children() {
        return this.rowWidgets;
      }

      @Override
      public List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
        return this.rowWidgets;
      }
    }
  }



  // --- DESLIZADOR ADAPTADO ---

  private class PropertySlider extends AbstractSliderButton {
    private final ConfigManager.ConfigEntry entry;
    private final double min;
    private final double max;
    private final int decimalPlaces;

    public PropertySlider(int x, int y, int width, int height, ConfigManager.ConfigEntry entry) {
      super(x, y, width, height, Component.empty(), 0.0);
      this.entry = entry;
      this.min = ((Number) entry.property.getMin()).doubleValue();
      this.max = ((Number) entry.property.getMax()).doubleValue();
      this.decimalPlaces = determineDecimalPlaces(entry.property.getDefaultValue());

      double currentVal = ((Number) entry.property.get()).doubleValue();
      this.value = (Mth.clamp(currentVal, min, max) - min) / (max - min);
      this.updateMessage();
    }

    private int determineDecimalPlaces(Object defaultValue) {
      if (defaultValue instanceof Integer)
        return 0;
      String text = String.valueOf(defaultValue);
      int integerPlaces = text.indexOf('.');
      if (integerPlaces < 0)
        return 2;
      int decimalLength = text.length() - integerPlaces - 1;
      return Math.max(2, decimalLength);
    }

    @Override
    protected void updateMessage() {
      String name = EverDefaultConfigGui.this.formatSmartName(entry.property);
      double currentRealValue = min + (this.value * (max - min));
      EverProperty.DisplayType type = entry.property.getDisplayType();

      switch (type) {
        case PERCENTAGE:
          double percentValue = currentRealValue * 100.0;
          if (percentValue % 1.0 == 0.0) {
            this.setMessage(Component.literal(name + ": " + String.format("%.0f%%", percentValue)));
          } else {
            this.setMessage(Component.literal(name + ": " + String.format("%.1f%%", percentValue)));
          }
          break;

        case SECONDS_FROM_TICKS:
          double secondsValue = currentRealValue / 20.0;
          this.setMessage(Component.literal(name + ": " + String.format("%.0fs", secondsValue)));
          break;

        case DEFAULT:
        default:
          if (entry.property.getDefaultValue() instanceof Integer) {
            this.setMessage(Component.literal(name + ": " + (int) Math.round(currentRealValue)));
          } else {
            String format = "%." + this.decimalPlaces + "f";
            this.setMessage(
                Component.literal(name + ": " + String.format(format, currentRealValue)));
          }
          break;
      }
    }

    @Override
    protected void applyValue() {
      double calculatedValue = min + (this.value * (max - min));
      if (entry.property.getDefaultValue() instanceof Integer) {
        EverDefaultConfigGui.this.pendingChanges.put(entry, (int) Math.round(calculatedValue));
      } else {
        double scale = Math.pow(10, this.decimalPlaces);
        double roundedValue = Math.round(calculatedValue * scale) / scale;
        EverDefaultConfigGui.this.pendingChanges.put(entry, roundedValue);
      }
    }
  }
}
