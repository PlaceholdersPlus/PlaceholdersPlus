package me.vankka.placeholdersplus.common.logger;

public class SLF4JLoggerWrapper extends Logger {

    private final org.slf4j.Logger logger;

    public SLF4JLoggerWrapper(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(header + message);
    }

    @Override
    public void warning(String message) {
        logger.warn(header + message);
    }

    @Override
    public void error(String message) {
        logger.error(header + message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(header + message, throwable);
    }
}
