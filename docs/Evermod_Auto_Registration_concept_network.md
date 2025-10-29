# 🧩 EverPacket Auto-Registration Concept

## 🎯 Objective

Simplify packet registration by allowing EverMod to automatically detect and register all network packet classes marked with a special annotation `@EverPacket`.

---

## 🧠 Overview

Currently, mod developers must manually register each packet:

```java
ChannelManager.registerMessage(SkinSyncPacket.class);
```

With the proposed `@EverPacket` system, packets would self-register during initialization, reducing boilerplate and potential errors.

---

## 🧱 Implementation Components

### 1️⃣ `@EverPacket` Annotation

A simple annotation to mark packet classes for automatic registration.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EverPacket {}
```

### 2️⃣ `EverModBootstrap`

A helper class that scans the specified package for all annotated classes and registers them automatically.

```java
public class EverModBootstrap {
    public static void registerPackets(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> packets = reflections.getTypesAnnotatedWith(EverPacket.class);
        for (Class<?> packetClass : packets) {
            if (PacketBase.class.isAssignableFrom(packetClass)) {
                ChannelManager.registerMessage((Class<? extends PacketBase<?>>) packetClass);
            }
        }
    }
}
```

### 3️⃣ Usage Example

```java
@EverPacket
public class SkinSyncPacket extends PacketBase<SkinSyncPacket> {
    ...
}

public class MyMod {
    public static void init() {
        ChannelManager.register(MODID);
        EverModBootstrap.registerPackets("net.wipodev.mymod.common.network.packets");
    }
}
```

---

## ✅ Benefits

- Eliminates manual packet registration.
- Reduces boilerplate code in mod initialization.
- Prevents missed registrations or version mismatches.
- Keeps mod code cleaner and more declarative.

---

## 🔮 Future Enhancements

- Support annotation parameters (e.g., `@EverPacket(id = 5, side = "client")`).
- Combine with EverMod’s reflection-based registry for dynamic loading.
- Potential integration with EverSync or auto-sync systems.

---

## 🏁 Summary

The `@EverPacket` auto-registration system provides a scalable and maintainable way to register packets automatically, improving the modding experience and keeping EverMod’s networking layer simple and declarative.
