package me.vankka.placeholdersplus.common.object;

import net.kyori.text.TextComponent;

import java.util.UUID;

public abstract class CommandSender {

    public abstract String getName();
    public abstract UUID getUniqueId();
    public abstract boolean hasPermission(String permission);
    public abstract void sendMessage(TextComponent textComponent);
}
