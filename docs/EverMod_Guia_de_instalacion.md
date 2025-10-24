# 🧩 Guía de instalación y uso — EverMod Framework

**Autor:** Wipodev
**Versión:** 1.0
**Última actualización:** 2025-10-23

---

## 🎯 Objetivo

Esta guía explica cómo **instalar EverMod** y cómo agregar tus propios mods dentro del entorno de trabajo del framework.

---

## 🧱 Estructura general del proyecto

```bash
/EverMod/
│
├── framework/                    # Núcleo del framework EverMod
│   ├── evermod-base/             # API abstracta común
│   ├── evermod-1.19.2/           # Implementación Forge 1.19.2
│   └── evermod-1.20.1/           # Implementación Forge 1.20.1
│
├── mods/                         # Carpeta para tus mods
│   ├── john666/
│   ├── omebuddy/
│   └── silentmask/
│
├── gradle/
├── build.gradle
├── evermix.bat
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradlew
```

---

## ⚙️ Instalación

### 🔹 Opción 1 — Descargar el ZIP

1. Ve al repositorio oficial de EverMod.
2. Descarga el archivo ZIP y descomprímelo en una carpeta local.
3. Abre la carpeta `/EverMod` en tu IDE (VSCode o IntelliJ IDEA).

### 🔹 Opción 2 — Clonar el repositorio con Git

```bash
git clone https://github.com/WipoDev/EverMod.git
```

Si el entorno ya contiene submódulos (mods agregados como repositorios externos), debes clonarlo de forma recursiva:

```bash
git clone --recurse-submodules https://github.com/WipoDev/EverMod.git
```

Para actualizar submódulos más adelante:

```bash
git submodule update --init --recursive
```

---

## 🧩 Agregar un nuevo mod

Existen **dos formas principales** de agregar un mod al entorno EverMod:

### 🏗️ 1. Crear un mod nuevo dentro de `mods/`

1. Dentro de la carpeta `mods/`, crea una nueva carpeta con el nombre de tu mod:

   ```bash
   mkdir mods/mimod
   ```

2. Estructura mínima requerida:

   ```bash
   mods/mimod/
   ├── build.gradle
   ├── gradle.properties
   ├── src/main/java/net/wipodev/mimod/
   │   └── MainMod.java
   └── src/main/resources/META-INF/mods.toml
   ```

3. Agrega el mod al archivo `settings.gradle` en la raíz:

   ```groovy
   include("mods:mimod")
   ```

4. Configura su dependencia hacia EverMod:

   ```groovy
   dependencies {
       minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"
       implementation project(":framework:evermod-${minecraft_version}")
   }
   ```

---

### 🔗 2. Agregar un mod existente como **submódulo Git**

Si tu mod está en un repositorio independiente, puedes vincularlo como submódulo dentro del espacio de trabajo de EverMod.

1. Desde la raíz del proyecto EverMod, ejecuta:

   ```bash
   git submodule add https://github.com/usuario/mimod.git mods/mimod
   ```

2. Para incluirlo en la compilación, agrega en `settings.gradle`:

   ```groovy
   include("mods:mimod")
   ```

3. Cuando clones tu entorno EverMod más adelante, recuerda usar:

   ```bash
   git clone --recurse-submodules https://github.com/usuario/EverModWorkspace.git
   ```

4. Para actualizar todos los mods vinculados:

   ```bash
   git submodule update --remote --merge
   ```

---

## 🧱 Archivos mínimos necesarios para un mod

| Archivo             | Descripción                                           |
| ------------------- | ----------------------------------------------------- |
| `build.gradle`      | Define dependencias y configuración del mod.          |
| `gradle.properties` | Contiene variables como versión de Minecraft y Forge. |
| `mods.toml`         | Archivo de metadatos requerido por Forge.             |
| `pack.mcmeta`       | Información básica del paquete de recursos.           |
| `MainMod.java`      | Clase principal del mod con anotación `@Mod`.         |

Ejemplo básico:

```java
@Mod("mimod")
public class MainMod {
    public MainMod() {
        System.out.println("Mi mod se ha cargado correctamente!");
    }
}
```

---

## 🧮 Configuración de `settings.gradle`

Ejemplo completo:

```groovy
rootProject.name = "EverMod"

include("framework:evermod-base")
include("framework:evermod-1.19.2")
include("framework:evermod-1.20.1")

include("mods:john666")
include("mods:omebuddy")
include("mods:silentmask")
include("mods:mimod")
```

---

## 🧪 Compilar los mods

Para compilar un mod en particular:

```bash
gradlew :mods:mimod:build
```

Para compilar todos los mods al mismo tiempo:

```bash
gradlew build
```

Para ejecutar uno de los mods:

```bash
gradlew :mods:mimod:runClient
```

---

## 🧠 Próximamente — Generador automático de mods

EverMod incluirá próximamente un **script o tarea Gradle** para crear automáticamente la estructura de un nuevo mod.

Ejemplo (en desarrollo):

```bash
gradlew createMod --name=mimod --external=true
```

- `--name`: nombre del nuevo mod.
- `--external`: si se desea crear como submódulo externo o dentro del workspace.

Esto permitirá automatizar la creación de mods listos para compilar sin configurar manualmente cada archivo.

---

## ⚙️ Licencia

Guía técnica oficial para el uso de **EverMod Framework**.
© 2025 — Wipodev. Todos los derechos reservados.
