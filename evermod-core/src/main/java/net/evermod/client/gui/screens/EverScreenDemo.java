package net.evermod.client.gui.screens;

import net.evermod.EverMod;
import net.evermod.client.gui.BorderColor;
import net.evermod.client.gui.layout.Box;
import net.evermod.client.gui.layout.Column;
import net.evermod.client.gui.layout.LayoutAlignment;
import net.evermod.client.gui.layout.Row;
import net.evermod.client.gui.layout.Space;
import net.evermod.client.gui.widget.SolidButton;
import net.evermod.client.gui.widget.Button;
import net.evermod.client.gui.widget.Checkbox;
import net.evermod.client.gui.widget.InputText;
import net.evermod.client.gui.widget.Label;
import net.evermod.client.gui.widget.Slider;
import net.evermod.resources.EverLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Diagnostic Screen used to test EverUI rendering, hierarchy, and components.
 *
 * @author Wipodev
 */
public class EverScreenDemo extends EverScreen {

  private String name = "";
  private double sliderValue = 30.0D;
  private boolean enableNotifications = true;
  ResourceLocation btnBase = EverLocation.parse(EverMod.EVER_ID, "textures/gui/button.png");
  ResourceLocation btnDisabled =
      EverLocation.parse(EverMod.EVER_ID, "textures/gui/button_disabled.png");

  /**
   * Constructs the diagnostic screen.
   */
  public EverScreenDemo() {
    super("EverUI Layout Demonstration");
  }

  @Override
  public void setupUI() {
    // 1. Root BOX occupying 80% of screen size, centered horizontally and vertically
    int boxWidth = (int) (this.width * 0.8F);
    int boxHeight = (int) (this.height * 0.8F);
    int boxX = getCenterX() - (boxWidth / 2);
    int boxY = getCenterY() - (boxHeight / 2);

    Box mainBox = new Box(boxX, boxY, boxWidth, boxHeight)
        .padding(12)
        .align(LayoutAlignment.CENTER, LayoutAlignment.CENTER);
    mainBox.backgroundColor(0xD0121218) // Dark gray container background
        .border(new BorderColor(
            0xFF007ACC, // Top (Blue)
            0xFFFF5555, // Right (Red)
            0xFF55FF55, // Bottom (Green)
            0xFFFFFF55 // Left (Yellow)
        ));

    // 2. Main vertical layout inside Box
    Column rootColumn = new Column()
        .gap(10)
        .fillMaxSize(boxWidth - 24, boxHeight - 24);

    // Box Header Label
    Label boxTitle = new Label(
        Component.literal("=== MAIN BOX CONTAINER ===").withStyle(ChatFormatting.GOLD,
            ChatFormatting.BOLD))
                .shadow(true);

    // 3. ROW Container holding two equal Columns side by side
    int availableRowWidth = boxWidth - 24;
    int availableRowHeight = boxHeight - 70;

    Row contentRow = new Row()
        .gap(12)
        .fillMaxSize(availableRowWidth, availableRowHeight);
    contentRow.backgroundColor(0x60222233); // Subtle blueish background for Row

    // 4. Left Column inside Row
    int halfColumnWidth = (availableRowWidth - 12) / 2;

    Column leftColumn = new Column()
        .gap(6)
        .padding(8)
        .fillMaxSize(halfColumnWidth, availableRowHeight);
    leftColumn.backgroundColor(0x801A3A5C); // Dark blue for Left Column

    leftColumn.addChild(new Label("--- COLUMN 1 (LEFT) ---").color(0xFF55FFFF).shadow(true));
    leftColumn.addChild(Space.height(4)); // Vertical spacing
    leftColumn.addChild(new Label("• Auto-calculated Y pos").color(0xFFFFFFFF));
    leftColumn.addChild(new Label("• Managed by Column flow").color(0xFFAAAAAA));

    InputText nameInput = new InputText()
        .placeholder("Escribe tu nombre...")
        .value(this.name)
        .setOnValueChange(newValue -> this.name = newValue);

    leftColumn.addChild(nameInput);
    leftColumn.addChild(Space.height(4));

    // --- ABSTRACT SLIDER DRAGGABLE INTEGRATION ---
    Label sliderValueLabel = new Label(String.format("Val: %.0f%%", this.sliderValue))
        .color(0xFFFFFF55)
        .shadow(true);

    // Instances concrete Slider inheriting smooth dragging behavior from AbstractSlider
    Slider demoSlider = new Slider(0, 0, 100, 20, 0.0D, 100.0D, this.sliderValue)
        .step(1.0D)
        .onChange(newValue -> {
          this.sliderValue = newValue;
          sliderValueLabel.setText(String.format("Val: %.0f%%", newValue));
        });

    Row sliderContainer = new Row().gap(8);

    sliderContainer.addChild(demoSlider);
    sliderContainer.addChild(sliderValueLabel);
    leftColumn.addChild(sliderContainer);
    leftColumn.addChild(Space.height(4));

    // --- CHECKBOX INTEGRATION ---
    Checkbox demoCheckbox =
        new Checkbox(0, 0, 120, 20, "Habilitar avisos", this.enableNotifications)
            .onChange(checked -> {
              this.enableNotifications = checked;
              System.out.println("Estado de notificaciones: " + checked);
            });

    leftColumn.addChild(demoCheckbox);

    // 5. Right Column inside Row
    Column rightColumn = new Column()
        .gap(6)
        .padding(8)
        .fillMaxSize(halfColumnWidth, availableRowHeight);
    rightColumn.backgroundColor(0x805C1A3A); // Dark red/purple for Right Column

    rightColumn.addChild(new Label("--- COLUMN 2 (RIGHT) ---").color(0xFFFF55FF).shadow(true));
    rightColumn.addChild(Space.height(4)); // Vertical spacing
    rightColumn.addChild(new Label("• Automatic horizontal Row gap").color(0xFFFFFFFF));
    rightColumn.addChild(new Label("• No manual setX / setY used").color(0xFF55FF55));
    rightColumn.addChild(new SolidButton("Boton Color")
        .backgroundColors(0xFF1E88E5, 0xFF1565C0, 0xFF424242)
        .border(1)
        .textColor(0xFFFFFFFF)
        .alignment(LayoutAlignment.CENTER)
        .onClick(btn -> System.out.println("¡Boton plano clickeado!")));
    rightColumn.addChild(new Button(btnBase, "Boton Textura")
        .backgroundImages(btnBase, btnDisabled)
        .border(new BorderColor(
            0xFF007ACC, // Top (Blue)
            0xFFFF5555, // Right (Red)
            0xFF55FF55, // Bottom (Green)
            0xFFFFFF55 // Left (Yellow)
        ))
        .onClick(btn -> System.out.println("¡Boton con textura presionado!")));
    rightColumn.addChild(new Button(btnBase, "Textura Deshabilitada")
        .backgroundImages(btnBase, btnDisabled)
        .border(1)
        .onClick(btn -> System.out.println("¡Boton con textura presionado!"))
        .enabled(false));

    // Assembly tree structure
    contentRow.addChild(leftColumn);
    contentRow.addChild(rightColumn);

    rootColumn.addChild(boxTitle);
    rootColumn.addChild(contentRow);

    mainBox.addChild(rootColumn);

    // Mount root container to screen
    this.add(mainBox);
  }
}
