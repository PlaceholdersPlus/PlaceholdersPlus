package me.vankka.placeholdersplus.common.logger;

public class DefaultLogger extends Logger {

    @Override
    public void info(String message) {
        log(message);
    }

    @Override
    public void warning(String message) {
        log(message);
    }

    @Override
    public void error(String message) {
        log(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log(message);
        throwable.printStackTrace();
    }

    private void log(String message) {
        System.out.println(header + message);
    }
}
