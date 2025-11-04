# 🌍 EverMod Framework

<p align="center">
<img src="./assets/logo.png" alt="logo" width="60%">
</p>

**EverMod** es un _framework universal para mods de Minecraft Forge_, diseñado para que tus mods puedan desarrollarse **una sola vez** y compilarse para diferentes versiones de Minecraft sin modificar su código fuente.

Su nombre proviene de la idea de crear **"mods para siempre"**, que solo necesiten cambiar la dependencia de EverMod correspondiente a la versión del juego.

---

## 🧩 Objetivo

EverMod unifica todas las funciones y utilidades comunes a través de una **API base abstracta**, la cual es implementada internamente por cada versión específica de Minecraft.

De esta forma, los mods que dependan de EverMod:

- Usan **los mismos imports** sin importar la versión de Minecraft.
- Solo necesitan cambiar una línea en el `build.gradle` para compilar con otra versión.
- No requieren runtime, detección de versión ni configuración adicional.

---

## 🧱 Estructura del proyecto

```
/EverMod/
│
├── evermod-base/               # API abstracta y lógica compartida
│   └── src/main/java/net/evermod/
│       ├── network/ChannelBase.java
│       ├── entity/EntityBase.java
│       ├── world/WorldBase.java
│       └── item/ItemBase.java
│
├── evermod-1.19.2/             # Implementación Forge 1.19.2
│   ├── build.gradle
│   └── src/main/java/net/evermod/
│       ├── network/ChannelManager.java
│       ├── entity/EntityManager.java
│       └── world/WorldManager.java
│
├── evermod-1.20.1/             # Implementación Forge 1.20.1
│   ├── build.gradle
│   └── src/main/java/net/evermod/
│       ├── network/ChannelManager.java
│       ├── entity/EntityManager.java
│       └── world/WorldManager.java
│
└── mods/                       # Mods que usan EverMod
    ├── john666/
    └── omebuddy/
```

---

## ⚙️ Cómo funciona

Cada módulo de versión (`evermod-1.19.2`, `evermod-1.20.1`, etc.) **extiende** las clases abstractas del módulo `evermod-base`.

Por ejemplo:

```java
// evermod-base/network/ChannelBase.java
package net.evermod.network;

public abstract class ChannelBase {
    protected final String modid;

    public ChannelBase(String modid) {
        this.modid = modid;
    }

    public abstract void register();
    public abstract void sendToClient(Object packet);
    public abstract void sendToServer(Object packet);
}
```

```java
// evermod-1.19.2/network/ChannelManager.java
package net.evermod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ChannelManager extends ChannelBase {
    private final SimpleChannel channel;

    public ChannelManager(String modid) {
        super(modid);
        this.channel = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(modid, "main"))
            .networkProtocolVersion(() -> "1")
            .clientAcceptedVersions("1"::equals)
            .serverAcceptedVersions("1"::equals)
            .simpleChannel();
    }

    @Override
    public void register() {
        System.out.println("Registrando canal (Forge 1.19.2)");
    }

    @Override
    public void sendToClient(Object packet) {}
    @Override
    public void sendToServer(Object packet) {}
}
```

---

## 🔧 Cómo usar EverMod en un mod

En el mod solo se importa el paquete común, **sin importar la versión de Minecraft:**

```java
import net.evermod.network.ChannelManager;

public class MyMod {
    private final ChannelManager channel = new ChannelManager("mymod");

    public void init() {
        this.channel.register();
    }
}
```

---

## 🏗️ Configuración de compilación

### `settings.gradle`

Asegúrate de incluir todos los módulos del framework y tus mods:

```groovy
include("evermod-base")
include("evermod-1.19.2")
include("evermod-1.20.1")
include("john666")
include("omebuddy")
```

---

### `build.gradle` del mod

```groovy
dependencies {
    // Dependencia de Forge
    minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"

    // Framework EverMod - versión automática según la versión de Minecraft
    implementation project(":evermod-${minecraft_version}")
}
```

> 💡 Solo necesitas cambiar la propiedad `minecraft_version` en `gradle.properties` para usar otra versión de EverMod.  
> No hace falta modificar el código del mod ni los imports.

---

## 🔗 Encadenamiento automático

Cada módulo de EverMod (por versión) incluye internamente el módulo base:

```groovy
// evermod-1.19.2/build.gradle
dependencies {
    implementation project(":evermod-base")
}
```

Por lo tanto, cuando compilas un mod que usa EverMod:

- Gradle trae automáticamente el módulo base y la versión correspondiente.
- No necesitas importarlo ni configurarlo manualmente.

---

## 🧰 Ventajas principales

✅ **Código universal:**  
Los mods usan el mismo código en todas las versiones.

✅ **Sin runtime ni reflexión:**  
Todo se resuelve en compilación.

✅ **Imports fijos:**  
Los nombres de clases y paquetes nunca cambian (`net.evermod.*`).

✅ **Estandarización garantizada:**  
Las clases base obligan a mantener la misma API entre versiones.

✅ **Mantenimiento simple:**  
Agregar una nueva versión solo requiere crear un nuevo módulo que extienda las bases existentes.

---

## 🧭 Plan de expansión

1. **Fase 1** — Base del framework (`ChannelBase`, `EntityBase`, `WorldBase`, `ItemBase`).
2. **Fase 2** — Implementaciones Forge para 1.19.2 y 1.20.1.
3. **Fase 3** — Soporte para utilidades comunes (`EverLogger`, `EverUtils`).
4. **Fase 4** — Integración en mods existentes (`John666`, `Omebuddy`, `SilentMask`, etc.).
5. **Fase 5** — Publicación opcional de EverMod como dependencia Maven interna o Git submódulo.

---

## ⚡ Licencia y autoría

Desarrollado por **Wipodev**  
Todos los derechos reservados © 2025

EverMod es un framework privado diseñado para estandarizar el desarrollo de mods Forge multiversión.  
No es un mod ni requiere instalación independiente: los mods que lo usen incorporan su código directamente.

---
