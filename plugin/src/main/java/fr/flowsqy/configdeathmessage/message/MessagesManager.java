package fr.flowsqy.configdeathmessage.message;

import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessagesManager {

    public MessagesManager() {
    }

    @Nullable
    public BaseComponent getMessage(@NotNull Player victim, @NotNull DamageType damageType, @Nullable Entity killer) {
        String[] messages = null;
        if (killer != null) {
            final var killerType = killer.getType();
            if (killerType.isRegistered()) {
                if (killer instanceof Player player) {
                    final var playerInv = player.getInventory();
                    final var mainHandItem = playerInv.getItemInMainHand();
                    if (mainHandItem != null) {

                    }
                }
                if (messages == null) {
                }
            }
        }
        return new TextComponent("");
    }

}
