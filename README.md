# EverMod Framework

## 🧩 Overview

EverMod is a **modular framework** designed to **abstract version differences** between Minecraft Forge environments. Its main goal is to allow mod developers to write their code once and **compile it for multiple Minecraft versions** without modifying the mod's source code.

EverMod simplifies mod development and maintenance by providing pre-built modules for each supported version of Minecraft, along with utilities for networking, sound handling, and position-based operations.

---

## 🏗️ Project Structure

```bash
/EverMod/
│
├── evermod-1.19.2/           # Implementation for Forge 1.19.2
├── evermod-1.20.1/           # Implementation for Forge 1.20.1
├── evermod-1.21/             # Implementation for Forge 1.21
├── .gitattributes
├── .gitignore
└── README.md
```

Each EverMod version module contains the same **API interface**, but adapted to its corresponding **Forge API and Minecraft internals**, ensuring maximum compatibility.

---

## ⚙️ Working With the EverMod Framework

EverMod supports four integration methods. While all rely on physical source injection to avoid runtime conflicts, the Multi-Project method is the official recommendation for professional cross-version development.

### 1️⃣ Multi-Version Method (Official & Recommended) ⭐

The most robust way to manage a single mod codebase across multiple Minecraft versions. It uses a **centralized root** to manage shared logic and specific "project containers" for each version.

#### Folder Structure

```text
MyMod_Root/
├── EverMod/                # Git Submodule
├── common/                 # YOUR MOD CODE (Single source of truth)
│   └── src/main/java/      # Common logic using EverMod API
├── projects/               # Version-specific build containers
│   ├── forge-1.19.2/
│   ├── forge-1.20.1/
│   └── forge-1.21/
├── build.gradle            # Global logic & task centralization
├── settings.gradle         # Subproject auto-discovery
└── gradle.properties       # Global mod metadata (ID, Name, Version)
```

#### Why use this?

- **Single Source:** Edit your mod once in `/common`, and it updates for all versions.
- **Centralized Metadata:** Change your mod version or authors in the root `gradle.properties` only once.
- **No Branching:** Switch between Minecraft versions in your IDE without changing Git branches.
- **Batch Compilation:** Run `./gradlew build` to generate all version JARs at once.

#### settings.gradle

```gradle
rootProject.name = "MyPrivateMod"
include("EverMod")
include("common")

file('projects').eachDir { dir ->
    if (new File(dir, 'build.gradle').exists()) {
        include "projects:${dir.name}"
    }
}
```

#### build.gradle

In this recommended setup, your version-specific `build.gradle` (e.g., `projects/forge-1.21/build.gradle`) stays clean by injecting both sources:

```gradle
sourceSets {
    main {
        java {
	          srcDirs = [
                project(":common").file("src/main/java"),
                project(":EverMod").file("evermod-${minecraft_version}/src/main/java")
            ]
        }
	      resources {
            srcDirs = [
                project(":common").file("src/main/resources"),
                "src/generated/resources"
            ]
        }
    }
}
```

### 2️⃣ Standalone Mod Method (Legacy / Single Version)

The framework lives inside a standard Forge mod folder. Best for mods that only target one specific version or developers who prefer manual branch management.

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
            srcDir project(":EverMod").file("evermod-${minecraft_version}/src/main/java")
        }
    }
}
```

### 3️⃣ Workspace Method (Shared Development Environment)

Best suited for developers working on multiple different mods simultaneously that all depend on the same local EverMod instance.

#### Folder Structure

```text
Workspace_Root/
├── EverMod/                # Git Submodule
├── projects/               # mods folder
│   ├── MyMod1/
│   ├── MyMod2/
│   └── MyMod3/
└── settings.gradle         # Subproject auto-discovery
```

#### settings.gradle

```gradle
rootProject.name = "evermod-workspace"

include("EverMod")

file('projects').eachDir { dir ->
    if (new File(dir, 'build.gradle').exists()) {
        include "projects:${dir.name}"
    }
}
```

#### build.gradle (Mod)

```gradle
sourceSets {
    main {
        java {
            srcDir project(":EverMod").file("evermod-${minecraft_version}/src/main/java")
        }
    }
}
```

### 4️⃣ Internal Integration (Vendorized Copy)

A manual copy of the framework source into your package structure. Not recommended as it breaks framework update paths.

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
