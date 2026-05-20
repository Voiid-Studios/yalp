package voiidstudios.yalp.core.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleYALPLogger implements YALPLogger {
    private final Logger logger;
    private boolean debugEnabled;

    public ConsoleYALPLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info("[YALP] " + message);
    }

    @Override
    public void success(String message) {
        info(message);
    }

    @Override
    public void warning(String message) {
        warn(message);
    }

    @Override
    public void warn(String message) {
        logger.warning("[YALP] " + message);
    }

    @Override
    public void error(String message) {
        logger.severe("[YALP] " + message);
    }

    @Override
    public void failure(String message) {
        error(message);
    }

    @Override
    public void process(String message) {
        info(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, "[YALP] " + message, throwable);
    }

    @Override
    public void exception(String message, Throwable throwable) {
        error(message, throwable);
    }

    @Override
    public void debug(String message) {
        if (debugEnabled) {
            info("[debug] " + message);
        }
    }

    @Override
    public void component(String componentName, String message) {
        info("[" + componentName + "] " + message);
    }

    @Override
    public void log(LogLevel level, String message) {
        if (level == null) {
            info(message);
            return;
        }
        switch (level) {
            case SUCCESS:
                success(message);
                break;
            case WARNING:
                warning(message);
                break;
            case ERROR:
                error(message);
                break;
            case FAILURE:
                failure(message);
                break;
            case PROCESS:
            case PASSIVE:
                process(message);
                break;
            case DEBUG:
                debug(message);
                break;
            case INFO:
            default:
                info(message);
                break;
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    @Override
    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
