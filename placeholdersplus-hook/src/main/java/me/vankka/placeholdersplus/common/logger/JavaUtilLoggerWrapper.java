package me.vankka.placeholdersplus.common.logger;

import java.util.logging.Level;

public class JavaUtilLoggerWrapper extends Logger {

    private final java.util.logging.Logger logger;

    public JavaUtilLoggerWrapper(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(header + message);
    }

    @Override
    public void warning(String message) {
        logger.warning(header + message);
    }

    @Override
    public void error(String message) {
        logger.severe(header + message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, throwable, () -> header + message);
    }
}
