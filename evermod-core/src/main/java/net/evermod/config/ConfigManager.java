package net.evermod.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.objectweb.asm.Type;
import net.evermod.EverMod;
import net.evermod.context.IEverContext;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;

/**
 * Multi-mod configuration manager handling annotation scanning, spec building,
 * and synchronized field updates.
 *
 * @author Wipodev
 */
public class ConfigManager {

  public static final List<ConfigEntry> ENTRIES = new ArrayList<>();
  private static final Map<String, ForgeConfigSpec> SPECS = new ConcurrentHashMap<>();
  private static final Map<String, Boolean> LOADED_STATES = new ConcurrentHashMap<>();
  protected static Class<?> customScreenClass = null;

  public static class ConfigEntry {
    protected final String modid;
    protected final Field field;
    protected final EverProperty<?> property;
    protected final String category;
    protected ForgeConfigSpec.ConfigValue<?> forgeValue;

    public ConfigEntry(String modid, Field field, EverProperty<?> property, String category) {
      this.modid = modid;
      this.field = field;
      this.property = property;
      this.category = category;
    }

    public String getModid() {
      return modid;
    }

    public Field getField() {
      return field;
    }

    public EverProperty<?> getProperty() {
      return property;
    }

    public String getCategory() {
      return category;
    }

    public ForgeConfigSpec.ConfigValue<?> getForgeValue() {
      return forgeValue;
    }

    public void syncToField() {
      try {
        if (forgeValue != null && forgeValue.get() != null) {
          Object currentForge = forgeValue.get();
          updatePropertyValue(property, currentForge);
        }
      } catch (Exception e) {
        EverMod.LOGGER.error("Error syncing field {}", field.getName(), e);
      }
    }

    @SuppressWarnings("unchecked")
    private <V> void updatePropertyValue(EverProperty<V> prop, Object val) {
      prop.set((V) val);
    }
  }

  public static void init(String modid, IEverContext context) {
    ModFileScanData scanData = ModList.get().getModFileById(modid).getFile().getScanResult();
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    boolean hasConfigAnnotations = false;

    for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
      if (data.annotationType().equals(Type.getType(EverConfigScreen.class))) {
        try {
          customScreenClass = Class.forName(data.memberName());
        } catch (ClassNotFoundException e) {
          EverMod.LOGGER.error("Custom screen class not found: {}", data.memberName(), e);
        }
        continue;
      }

      if (!data.annotationType().equals(Type.getType(EverConfig.class))) {
        continue;
      }

      hasConfigAnnotations = true;

      try {
        Class<?> configClass = Class.forName(data.memberName());
        EverConfig anno = configClass.getAnnotation(EverConfig.class);

        String category =
            anno.category().isEmpty() ? configClass.getSimpleName().toLowerCase() : anno.category();
        builder.push(category);

        for (Field field : configClass.getDeclaredFields()) {
          if (Modifier.isStatic(field.getModifiers()) && field.getType() == EverProperty.class) {
            field.setAccessible(true);
            EverProperty<?> property = (EverProperty<?>) field.get(null);
            String configId = field.getName();
            property.setId(configId);

            Object def = property.getDefaultValue();
            if (!property.getComment().isEmpty()) {
              builder.comment(property.getComment());
            }

            ForgeConfigSpec.ConfigValue<?> forgeVal;
            if (def instanceof Integer i) {
              forgeVal = builder.defineInRange(configId, i, (Integer) property.getMin(),
                  (Integer) property.getMax());
            } else if (def instanceof Double d) {
              forgeVal = builder.defineInRange(configId, d, (Double) property.getMin(),
                  (Double) property.getMax());
            } else if (def instanceof Boolean b) {
              forgeVal = builder.define(configId, b);
            } else {
              forgeVal = builder.define(configId, def.toString());
            }

            ConfigEntry entry = new ConfigEntry(modid, field, property, category);
            entry.forgeValue = forgeVal;
            ENTRIES.add(entry);
          }
        }
        builder.pop();
      } catch (Exception e) {
        throw new RuntimeException("Error processing config classes for mod: " + modid, e);
      }
    }

    if (!hasConfigAnnotations && ENTRIES.stream().noneMatch(e -> e.getModid().equals(modid))) {
      EverMod.LOGGER.info(
          "No configurations found for mod: {}. Skipping screen and config file registration.",
          modid);
      return;
    }

    ForgeConfigSpec modSpec = builder.build();
    SPECS.put(modid, modSpec);
    LOADED_STATES.put(modid, false);

    context.registerConfig(modSpec, "evermod-" + modid + ".toml");

    context.getEventBus().addListener((ModConfigEvent.Loading event) -> {
      if (event.getConfig().getSpec() == modSpec) {
        ENTRIES.stream()
            .filter(e -> e.getModid().equals(modid))
            .forEach(ConfigEntry::syncToField);
        LOADED_STATES.put(modid, true);
      }
    });

    context.getEventBus().addListener((ModConfigEvent.Reloading event) -> {
      if (event.getConfig().getSpec() == modSpec) {
        ENTRIES.stream()
            .filter(e -> e.getModid().equals(modid))
            .forEach(ConfigEntry::syncToField);
      }
    });

    ConfigClientRegistry.registerScreen(context);
  }

  @SuppressWarnings("unchecked")
  public static void setAndSync(ConfigEntry entry, Object newValue) {
    String modid = entry.getModid();
    boolean isLoaded = LOADED_STATES.getOrDefault(modid, false);
    ForgeConfigSpec modSpec = SPECS.get(modid);

    if (!isLoaded || entry.forgeValue == null || modSpec == null) {
      EverMod.LOGGER.info(
          "Save omitted: Forge configuration system for mod '{}' is not fully loaded yet.", modid);
      return;
    }

    try {
      ((ForgeConfigSpec.ConfigValue<Object>) entry.forgeValue).set(newValue);
      entry.syncToField();
      modSpec.save();
    } catch (Exception e) {
      EverMod.LOGGER.error("Error saving configuration for field: {}", entry.field.getName(), e);
    }
  }
}
