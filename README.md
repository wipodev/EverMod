# 🛠️ EverMod Framework

**El framework de abstracción modular para el desarrollo de mods en Minecraft Forge.**  
Escribe la lógica de tu mod una sola vez y compílalo para múltiples versiones de Minecraft de forma nativa y sin pérdida de rendimiento.

<p align="left">
  <a href="https://evermod.wipodev.com"><img src="https://img.shields.io/badge/🌐_Sitio_Web-evermod.wipodev.com-blue" alt="Web Oficial"></a>
  <a href="https://evermod.wipodev.com/docs"><img src="https://img.shields.io/badge/📖_Documentación-Leer_Guías-orange" alt="Docs"></a>
  <a href="https://evermod.wipodev.com/download"><img src="https://img.shields.io/badge/💻_Descargar-EverMod_CLI-green" alt="CLI Tool"></a>
  <a href="https://github.com/wipodev/evermod-locales"><img src="https://img.shields.io/badge/🌍_Comunidad-Traducciones-purple" alt="Locales"></a>
</p>

---

## 💡 ¿Qué es EverMod?

Crear mods para Minecraft suele requerir mantener múltiples ramas de código o rehacer el proyecto cada vez que Mojang cambia las APIs internas del juego.

**EverMod** resuelve este problema ofreciendo una capa de abstracción unificada. Te permite desarrollar sobre un único código base (`common`) y adaptar tu mod automáticamente a diferentes versiones de Minecraft durante la fase de compilación.

### ✨ Características Principales

- **Cero impacto en rendimiento:** No usa _reflection_ ni procesos pesados en tiempo de ejecución. Tu mod se compila directamente para la versión objetivo.
- **Redes y Paquetes simplificados:** Sistema unificado para enviar datos entre cliente y servidor.
- **Renderizado y GUI unificado:** Manejo transparente de interfaces gráficas entre versiones.
- **Compatibilidad de Sonidos e Ítems:** Abstracciones listas para usar en eventos, registros y audio 3D.

---

## ⚡ ¿Cómo funciona? (Inyección en Compilación)

En lugar de ralentizar el juego mientras el usuario juega, EverMod conecta el módulo de la versión correspondiente al momento de generar el archivo `.jar` final mediante Gradle:

```text
                        ┌─────────────────────────────────┐
                        │     /common (Tu Código Base)    │
                        └────────────────┬────────────────┘
                                         │
                  ┌──────────────────────┴──────────────────────┐
                  ▼                                             ▼
   ┌─────────────────────────────┐               ┌─────────────────────────────┐
   │   evermod-1.20.1 (Modulo)   │               │   evermod-1.21.1 (Modulo)   │
   └──────────────┬──────────────┘               └──────────────┬──────────────┘
                  │                                             │
                  ▼                                             ▼
   ┌─────────────────────────────┐               ┌─────────────────────────────┐
   │  MiMod-1.20.1-v1.0.0.jar    │               │  MiMod-1.21.1-v1.0.0.jar    │
   └─────────────────────────────┘               └─────────────────────────────┘
```

---

## 🗺️ El Ecosistema EverMod

Para mantener todo organizado, el proyecto se divide en componentes especializados:

- 🛠️ **[EverMod Framework](https://github.com/wipodev/EverMod):** (Este repositorio) Los módulos de abstracción y lógica base para Minecraft.
- 💻 **[EverMod CLI](https://evermod.wipodev.com/download):** La herramienta de consola para crear y gestionar proyectos en segundos.
- 🌐 **[EverMod Locales](https://github.com/wipodev/evermod-locales):** El repositorio comunitario para agregar y mejorar traducciones del ecosistema.
- 📖 **[Sitio Web & Docs](https://evermod.wipodev.com):** Guías paso a paso, tutoriales de programación, zona de descargas y blog.

---

## 🚀 Inicio Rápido

La forma más rápida de comenzar a crear un mod con EverMod es utilizando **EverMod CLI**:

1. Descarga e instala **EverMod CLI** desde la [Zona de Descargas](https://evermod.wipodev.com/download).
2. Abre tu terminal y ejecuta:

```bash
evermod create
```

3. Sigue el asistente interactivo para generar tu entorno de trabajo listo para programar.

> 📘 Para configuraciones avanzadas o integración manual en proyectos existentes, consulta la [Documentación Oficial](https://evermod.wipodev.com/docs).

---

## 🧩 Matriz de Compatibilidad

| Versión de Minecraft | Plataforma | Versión de Java Requerida | Módulo Interno   |
| -------------------- | ---------- | ------------------------- | ---------------- |
| **1.19.2**           | Forge      | Java 17                   | `evermod-1.19.2` |
| **1.20.1**           | Forge      | Java 17                   | `evermod-1.20.1` |
| **1.21**             | Forge      | Java 21                   | `evermod-1.21`   |
| **1.21.1**           | Forge      | Java 21                   | `evermod-1.21.1` |

---

## 📜 Licencia

Este proyecto está bajo la licencia **GNU Lesser General Public License v3.0 (LGPLv3)** — creada y mantenida por **Wipodev**.

- **Puedes:** Usar EverMod Framework como dependencia para crear cualquier mod de Minecraft (libre o comercial).
- **Debes:** Si modificas o mejoras el código interno del framework EverMod, esas modificaciones deben ser publicadas bajo esta misma licencia (LGPLv3).

Para más detalles, consulta el archivo [LICENSE](./LICENSE).
