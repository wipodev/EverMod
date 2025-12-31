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
│
├── mods/                         # Mods that use EverMod
│   ├── john666/
│   ├── omebuddy/
│   └── silentmask/
│
├── build.gradle
├── gradle.properties
├── settings.gradle
├── gradlew / gradlew.bat
└── evermix.bat
```

Each EverMod version module contains the same **API interface**, but adapted to its corresponding **Forge API and Minecraft internals**, ensuring maximum compatibility.

---

## ⚙️ How It Works

EverMod can be used in **two main ways** depending on your workflow and project scale:

### 1. Internal Mode
- Install the EverMod module **directly inside the mod’s source folder** (`src/main/java/net/`).
- The module becomes part of the mod’s own codebase.
- No extra Gradle configuration is required.

✅ **Recommended for small or standalone mods.**

### 2. Multi-Project Workspace Mode
- Create a shared **EverMod workspace**.
- Each mod lives inside the `/mods/` folder.
- Add each mod to the root `settings.gradle`:

  ```gradle
  include("john666")
  ```

- In each mod’s `build.gradle`, add the dependency to the desired EverMod version:

  ```gradle
  implementation project(":framework:evermod-${minecraft_version}")
  ```

✅ **Recommended for multi-version or multi-mod development.**

---

## 🧠 Core Features

### 🔌 Modular Version Abstraction
Each `evermod-{version}` module isolates all version-specific logic. This allows your mod to focus on gameplay and logic without worrying about Forge API differences.

### 📡 Network Channel API
Provides a **ready-to-use network channel system** compatible with each Forge version, based on `SimpleChannel`.

- Automatically handles packet registration.
- Simplified buffer and context management (`EverBuffer`, `EverContext`).
- Includes built-in packets like `PlaySoundPacket` for synchronized sound playback across clients.

### 🔊 Sound System API
Provides a **clean and consistent API for sound playback**, updates, and control between server and client.

- `EverSound.playToAll(...)` — broadcast a sound globally.
- `EverSound.playTo(ServerPlayer, ...)` — play to a specific player.
- `EverSound.updateToAll(...)` — dynamically update sound volume/pitch.
- `EverSound.stopAll()` — stop all sounds across clients.

Internally, EverMod uses `ClientSoundHandler` and `SoundController` classes to manage and synchronize sound states across entities.

### 🧭 Utility Helpers
Planned helper classes include:
- **Teleportation and position helpers** — to safely move entities or validate proximity.
- **Reach & arrival verification** — detect when an entity reaches a target point.
- **Cross-version resource utilities** — simplify access to `ResourceLocation` across versions.

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

## 🚀 Getting Started

### 1. Clone the Workspace
```bash
git clone https://github.com/wipodev/EverMod.git
cd EverMod
```

### 2. Add a Mod
Inside the `/mods` folder:
```bash
mkdir mymod
```
Then add it to `settings.gradle`:
```gradle
include("mymod")
```

### 3. Link the Framework
In your mod’s `build.gradle`:
```gradle
dependencies {
    implementation project(":framework:evermod-1.20.1")
}
```

### 4. Run and Build
Compile with:
```bash
./gradlew build
```

---

## 📁 Module Example: evermod-1.20.1

### Key Packages
- `net.evermod.common.network` — Cross-version packet management.
- `net.evermod.common.sounds` — Server-side sound control.
- `net.evermod.client.handlers` — Client sound event handlers.
- `net.evermod.client.sounds` — Volume, pitch, and playback management.

---

## 🧩 Compatibility
| Minecraft Version | Forge Version | Java Version |
|-------------------|----------------|----------------|
| 1.19.2 | 43.5.0 | 17 |
| 1.20.1 | 47.4.10 | 17 |
| 1.21 | 51.0.33 | 21 |

---

## 📜 License
**All Rights Reserved.**  
Developed by **Wipodev** — https://www.wipodev.com

---

## 🌐 Repositories
- **Main Framework:** [EverMod](https://github.com/wipodev/EverMod)
- **CLI Tool:** [EverMod CLI](https://github.com/wipodev/evermod-cli)
- **Template System:** [EverMod Template](https://github.com/wipodev/evermod-template)

---

## 🧩 Future Roadmap
- [ ] Add version 1.21.1 and 1.22 support.
- [ ] Implement teleportation and reach utilities.
- [ ] Create a unified configuration system.
- [ ] Integrate cross-mod event bus.
- [ ] Extend CLI for workspace automation.

---

> EverMod — Simplifying Minecraft Mod Development Across Versions

