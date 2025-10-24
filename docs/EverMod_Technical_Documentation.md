# 🧠 Documento Técnico — EverMod Framework

**Autor:** Wipodev
**Versión del documento:** 1.1
**Última actualización:** 2025-10-23

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

```bash
/EverMod/
│
├── framework/                    # Núcleo del framework EverMod
│   ├── evermod-base/             # Define la API abstracta común
│   │   └── src/main/java/net/evermod/
│   │       ├── network/ChannelBase.java
│   │       ├── entity/EntityBase.java
│   │       ├── world/WorldBase.java
│   │       ├── item/ItemBase.java
│   │       └── util/EverLogger.java
│   │
│   ├── evermod-1.19.2/           # Implementación Forge 1.19.2
│   │   └── src/main/java/net/evermod/
│   │       ├── network/ChannelManager.java
│   │       ├── entity/EntityManager.java
│   │       └── world/WorldManager.java
│   │
│   └── evermod-1.20.1/           # Implementación Forge 1.20.1
│       └── src/main/java/net/evermod/
│           ├── network/ChannelManager.java
│           ├── entity/EntityManager.java
│           └── world/WorldManager.java
│
└── mods/                         # Mods que utilizan EverMod
    ├── john666/
    ├── omebuddy/
    └── silentmask/
```

---

## 🧠 Estructura modular

### 1. Módulo `evermod-base`

Contiene todas las clases abstractas comunes y utilidades compartidas.

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
    implementation project(":framework:evermod-base")
}
```

### 3. Módulos de mods

Cada mod depende del módulo correspondiente a su versión de Minecraft:

```groovy
dependencies {
    implementation project(":framework:evermod-${minecraft_version}")
}
```

---

## ⚙️ Sistema de dependencias

### Encadenamiento automático

Cuando un mod depende de `evermod-1.19.2`, Gradle resuelve transitivamente:

```
john666 → framework/evermod-1.19.2 → framework/evermod-base
```

Por lo tanto, el mod tiene acceso completo a todas las clases base sin declarar dependencias adicionales.

### Compilación estática

EverMod no utiliza _runtime loading_ ni reflexión.
Cada versión se compila directamente con el código correcto en tiempo de construcción.

---

## 🧭 Convenciones de paquetes

| Subpaquete            | Descripción                                 |
| --------------------- | ------------------------------------------- |
| `net.evermod.network` | Manejo de canales y paquetes de red.        |
| `net.evermod.entity`  | Registro y control de entidades.            |
| `net.evermod.world`   | Interacción con el mundo, bloques y biomas. |
| `net.evermod.item`    | Manejo de ítems personalizados.             |
| `net.evermod.util`    | Utilidades comunes, logging y helpers.      |

---

## 🔨 Configuración multiproyecto

### `settings.gradle`

```groovy
include("framework:evermod-base")
include("framework:evermod-1.19.2")
include("framework:evermod-1.20.1")
include("mods:john666")
include("mods:omebuddy")
include("mods:silentmask")
```

### `build.gradle` del mod

```groovy
dependencies {
    minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"
    implementation project(":framework:evermod-${minecraft_version}")
}
```

---

## 🧪 Plan de pruebas

| Tipo de prueba                    | Descripción                                             |
| --------------------------------- | ------------------------------------------------------- |
| **Compilación base**              | Verificar que los mods compilan sin errores.            |
| **Verificación de API**           | Asegurar que todas las clases base están implementadas. |
| **Ejecución en cliente/servidor** | Confirmar correcto funcionamiento en Forge.             |
| **Compatibilidad binaria**        | Verificar que los imports se mantienen iguales.         |

---

## 🧭 Futuras expansiones

- **EverData:** sistema persistente cross-version.
- **EverRender:** capa gráfica unificada.
- **Compatibilidad con 1.21+ y NeoForge.**
- **Guía pública en español sobre entornos multiproyecto Forge.**

---

## ⚙️ Licencia

Framework privado desarrollado por **Wipodev**
Todos los derechos reservados © 2025
