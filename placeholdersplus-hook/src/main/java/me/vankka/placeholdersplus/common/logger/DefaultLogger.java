package me.vankka.placeholdersplus.common.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultLogger extends Logger {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void info(String message) {
        log("INFO", message);
    }

    @Override
    public void warning(String message) {
        log("WARNING", message);
    }

    @Override
    public void error(String message) {
        log("ERROR", message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log("ERROR", message);
        throwable.printStackTrace();
    }

    private void log(String level, String message) {
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                + " " + level + "]: " + header + message);
    }
}
