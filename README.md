# EverMod Framework

## 🧩 Overview

EverMod is a **modular framework** designed to **abstract version differences** between Minecraft Forge environments. Its main goal is to allow mod developers to write their code once and **compile it for multiple Minecraft versions** without modifying the mod's source code.

EverMod simplifies mod development and maintenance by providing pre-built modules for each supported version of Minecraft, along with utilities for networking, sound handling, and position-based operations.

---

## 🏗️ Project Structure

```bash
/EverMod/
│
├── framework/                    # Core EverMod modules
│   ├── evermod-1.19.2/           # Implementation for Forge 1.19.2
│   ├── evermod-1.20.1/           # Implementation for Forge 1.20.1
│   └── evermod-1.21/             # Implementation for Forge 1.21
├── .gitattributes
├── .gitignore
└── README.md
```

Each EverMod version module contains the same **API interface**, but adapted to its corresponding **Forge API and Minecraft internals**, ensuring maximum compatibility.

---

## ⚙️ Working With the EverMod Framework

EverMod supports **three official integration methods**, depending on
your project scale and maintenance strategy. All methods rely on
**physical source injection**, avoiding runtime dependencies and Forge
plugin conflicts.

### 1️⃣ Standalone Mod Method (Recommended)

The **official and recommended** EverMod configuration.\
The framework lives inside the mod folder (preferably as a Git
submodule) and is treated as a **passive source container**.

#### Folder Structure

```text
MyPrivateMod/
├── EverMod/
│   ├── evermod-1.19.2/
│   ├── evermod-1.20.1/
│   └── evermod-1.21/
├── src/main/java/
├── build.gradle
└── settings.gradle
```

#### settings.gradle

```gradle
rootProject.name = "MyPrivateMod"
include("EverMod")
```

#### build.gradle

```gradle
sourceSets {
    main {
        java {
            srcDir project(":EverMod")
                .file("evermod-${minecraft_version}/src/main/java")
        }
    }
}

minecraft {
    runs {
        configureEach {
            mods {
                "${mod_id}" {
                    source sourceSets.main
                }
            }
        }
    }
}
```

### 2️⃣ Workspace Method (Shared Development Environment)

Best suited for developers working on **multiple mods simultaneously**
that depend on the same framework.

#### Folder Structure

```text
Workspace_Root/
├── EverMod/
│   └── framework/
│       ├── evermod-1.19.2/
│       ├── evermod-1.20.1/
│       └── evermod-1.21/
├── MyPrivateMod/
└── settings.gradle
```

#### settings.gradle

```gradle
rootProject.name = "evermod-workspace"

include("EverMod:framework:evermod-1.19.2")
include("EverMod:framework:evermod-1.20.1")
include("EverMod:framework:evermod-1.21")
include("MyPrivateMod")
```

#### build.gradle (Mod)

```gradle
sourceSets {
    main {
        java {
            srcDir project(":EverMod:framework:evermod-${minecraft_version}")
                .file("src/main/java")
        }
    }
}
```

### 3️⃣ Internal Integration (Vendorized Copy)

A manual copy of the framework source. Simple but **not recommended**
for long-term projects.

```text
MyPrivateMod/
└── src/main/java/
    ├── com/my_mod/
    └── net/evermod/
```

---

## 🧠 Core Features

### 🔌 Modular Version Abstraction

Each EverMod module isolates all version-specific Forge logic.

### 📡 Network Channel API

Simplified packet registration using `SimpleChannel`, `EverBuffer`, and
`EverContext`.

### 🔊 Sound System API

Unified sound playback and synchronization across server and client.

### 🧭 Utility Helpers

Teleportation helpers, reach detection, and cross-version resource
utilities.

---

## 🧰 Complementary Tools

EverMod is supported by two complementary repositories:

### 1. **EverMod CLI**

A command-line tool for managing EverMod projects.

- Create new mods using the EverMod template.
- Keep the framework and templates updated.
- Compile and package EverMod for distribution.
- Add existing mods into a workspace as Git submodules.
- Generate XML summaries for AI-assisted project documentation.

### 2. **EverMod Template**

A customizable mod template that uses **Jinja2** to dynamically generate base files for any Minecraft version using a **JSON version database**.

---

## 🧩 Compatibility

| Minecraft Version | Forge Version | Java Version |
| ----------------- | ------------- | ------------ |
| 1.19.2            | 43.5.0        | 17           |
| 1.20.1            | 47.4.10       | 17           |
| 1.21              | 51.0.33       | 21           |

---

## 📜 License

**All Rights Reserved.**
Developed by **Wipodev** — [https://www.wipodev.com](https://www.wipodev.com)

---

## 🌐 Repositories

- **Main Framework:** [EverMod](https://github.com/wipodev/EverMod)
- **CLI Tool:** [EverMod CLI](https://github.com/wipodev/evermod-cli)
- **Template System:** [EverMod Template](https://github.com/wipodev/evermod-template)

---

> EverMod — One codebase, multiple Minecraft versions.
