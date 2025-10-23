# 🧠 Documento Técnico — EverMod Framework

**Autor:** Wipodev  
**Versión del documento:** 1.0  
**Última actualización:** 2025-10-21

---

## 📘 Resumen

Este documento describe los fundamentos técnicos del framework **EverMod**, su estructura modular, los principios de diseño, y las convenciones necesarias para mantener compatibilidad entre versiones de Minecraft Forge.

EverMod es un _framework unificado_ que permite desarrollar mods compatibles con múltiples versiones de Minecraft sin modificar su código fuente, garantizando una arquitectura coherente, escalable y mantenible.

---

## 🧩 Filosofía del framework

### Objetivo principal

Permitir que los mods creados con EverMod se compilen **para diferentes versiones de Forge** simplemente cambiando la dependencia Gradle, sin modificar clases ni imports.

### Principios de diseño

1. **Transparencia total:** el mod no debe conocer ni importar versiones específicas.
2. **Estandarización:** todos los módulos de versión implementan las mismas firmas.
3. **Compilación estática:** no se utiliza detección de versión en tiempo de ejecución.
4. **Independencia modular:** cada versión puede actualizarse sin afectar a las demás.
5. **Jerarquía simple:** una clase base abstracta define la API; las versiones concretas la extienden.

---

## 🧱 Arquitectura general

```
/EverMod/
│
├── evermod-base/                  # Define la API abstracta común
│   └── src/main/java/net/evermod/
│       ├── network/ChannelBase.java
│       ├── entity/EntityBase.java
│       ├── world/WorldBase.java
│       ├── item/ItemBase.java
│       └── util/EverLogger.java
│
├── evermod-1.19.2/                # Implementación Forge 1.19.2
│   ├── build.gradle
│   └── src/main/java/net/evermod/
│       ├── network/ChannelManager.java
│       ├── entity/EntityManager.java
│       └── world/WorldManager.java
│
├── evermod-1.20.1/                # Implementación Forge 1.20.1
│   ├── build.gradle
│   └── src/main/java/net/evermod/
│       ├── network/ChannelManager.java
│       ├── entity/EntityManager.java
│       └── world/WorldManager.java
│
└── mods/                          # Mods que utilizan EverMod
    ├── john666/
    └── omebuddy/
```

---

## 🧠 Estructura modular

### 1. Módulo `evermod-base`

Contiene todas las clases abstractas comunes y utilidades compartidas.

Ejemplo de clase base:

```java
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

### 2. Módulos por versión (`evermod-1.19.2`, `evermod-1.20.1`, etc.)

Implementan las clases base con la API concreta de Forge y Minecraft correspondiente.

Cada módulo incluye la dependencia al módulo base:

```groovy
dependencies {
    implementation project(":evermod-base")
}
```

### 3. Módulos de mods

Cada mod simplemente depende del módulo correspondiente a su versión de Minecraft:

```groovy
implementation project(":evermod-${minecraft_version}")
```

---

## ⚙️ Sistema de dependencias

### Encadenamiento automático

Cuando un mod depende de `evermod-1.19.2`, Gradle resuelve transitivamente:

```
john666 → evermod-1.19.2 → evermod-base
```

Por lo tanto, el mod tiene acceso completo a todas las clases base sin declarar dependencias adicionales.

### Compilación estática

EverMod no utiliza _runtime loading_ ni reflexión.  
Cada versión se compila directamente con el código correcto en tiempo de construcción.

---

## 🧭 Convenciones de paquetes

### Paquete raíz

El paquete raíz global de EverMod es:

```
net.evermod
```

### Subpaquetes por categoría

| Subpaquete            | Descripción                                              |
| --------------------- | -------------------------------------------------------- |
| `net.evermod.network` | Manejo de canales y paquetes de red.                     |
| `net.evermod.entity`  | Utilidades de entidades, registro, tracking, atributos.  |
| `net.evermod.world`   | Interacciones con el mundo, bloques, posiciones, biomas. |
| `net.evermod.item`    | Registro y comportamiento de ítems personalizados.       |
| `net.evermod.util`    | Herramientas comunes, logging, helpers genéricos.        |

### Convención de nombres de clases

| Tipo                | Sufijo             | Ejemplo                           |
| ------------------- | ------------------ | --------------------------------- |
| Clases base         | `Base`             | `ChannelBase`, `EntityBase`       |
| Implementaciones    | `Manager`          | `ChannelManager`, `EntityManager` |
| Helpers utilitarios | `Helper` / `Utils` | `PositionHelper`, `EverUtils`     |

---

## 🧩 Ejemplo de implementación completa

### Clase base (API común)

```java
package net.evermod.entity;

