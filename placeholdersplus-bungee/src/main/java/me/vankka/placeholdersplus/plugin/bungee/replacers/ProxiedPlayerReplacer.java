package me.vankka.placeholdersplus.plugin.bungee.replacers;

import me.vankka.placeholdersplus.common.object.PlaceholderReplacer;
import me.vankka.placeholdersplus.common.object.Replacement;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class ProxiedPlayerReplacer implements PlaceholderReplacer {

    @Replacement(placeholder = "player_uuid")
    public UUID uuid(ProxiedPlayer proxiedPlayer) {
        return proxiedPlayer.getUniqueId();
    }

    @Replacement(placeholder = "player_name")
    public String name(ProxiedPlayer proxiedPlayer) {
        return proxiedPlayer.getName();
    }

    @Replacement(placeholder = "player_display_name")
    public String displayName(ProxiedPlayer proxiedPlayer) {
        return proxiedPlayer.getDisplayName();
    }
}
