# ⚙️ EverSyncHandler — Automatic Synchronization System

## 🎯 Objective

Automate the synchronization of entity or configuration data between server and client without requiring mod developers to write manual network packets.

---

## 🧠 Overview

EverSyncHandler allows mod developers to mark any field with `@EverSync`, and EverMod automatically detects changes, serializes data, and updates the corresponding client or server side transparently.

---

## 🧩 Core Components

### 1️⃣ `@EverSync`

Annotation to mark variables that should be synchronized.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EverSync {}
```

---

### 2️⃣ `EverSyncHandler`

Central manager that tracks all annotated fields, detects changes, and triggers synchronization packets when data differs.

```java
public class EverSyncHandler {
    private static final Map<UUID, Map<String, Object>> lastValues = new HashMap<>();

    public static void trackEntity(Entity entity) {
        UUID id = entity.getUUID();
        Map<String, Object> current = getSyncedValues(entity);

        Map<String, Object> previous = lastValues.get(id);
        if (previous == null || !previous.equals(current)) {
            lastValues.put(id, current);
            ChannelManager.sendToAllClients(new EverSyncPacket(entity, current));
        }
    }

    private static Map<String, Object> getSyncedValues(Object target) {
        Map<String, Object> values = new HashMap<>();
        for (Field f : target.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(EverSync.class)) {
                f.setAccessible(true);
                try { values.put(f.getName(), f.get(target)); } catch (Exception ignored) {}
            }
        }
        return values;
    }
}
```

---

### 3️⃣ `EverSyncPacket`

Internal EverMod packet automatically sent when a tracked value changes.

```java
@EverPacket
public class EverSyncPacket extends PacketBase<EverSyncPacket> {
    private UUID entityId;
    private Map<String, Object> values;

    public EverSyncPacket() {}
    public EverSyncPacket(Entity e, Map<String, Object> data) {
        this.entityId = e.getUUID();
        this.values = data;
    }

    @Override
    public void encode(EverBuffer buf) { /* serialize values */ }

    @Override
    public EverSyncPacket decode(EverBuffer buf) { /* deserialize values */ return this; }

    @Override
    public void handle(EverContext ctx) {
        ctx.runClient(() -> EverSyncHandler.applyChanges(entityId, values));
    }
}
```

---

### 4️⃣ Applying Updates

On the client side, EverMod applies received updates automatically:

```java
public static void applyChanges(UUID id, Map<String, Object> data) {
    Entity e = Minecraft.getInstance().level.getEntity(id);
    if (e == null) return;

    for (Field f : e.getClass().getDeclaredFields()) {
        if (f.isAnnotationPresent(EverSync.class) && data.containsKey(f.getName())) {
            f.setAccessible(true);
            try { f.set(e, data.get(f.getName())); } catch (Exception ignored) {}
        }
    }
}
```

---

## ⚙️ Workflow Summary

1. Modder marks fields with `@EverSync`.
2. EverSyncHandler detects when the entity or config changes.
3. An internal `EverSyncPacket` is automatically sent.
4. The receiving side updates the corresponding values instantly.

---

## 🚀 Benefits

| Feature        | Description                                                          |
| -------------- | -------------------------------------------------------------------- |
| 🔄 Auto-sync   | No need to write manual packets or handlers.                         |
| 🧩 Modular     | Works identically across all versions via EverBuffer/EverContext.    |
| ⚙️ Extensible  | Can include interpolation, update intervals, or conditional syncing. |
| 🧠 Transparent | Developers only use `@EverSync` — EverMod handles everything else.   |

---

## 🔮 Future Improvements

- Add sync frequency control (e.g., `@EverSync(rate = 20)`).
- Allow selective side targeting (`@EverSync(side = CLIENT)`).
- Extend to config synchronization and saved data.

---

## 🏁 Summary

EverSyncHandler introduces a fully automated data synchronization layer for EverMod. It dramatically reduces boilerplate and ensures seamless, version-independent state updates between client and server.
