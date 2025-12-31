# Informe Técnico — EverUI (Sistema Declarativo de Interfaces para EverMod)

**Versión del Documento:** 1.0  
**Autor:** Wipodev (EverMod Framework)  
**Estado:** Diseño Aprobado

## 1. Introducción

EverUI es un sistema de interfaz gráfica declarativa diseñado exclusivamente para EverMod, el framework modular que abstrae las diferencias entre versiones de Minecraft.  
El objetivo de EverUI es:

- ofrecer un modelo de UI moderno, simple y declarativo,
- que funcione sin cambios de código entre versiones (1.19.2, 1.20.1, futuras versiones),
- que mantenga la estética de Minecraft Vanilla,
- pero permita estilos y temas personalizados,
- sin exponer ninguna clase interna de Minecraft,
- con una API intuitiva inspirada en Jetpack Compose, adaptada al ecosistema Java.

EverUI se convierte en la solución estándar para crear pantallas, overlays, HUDs y menús dentro de mods compatibles con EverMod.

## 2. Objetivos de Diseño

### 2.1 Simplicidad y Naturalidad
La UI debe construirse con código declarativo, intuitivo y de baja fricción:

```java
Column()
    .padding(10)
    .add(Text("Title").size(18))
    .add(Button("Play").onClick(this::startGame));
```

### 2.2 Compatibilidad Cross-Version
El mod que usa EverUI:

- NO ve clases de Minecraft,
- NO recibe eventos Forge,
- NO pasa GuiGraphics ni GuiComponent,
- NO condiciona por versión.

Cada módulo de EverMod implementa internamente su propia versión de EverUI, con la misma API pública.

### 2.3 Estética Vanilla como Tema Predeterminado
Todas las interfaces generadas por EverUI respetan la estética original de Minecraft:

- botones estilo widgets.png
- fuentes vanilla
- colores vanilla
- bordes pixelados

### 2.4 Comportamiento extendible y composable
Sistema modular que permite crear nuevos componentes sin romper la API existente.

## 3. Arquitectura General

EverUI se compone de:
1. Declarative DSL Layer
2. Node Tree Layer
3. Layout Engine
4. Renderer Engine
5. Event System

## 4. API Pública (Vista del Mod)

### 4.1 Componentes Base

**Contenedores (aceptan .add()):**
- Column()
- Row()
- Box()

**Simples:**
- Text()
- Image()
- Button()

### 4.2 Sistema .add()

```java
col.add(Text("A"));
col.add(Text("A"), Text("B"), Text("C"));
col.addIf(showDebug, Text("DEBUG"));
col.addEach(players, (p,i) -> Text(p.getName()));
```

### 4.3 NodeMapper<T>

```java
public interface NodeMapper<T> {
    Node map(T item, int index);
}
```

### 4.4 Modificadores

```
.padding()
.align()
.size()
.fill()
.center()
.spacing()
.background()
```

### 4.5 Temas
Por defecto: Minecraft Vanilla.

## 5. Implementación Interna

Cada módulo contiene su implementación:
```
net.evermod.ui.*
UiRenderer
UiLayout
UiNode*
UiEvents
```

## 6. Manejo de Eventos

- hover
- click
- focus
- dispatch onClick()

## 7. Layout Engine

- Column
- Row
- Box
- padding
- align
- wrap
- fill
- spacing

## 8. Renderer Engine

Usa APIs propias de cada versión:
- 1.19.2: GuiComponent.blit
- 1.20.1: GuiGraphics.blit

## 9. Ventajas

- Cross-version real
- Declarativo
- Intuitivo
- Estética consistente
- Seguro
- Extensible

## 10. Extensiones Futuras

- Slider
- Checkbox
- TextField
- ScrollView
- Animaciones
- Temas avanzados

## Conclusión

EverUI se define como la capa declarativa oficial de EverMod — moderno, estable, intuitivo y totalmente compatible entre versiones.