public abstract class EntityBase {
    public abstract void registerEntities();
    public abstract void spawnAtPlayer(Object player);
}
```

### Implementación concreta

```java
package net.evermod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class EntityManager extends EntityBase {
    @Override
    public void registerEntities() {
        System.out.println("Registrando entidades (Forge 1.19.2)");
    }

    @Override
    public void spawnAtPlayer(Object player) {
        if (player instanceof Player p) {
            ServerLevel level = (ServerLevel) p.level();
            Mob mob = EntityType.ZOMBIE.create(level);
            if (mob != null) {
                mob.setPos(p.getX(), p.getY(), p.getZ());
                level.addFreshEntity(mob);
            }
        }
    }
}
```

---

## 🧰 Lineamientos de desarrollo

1. **Cada clase base debe estar en `evermod-base`.**  
   Ningún módulo de versión debe declarar una nueva API sin base abstracta.

2. **Toda nueva función debe añadirse primero al módulo base.**  
   Esto garantiza que todas las versiones la implementen.

3. **Las clases deben mantener el mismo nombre y paquete en todas las versiones.**  
   Así los mods pueden compilar sin cambios.

4. **Nunca debe haber código Forge específico en `evermod-base`.**  
   Solo lógica neutral o Java puro.

5. **Los métodos abstractos deben documentar su comportamiento esperado.**

6. **Los imports usados en mods siempre serán `net.evermod.*`**  
   No se exponen imports por versión.

---

## 🔨 Convenciones de compilación

### 1. `settings.gradle`

```groovy
include("evermod-base")
include("evermod-1.19.2")
include("evermod-1.20.1")
include("john666")
include("omebuddy")
```

### 2. `build.gradle` del mod

```groovy
dependencies {
    minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"
    implementation project(":evermod-${minecraft_version}")
}
```

---

## 🧮 Flujo de trabajo

1. **Crear nueva versión de EverMod**

   - Copiar carpeta de la versión anterior.
   - Ajustar dependencias y código Forge.
   - Verificar que todas las clases implementen sus métodos base.

2. **Agregar nueva API**

   - Declarar método abstracto en la clase base.
   - Implementarlo en todas las versiones.
   - Añadir documentación de comportamiento esperado.

3. **Actualizar un mod**
   - Cambiar `minecraft_version` en `gradle.properties`.
   - Compilar normalmente.

---

## 🧪 Plan de pruebas

Cada nueva versión de EverMod debe validar:

| Tipo de prueba             | Descripción                                       |
| -------------------------- | ------------------------------------------------- |
| **Compilación base**       | El mod compila sin errores con la nueva versión.  |
| **Verificación de API**    | Todas las clases base están implementadas.        |
| **Ejecutables Forge**      | El mod corre correctamente en cliente y servidor. |
| **Compatibilidad binaria** | Los imports permanecen iguales.                   |

---

## 🧭 Futuras expansiones

- Implementación de **EverData**, un sistema persistente de guardado cross-version.
- Integración de **EverRender**, capa gráfica unificada para entidades y overlays.
- Compatibilidad con futuras versiones de Forge 1.21+.
- Posible soporte para Fabric o NeoForge mediante submódulos alternativos.

---

## ⚙️ Licencia

Framework privado desarrollado por **Wipodev**  
Todos los derechos reservados © 2025

EverMod puede ser utilizado internamente en proyectos de Wipodev, pero no debe redistribuirse sin autorización expresa.

---
