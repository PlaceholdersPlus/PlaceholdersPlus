package me.vankka.placeholdersplus.plugin.velocity.replacers;

import com.velocitypowered.api.proxy.Player;
import me.vankka.placeholdersplus.common.model.PlaceholderReplacer;
import me.vankka.placeholdersplus.common.model.Replacement;

import java.util.UUID;

public class PlayerReplacer implements PlaceholderReplacer {

    @Replacement(placeholder = "player_uuid")
    public UUID uuid(Player player) {
        return player.getUniqueId();
    }

    @Replacement(placeholder = "player_name")
    public String name(Player player) {
        return player.getUsername();
    }
}
