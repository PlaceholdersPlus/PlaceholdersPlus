package dev.vankka.placeholdersplus.common.plugin.command;

import net.kyori.text.Component;

public abstract class CommandSender {

    public abstract String getName();
    public abstract boolean hasPermission(String permission);
    public abstract void sendMessage(Component component);
}
