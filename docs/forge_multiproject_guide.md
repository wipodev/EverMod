# 🧭 Guía — Crear un entorno multiproyecto para mods Forge

**Autor:** Wipodev  
**Versión:** 1.0  
**Última actualización:** 2025-10-23

---

## 🎯 Objetivo

Esta guía enseña cómo crear desde cero un **entorno multiproyecto Gradle** para mods de **Minecraft Forge**.  
Está pensada para desarrolladores que desean mantener varios mods o módulos compartidos en un mismo workspace.

---

## 🧱 Estructura base

```bash
/workspace/
│
├── common/              # Código compartido (API, utilidades, clases base)
│   ├── build.gradle
│   └── src/main/java/net/example/common/
│
├── modA/                # Primer mod
│   ├── build.gradle
│   └── src/main/java/net/example/moda/
│
├── modB/                # Segundo mod
│   ├── build.gradle
│   └── src/main/java/net/example/modb/
│
├── gradle/
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradlew
```

Cada carpeta es un **subproyecto Gradle** con su propio `build.gradle`, pero todos se compilan dentro del mismo entorno.

---

## ⚙️ Configurar `settings.gradle`

En la raíz del proyecto multiproyecto (`/workspace`):

```groovy
rootProject.name = "ForgeWorkspace"

include("common")
include("modA")
include("modB")
```

Gradle reconocerá automáticamente los subproyectos como módulos del workspace.

---

## 🧩 Configuración del módulo común (`common`)

```groovy
plugins {
    id 'java'
}

group = 'net.example.common'
version = '1.0.0'

repositories {
    mavenCentral()
}
```

El módulo `common` puede contener utilidades, clases base, o código que compartirán todos los mods.

---

## 🏗️ Configuración de un mod (`modA` o `modB`)

```groovy
plugins {
    id 'net.minecraftforge.gradle' version '5.1.+'
    id 'java'
}

group = 'net.example.moda'
version = '0.1.0'

repositories {
    mavenCentral()
    maven {
        name = 'Forge'
        url = 'https://maven.minecraftforge.net'
    }
}

dependencies {
    minecraft "net.minecraftforge:forge:1.20.1-47.3.0"
    implementation project(":common")
}
```

Así, cada mod puede acceder al código del módulo común sin duplicarlo.

---

## ⚒️ Ejemplo de flujo de dependencias

```text
modA → common
modB → common
```

Ambos mods usan el mismo código base, evitando mantener múltiples copias de las mismas utilidades.

---

## 🧪 Compilar los mods

Para compilar un mod en particular:

```bash
gradlew :modA:build
```

Para compilar todos los mods al mismo tiempo:

```bash
gradlew build
```

Para ejecutar uno de los mods:

```bash
gradlew :modA:runClient
```

---

## ⚙️ Recomendaciones

1. **Evita dependencias circulares.**  
   Solo el código común debe ser compartido.

2. **Mantén coherencia en nombres de paquetes.**  
   Usa un esquema como `net.tuempresa.modnombre`.

3. **Centraliza configuraciones globales.**  
   Usa el `gradle.properties` para definir versiones y memoria.

4. **Puedes agregar más módulos.**  
   Solo necesitas crear la carpeta, su `build.gradle` y añadirlo a `settings.gradle`.

---

## 🧠 Diferencias con EverMod

| Característica                 | Multiproyecto básico            | EverMod Framework                |
| ------------------------------ | ------------------------------- | -------------------------------- |
| Código compartido              | Manual (módulo `common`)        | Automático (API abstracta)       |
| Compatibilidad entre versiones | Manual                          | Estandarizada por versión        |
| Escalabilidad                  | Media                           | Alta                             |
| Ideal para...                  | Proyectos pequeños o personales | Frameworks o colecciones de mods |

---

## 🧭 Conclusión

Un entorno multiproyecto Forge te permite mantener múltiples mods dentro del mismo workspace de forma organizada.  
Si en el futuro quieres un sistema más avanzado y modular, puedes migrar a **EverMod Framework** sin cambiar tu estructura básica.

---

## ⚙️ Licencia

Guía educativa libre para desarrolladores Forge.  
© 2025 — Wipodev
