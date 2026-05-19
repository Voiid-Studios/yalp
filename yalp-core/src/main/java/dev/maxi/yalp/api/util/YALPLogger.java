package dev.maxi.yalp.api.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class YALPLogger {
    private final Logger logger;
    private boolean debugEnabled;

    public YALPLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String message) {
        logger.info("[YALP] " + message);
    }

    public void warn(String message) {
        logger.warning("[YALP] " + message);
    }

    public void error(String message) {
        logger.severe("[YALP] " + message);
    }

    public void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, "[YALP] " + message, throwable);
    }

    public void debug(String message) {
        if (debugEnabled) {
            logger.info("[YALP] [debug] " + message);
        }
    }

    public void component(String componentName, String message) {
        info("[" + componentName + "] " + message);
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
