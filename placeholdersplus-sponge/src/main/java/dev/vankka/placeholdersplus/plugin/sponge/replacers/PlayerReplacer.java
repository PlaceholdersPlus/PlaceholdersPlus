package dev.vankka.placeholdersplus.plugin.sponge.replacers;

import dev.vankka.placeholdersplus.common.plugin.placeholder.PlaceholderReplacer;
import dev.vankka.placeholdersplus.common.plugin.placeholder.Replacement;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class PlayerReplacer implements PlaceholderReplacer {

    @Replacement(placeholder = "player_uuid")
    public UUID uuid(Player player) {
        return player.getUniqueId();
    }

    @Replacement(placeholder = "player_name")
    public String name(Player player) {
        return player.getName();
    }
}
