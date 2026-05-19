package dev.maxi.yalp.api.logger;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import dev.maxi.yalp.api.util.YALPLogger;

public final class LoggerComponent implements YALPComponent {
    private final YALPLogger logger;
    private boolean debugEnabled;

    public LoggerComponent(YALPLogger logger) {
        this.logger = logger;
    }

    @Override
    public String getId() {
        return "logger";
    }

    @Override
    public void onLoad(ComponentContext context) {
        this.debugEnabled = context.getPlugin().getConfig().getBoolean("debug", false);
        logger.setDebugEnabled(debugEnabled);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void success(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void component(String componentName, String message) {
        logger.info("[" + componentName + "] " + message);
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        logger.setDebugEnabled(debugEnabled);
    }
}
