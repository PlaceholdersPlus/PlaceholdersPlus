package dev.vankka.placeholdersplus.plugin.sponge.replacers;

import dev.vankka.placeholdersplus.common.plugin.placeholder.PlaceholderReplacer;
import dev.vankka.placeholdersplus.common.plugin.placeholder.Replacement;
import org.spongepowered.api.entity.living.player.User;

import java.util.UUID;

public class UserReplacer implements PlaceholderReplacer {

    @Replacement(placeholder = "user_uuid")
    public UUID uuid(User user) {
        return user.getUniqueId();
    }

    @Replacement(placeholder = "user_name")
    public String name(User user) {
        return user.getName();
    }
}
