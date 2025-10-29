# 🎧 EverMod SoundController System

## 🎯 Objective

Provide a unified, version-independent sound control system for the Minecraft client that allows precise playback, volume adjustment, and looping behavior for entity-bound sounds across all supported versions (1.19.2, 1.20.1, 1.21).

---

## 🧠 Overview

The **EverMod SoundController** system offers a stable and centralized API to handle complex sound logic directly on the client side without depending on Forge or version-specific APIs. It leverages Minecraft’s native sound engine for reliable performance and precise control.

---

## 🧩 Core Components

### 1️⃣ `SoundController`

Static utility class responsible for creating, managing, and stopping sounds associated with entities.

**Key Features:**

- Tracks active sounds by entity (`UUID → SoundEvent → VariableVolumeSound`).
- Prevents duplicate sounds from overlapping on the same entity.
- Supports looping sounds and category-based playback (e.g., HOSTILE, AMBIENT, MUSIC).
- Provides fine-grained runtime control.

**Main API:**

```java
play(entity, sound, volume, pitch);
play(entity, sound, SoundSource, volume, pitch, looping);
stop(entity, sound);
stopAll();
isPlaying(entity, sound);
setVolume(entity, sound, volume, pitch);
```

**Implementation Highlights:**

- Uses `Minecraft.getInstance().getSoundManager()` for direct sound control.
- Stores references to `VariableVolumeSound` objects for live manipulation.
- Automatically cleans up inactive sounds.

---

### 2️⃣ `VariableVolumeSound`

Subclass of `EntityBoundSoundInstance` that exposes real-time control over volume, pitch, and looping.

**Custom Behavior:**

```java
setVolume(float volume);
setPitch(float pitch);
boolean isLooping();
Attenuation getAttenuation(); // LINEAR by default
```

**Benefits:**

- Real-time audio adjustment (useful for adaptive ambient systems).
- Consistent sound behavior across all versions.
- Flexible loop support for continuous or reactive sounds.

---

## 💡 Integration Example

```java
SoundController.play(entity, MySounds.ROAR.get(), 1.0f, 1.0f);
SoundController.setVolume(entity, MySounds.ROAR.get(), 0.6f, 1.2f);
SoundController.stop(entity, MySounds.ROAR.get());
```

---

## ⚙️ Module Placement

Because this system relies on Minecraft client classes, it resides within **each specialized EverMod version module**, under:

```
client/sounds/
  ├── SoundController.java
  └── VariableVolumeSound.java
```

It cannot be placed inside `evermod-base`, as that module must remain version-agnostic and free of direct client dependencies.

---

## ✅ Advantages

| Feature               | Description                                                          |
| --------------------- | -------------------------------------------------------------------- |
| 🎧 Precision          | Frame-accurate sound playback and looping.                           |
| 🔄 Compatibility      | Works identically in 1.19.2, 1.20.1, and 1.21.                       |
| 🧩 Modular            | Contained within the `client/sounds` package of each version module. |
| ⚙️ Lightweight        | Minimal overhead, no Forge-specific dependencies.                    |
| 🧠 Developer-Friendly | Easy-to-use static API for quick integration.                        |

---

## 🏁 Summary

The **EverMod SoundController System** standardizes and simplifies sound handling for mods. It provides powerful, unified, and version-stable sound control while remaining modular and compatible across all supported Minecraft versions.
