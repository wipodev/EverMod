package net.evermod.config;

import net.evermod.EverMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
  public static final List<ConfigEntry> ENTRIES = new ArrayList<>();
  protected static Class<?> customScreenClass = null;
  private static ForgeConfigSpec spec;
  private static boolean isConfigLoaded = false;

  public static class ConfigEntry {
    protected final Field field;
    protected final EverProperty<?> property;
    protected final String category;
    protected ForgeConfigSpec.ConfigValue<?> forgeValue;

    public ConfigEntry(Field field, EverProperty<?> property, String category) {
      this.field = field;
      this.property = property;
      this.category = category;
    }

    public void syncToField() {
      try {
        if (forgeValue != null && forgeValue.get() != null) {
          Object currentForge = forgeValue.get();
          updatePropertyValue(property, currentForge);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @SuppressWarnings("unchecked")
    private <V> void updatePropertyValue(EverProperty<V> prop, Object val) {
      prop.set((V) val);
    }
  }

  @SuppressWarnings("removal")
  public static void init(String modid, IEventBus modEventBus) {
    ModFileScanData scanData = ModList.get().getModFileById(modid).getFile().getScanResult();
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    boolean hasConfigAnnotations = false;

    for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
      if (data.annotationType().equals(Type.getType(EverConfigScreen.class))) {
        try {
          customScreenClass = Class.forName(data.memberName());
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
        continue;
      }

      if (!data.annotationType().equals(Type.getType(EverConfig.class)))
        continue;

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
            if (!property.getComment().isEmpty())
              builder.comment(property.getComment());

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

            ConfigEntry entry = new ConfigEntry(field, property, category);
            entry.forgeValue = forgeVal;
            ENTRIES.add(entry);
          }
        }
        builder.pop();
      } catch (Exception e) {
        throw new RuntimeException("Error procesando clases de configuración", e);
      }
    }

    if (!hasConfigAnnotations && ENTRIES.isEmpty()) {
      EverMod.LOGGER.info("No se encontraron configuraciones para el mod: " + modid + ". Omitiendo registro de pantalla y archivo config.");
      return;
    }

    spec = builder.build();

    // Registrar la especificación en Forge usando el entorno común
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec,
        "evermod-" + modid + ".toml");

    modEventBus.addListener((ModConfigEvent.Loading event) -> {
      if (event.getConfig().getSpec() == spec) {
        ENTRIES.forEach(ConfigEntry::syncToField);
        isConfigLoaded = true;
      }
    });

    modEventBus.addListener((ModConfigEvent.Reloading event) -> {
      if (event.getConfig().getSpec() == spec) {
        ENTRIES.forEach(ConfigEntry::syncToField);
      }
    });

    // Registrar el apartado visual
    ConfigClientRegistry.registerScreen();
  }

  @SuppressWarnings("unchecked")
  public static void setAndSync(ConfigEntry entry, Object newValue) {
    if (!isConfigLoaded || entry.forgeValue == null) {
      EverMod.LOGGER.info(
          "Guardado omitido: El sistema de configuracion de Forge no se ha cargado por completo aun.");
      return;
    }

    try {
      ((ForgeConfigSpec.ConfigValue<Object>) entry.forgeValue).set(newValue);
      entry.syncToField();
      spec.save();
    } catch (Exception e) {
      EverMod.LOGGER.error("Error al guardar la configuracion: " + entry.field.getName());
      e.printStackTrace();
    }
  }
}
