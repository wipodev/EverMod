package net.evermod.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EverLogger {

  private final Logger logger;
  private final String modId;
  private final boolean debugEnabled;
  private final boolean infoEnabled;
  private final boolean errorEnabled;

  /**
   * Constructor para configurar el Logger desde el Mod que usa EverMod.
   * 
   * @param modId El identificador de tu mod (ej: "mimod")
   * @param debug Enabled/Disabled para mensajes DEBUG
   * @param info Enabled/Disabled para mensajes INFO
   * @param error Enabled/Disabled para mensajes ERROR
   */
  public EverLogger(String modId, boolean debug, boolean info, boolean error) {
    this.modId = modId;
    this.logger = LogManager.getLogger(modId);
    this.debugEnabled = debug;
    this.infoEnabled = info;
    this.errorEnabled = error;
  }

  /**
   * Constructor simplificado con configuraciones por defecto (Todo activo).
   */
  public EverLogger(String modId) {
    this(modId, true, true, true);
  }

  // --- MÉTODOS DE LOGUEO ---

  public void debug(String message, Object... args) {
    if (debugEnabled) {
      logger.debug(getAutomaticPrefix() + message, args);
    }
  }

  public void info(String message, Object... args) {
    if (infoEnabled) {
      logger.info(getAutomaticPrefix() + message, args);
    }
  }

  public void error(String message, Object... args) {
    if (errorEnabled) {
      logger.error(getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Sobrecarga especial para cuando quieres registrar una excepción/error del sistema.
   */
  public void error(String message, Throwable throwable, Object... args) {
    if (errorEnabled) {
      logger.error(getAutomaticPrefix() + message, args, throwable);
    }
  }

  /**
   * Analiza el StackTrace para obtener el nombre de la clase y método que llamaron al Logger.
   */
  private String getAutomaticPrefix() {
    // Obtenemos la pila de llamadas del hilo actual
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

    // Posición 0: getStackTrace
    // Posición 1: getAutomaticPrefix
    // Posición 2: info/debug/error (este archivo)
    // Posición 3: La clase externa real que llamó a EverLogger
    if (stackTrace.length > 3) {
      StackTraceElement caller = stackTrace[3];
      String fullClassName = caller.getClassName();
      String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
      String methodName = caller.getMethodName();

      return "[" + modId + "-" + simpleClassName + "-" + methodName + "]: ";
    }

    return "[" + modId + "]: ";
  }
}
