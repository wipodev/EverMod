package net.evermod.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Custom logger wrapper for EverMod that provides automatic caller context 
 * prefixing and level-based logging filters.
 * 
 * @author Wipodev
 */
public class EverLogger {

  private final Logger logger;
  private final String modId;
  private final boolean infoEnabled;
  private final boolean logEnabled;
  private final boolean debugEnabled;
  private final boolean traceEnabled;
  private final boolean warnEnabled;
  private final boolean errorEnabled;
  private final boolean fatalEnabled;

  /**
   * Constructs an EverLogger instance with custom log level toggles.
   *
   * @param modId The unique identifier of the mod using EverMod (e.g., "mymod").
   * @param info Enable or disable INFO messages.
   * @param log Enable or disable custom level LOG messages.
   * @param debug Enable or disable DEBUG messages.
   * @param trace Enable or disable TRACE messages.
   * @param warn Enable or disable WARN messages.
   * @param error Enable or disable ERROR messages.
   * @param fatal Enable or disable FATAL messages.
   */
  public EverLogger(String modId, boolean info, boolean log, boolean debug, boolean trace,
      boolean warn, boolean error, boolean fatal) {
    this.modId = modId;
    this.logger = LogManager.getLogger(modId);
    this.infoEnabled = info;
    this.logEnabled = log;
    this.debugEnabled = debug;
    this.traceEnabled = trace;
    this.warnEnabled = warn;
    this.errorEnabled = error;
    this.fatalEnabled = fatal;
  }

  /**
   * Constructs a simplified EverLogger instance with all log levels enabled by default.
   *
   * @param modId The unique identifier of the mod using EverMod.
   */
  public EverLogger(String modId) {
    this(modId, true, true, true, true, true, true, true);
  }

  // --- LOGGING METHODS ---

  /**
   * Logs an INFO message with optional formatted arguments.
   *
   * @param message The message format string.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void info(String message, Object... args) {
    if (infoEnabled) {
      logger.info(getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Logs a message at the specified Log4j level with optional formatted arguments.
   *
   * @param level The Log4j logging level.
   * @param message The message format string.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void log(Level level, String message, Object... args) {
    if (logEnabled) {
      logger.log(level, getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Logs a DEBUG message with optional formatted arguments.
   *
   * @param message The message format string.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void debug(String message, Object... args) {
    if (debugEnabled) {
      logger.debug(getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Logs a TRACE message with optional formatted arguments.
   *
   * @param message The message format string.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void trace(String message, Object... args) {
    if (traceEnabled) {
      logger.trace(getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Logs a WARN message with optional formatted arguments.
   *
   * @param message The message format string.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void warn(String message, Object... args) {
    if (warnEnabled) {
      logger.warn(getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Logs an ERROR message with optional formatted arguments.
   *
   * @param message The message format string.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void error(String message, Object... args) {
    if (errorEnabled) {
      logger.error(getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Logs an ERROR message along with an exception/throwable stack trace.
   *
   * @param message The message format string.
   * @param throwable The exception or error to log.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void error(String message, Throwable throwable, Object... args) {
    if (errorEnabled) {
      if (args.length == 0) {
        logger.error(getAutomaticPrefix() + message, throwable);
      } else {
        logger.error(getAutomaticPrefix() + message, args, throwable);
      }
    }
  }

  /**
   * Logs a FATAL message with optional formatted arguments.
   *
   * @param message The message format string.
   * @param args Arguments referenced by the format specifiers in the message string.
   */
  public void fatal(String message, Object... args) {
    if (fatalEnabled) {
      logger.fatal(getAutomaticPrefix() + message, args);
    }
  }

  /**
   * Analyzes the call stack to construct a log prefix containing the mod ID, 
   * simple caller class name, and method name.
   *
   * @return A formatted prefix string for log entries.
   */
  private String getAutomaticPrefix() {
    // Retrieve the call stack of the current thread
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

    // Index 0: Thread.getStackTrace
    // Index 1: EverLogger.getAutomaticPrefix
    // Index 2: EverLogger logging method (info, debug, error, etc.)
    // Index 3: The actual external caller class/method
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
