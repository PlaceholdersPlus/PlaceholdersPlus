package me.vankka.placeholdersplus.plugin.sponge.replacers;

import me.vankka.placeholdersplus.common.object.PlaceholderReplacer;
import me.vankka.placeholdersplus.common.object.Replacement;
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
