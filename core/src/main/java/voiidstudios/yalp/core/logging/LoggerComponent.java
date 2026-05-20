package voiidstudios.yalp.core.logging;

import voiidstudios.yalp.core.component.YALPComponent;

public final class LoggerComponent implements YALPComponent, YALPLogger {
    private final YALPLogger delegate;

    public LoggerComponent(YALPLogger delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId() {
        return "logger";
    }

    @Override
    public void info(String message) { delegate.info(message); }

    @Override
    public void success(String message) { delegate.success(message); }

    @Override
    public void warning(String message) { delegate.warning(message); }

    @Override
    public void warn(String message) { delegate.warn(message); }

    @Override
    public void error(String message) { delegate.error(message); }

    @Override
    public void failure(String message) { delegate.failure(message); }

    @Override
    public void process(String message) { delegate.process(message); }

    @Override
    public void error(String message, Throwable throwable) { delegate.error(message, throwable); }

    @Override
    public void exception(String message, Throwable throwable) { delegate.exception(message, throwable); }

    @Override
    public void debug(String message) { delegate.debug(message); }

    @Override
    public void component(String componentName, String message) { delegate.component(componentName, message); }

    @Override
    public void log(LogLevel level, String message) { delegate.log(level, message); }

    @Override
    public boolean isDebugEnabled() { return delegate.isDebugEnabled(); }

    @Override
    public void setDebugEnabled(boolean debugEnabled) { delegate.setDebugEnabled(debugEnabled); }
}
