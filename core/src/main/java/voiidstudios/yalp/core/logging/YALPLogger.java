package voiidstudios.yalp.core.logging;

public interface YALPLogger {
    void info(String message);

    void success(String message);

    void warning(String message);

    void warn(String message);

    void error(String message);

    void failure(String message);

    void process(String message);

    void error(String message, Throwable throwable);

    void exception(String message, Throwable throwable);

    void debug(String message);

    void component(String componentName, String message);

    void log(LogLevel level, String message);

    boolean isDebugEnabled();

    void setDebugEnabled(boolean debugEnabled);
}
