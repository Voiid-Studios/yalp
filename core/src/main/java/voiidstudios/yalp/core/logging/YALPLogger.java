package voiidstudios.yalp.core.logging;

public interface YALPLogger {
    void info(String message);

    void success(String message);

    void warn(String message);

    void error(String message);

    void error(String message, Throwable throwable);

    void debug(String message);

    void component(String componentName, String message);

    boolean isDebugEnabled();

    void setDebugEnabled(boolean debugEnabled);
}
