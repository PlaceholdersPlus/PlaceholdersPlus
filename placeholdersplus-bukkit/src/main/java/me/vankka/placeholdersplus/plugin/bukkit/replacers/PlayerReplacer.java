package me.vankka.placeholdersplus.plugin.bukkit.replacers;

import me.vankka.placeholdersplus.common.model.PlaceholderReplacer;
import me.vankka.placeholdersplus.common.model.Replacement;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerReplacer implements PlaceholderReplacer {

    @Replacement(placeholder = "player_uuid")
    public UUID uuid(OfflinePlayer offlinePlayer) {
        return offlinePlayer.getUniqueId();
    }

    @Replacement(placeholder = "player_name")
    public String name(OfflinePlayer offlinePlayer) {
        return offlinePlayer.getName();
    }

    @Replacement(placeholder = "player_display_name")
    public String displayName(Player player) {
        return player.getDisplayName();
    }
}
