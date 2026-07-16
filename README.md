## [![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/wipodev/EverMod)

# EverMod Framework

EverMod is a high-performance, **modular cross-version abstraction framework** designed for Minecraft Forge. It enables mod developers to write their codebase once and target multiple Minecraft versions simultaneously (from 1.19.2 through 1.21.1+) without modifying the core logic, maintaining separate Git branches, or introducing runtime performance overhead.

---

## ⚡ Key Architectural Concepts

### 📦 Compile-Time Source Injection (Zero Runtime Overhead)

Unlike frameworks that rely on heavy reflection, dynamic class loading, or runtime proxying (which degrade client/server performance), EverMod injects its version-specific abstraction modules directly during the compilation phase using Gradle SourceSets. This ensures the resulting JAR contains direct, devirtualized, and optimized JVM calls tailored to each target Minecraft environment.

### 🌐 Key Abstractions Provided

Minecraft internals and Mojang mappings change frequently. EverMod encapsulates these breaking changes under a unified, stable API:

- **Networking & Buffers (`ChannelManager`, `EverContext`, `EverBuffer`):** Bridges context differences (e.g., Minecraft 1.20.1's `NetworkEvent.Context` vs 1.21.1's `CustomPayloadEvent.Context` within [EverContext.java](https://github.com/wipodev/EverMod/blob/main/evermod-1.20.1/src/main/java/net/evermod/network/io/EverContext.java)) and abstracts packet dispatching and automatic registration via scanning.
- **Rendering & GUI (`EverGui`):** Unifies rendering across major rendering engine rewrites, automatically handling the differences between `PoseStack` (1.19.2) and `GuiGraphics` (1.20.1+).
- **Creative Tabs & Items (`EverItem`):** Emulates the deprecated `Item.Properties.tab(...)` builder syntax in newer versions by tracking item-tab associations in static registry maps and automatically registering them via mod-bus events (`BuildCreativeModeTabContentsEvent`).
- **Entity & Sync Data (`EverEntity`):** Handles signature changes of sync data definitions (such as the introduction of `SynchedEntityData.Builder` in 1.21.1) and provides cross-version methods for entity level retrieval (`level` field vs `level()` method) and generic damage sources.
- **Saved Data (`EverSavedData`):** Bridges serialization differences, shielding the developer from new data-fixing structures and registry lookups (`HolderLookup.Provider`) in 1.21+.

---

## 🏗️ Project Structure

```text
/EverMod/
├── evermod-1.19.2/           # Abstraction module for Forge 1.19.2
├── evermod-1.20.1/           # Abstraction module for Forge 1.20.1
├── evermod-1.21/             # Abstraction module for Forge 1.21
├── evermod-1.21.1/           # Abstraction module for Forge 1.21.1
├── .gitattributes
├── .gitignore
└── README.md
```

Each version module exposes the exact same API signature, allowing you to link them dynamically at compile time.

---

## ⚙️ Development Workflows

EverMod supports several integration methodologies. The **Multi-Version Workspace** is highly recommended for production environments.

### 1️⃣ Multi-Version Workspace (Recommended) ⭐

This setup manages a single shared codebase under `/common` and builds specific sub-projects for each target version.

#### Directory Layout

```text
MyMod_Root/
├── EverMod/                # Git Submodule
├── common/                 # Single source of truth (Common logic using EverMod API)
│   └── src/main/java/
├── projects/               # Version-specific build targets
│   ├── forge-1.19.2/
│   ├── forge-1.20.1/
│   ├── forge-1.21/
│   └── forge-1.21.1/
├── build.gradle            # Global build configuration
├── settings.gradle         # Subproject auto-discovery
└── gradle.properties       # Global mod metadata
```

#### Settings Configuration (`settings.gradle`)

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

#### Build Setup for Version Subprojects (`projects/forge-1.20.1/build.gradle`):

Inject both the common logic and the corresponding EverMod adapter module:

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

> [!TIP]
> With this setup, you can switch targeted Minecraft versions inside your IDE seamlessly, compile all versions in parallel, and share all assets without maintaining separate code branches.

---

### 2️⃣ Standalone Mod (Single Target / Legacy)

Best for mods targeting only one specific version or projects using traditional Git branching strategies.

#### Directory Layout

```text
MyPrivateMod/
├── EverMod/                # Git Submodule
├── src/main/java/          # Mod code referencing EverMod API
├── build.gradle
└── settings.gradle
```

#### Gradle Setup (`build.gradle`)

```gradle
sourceSets {
    main {
        java {
            srcDir project(":EverMod").file("evermod-${minecraft_version}/src/main/java")
        }
    }
}
```

---

### 3️⃣ Multi-Project Workspace

Ideal for developers maintaining multiple independent mods that share a centralized EverMod setup.

#### Directory Layout

```text
Workspace_Root/
├── EverMod/                # Git Submodule
├── projects/               # Independent mod subprojects
│   ├── MyMod1/
│   ├── MyMod2/
│   └── MyMod3/
└── settings.gradle         # Workspace discovery
```

#### Settings Configuration (`settings.gradle`)

```gradle
rootProject.name = "evermod-workspace"
include("EverMod")

file('projects').eachDir { dir ->
    if (new File(dir, 'build.gradle').exists()) {
        include "projects:${dir.name}"
    }
}
```

---

## 🚀 Core API Features

- **📡 Simplified Network API:** Register and handle custom payloads effortlessly using `@EverPacket` annotations, `SimpleChannel`, `EverBuffer`, and `EverContext`.
- **🔊 Sound Fading and Tracking:** Play, stop, transition, or fade 3D sounds bound to entities over time using `SoundController` and `VariableVolumeSound`.
- **🧭 Navigation & Pathfinding Helpers:** Check standing safety (`isValidStandingPos`), cave properties, or evaluate paths via `EverPathEvaluator` regardless of whether the platform uses `BlockPathTypes` or `PathType`.
- **⚔️ Cross-Version Combat & Items:** Standardize item attributes, durability, food values, and sword mechanics across vanilla registry migrations using `EverSwordItem` and `EverItemCheck`.

---

## 🧰 Complementary Tools

EverMod is fully supported by its surrounding ecosystem:

1. **[EverMod CLI](https://github.com/wipodev/evermod-cli):** Command-line tool to bootstrap template projects, manage Git submodules, and package build versions.
2. **[EverMod Template](https://github.com/wipodev/evermod-template):** A flexible template system using **Jinja2** to dynamically render base files based on a JSON-version database.

---

## 🧩 Compatibility Matrix

| Minecraft Version | Forge Version | Java Version |
| ----------------- | ------------- | ------------ |
| 1.19.2            | 43.5.0        | 17           |
| 1.20.1            | 47.4.10       | 17           |
| 1.21              | 51.0.33       | 21           |
| 1.21.1            | 52.1.6        | 21           |

---

## 📜 License

**All Rights Reserved.**
Developed by **Wipodev** — [https://www.wipodev.com](https://www.wipodev.com)

---

## 🌐 Repositories

- **Main Framework:** [EverMod](https://github.com/wipodev/EverMod)
- **CLI Tool:** [EverMod CLI](https://github.com/wipodev/evermod-cli)
- **Template System:** [EverMod Template](https://github.com/wipodev/evermod-template)
