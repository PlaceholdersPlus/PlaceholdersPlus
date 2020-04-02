package dev.vankka.placeholdersplus.common.plugin.logger;

public abstract class Logger {

    protected String header = "[PlaceholdersPlus] ";

    public abstract void info(String message);
    public abstract void warning(String message);
    public abstract void error(String message);
    public abstract void error(String message, Throwable throwable);

    public void setHeader(String header) {
        this.header = header;
    }
}
