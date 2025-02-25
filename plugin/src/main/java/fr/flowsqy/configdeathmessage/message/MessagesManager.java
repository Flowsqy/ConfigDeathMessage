package fr.flowsqy.configdeathmessage.message;

import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;

public class MessagesManager {

    private final Map<String, MessagesData> registry;
    private final Random random;

    public MessagesManager(@NotNull Map<String, MessagesData> registry) {
        this.registry = registry;
        random = new Random();
    }

    @Nullable
    public BaseComponent getMessage(@NotNull Player victim, @NotNull DamageType damageType, @Nullable Entity killer) {
        final var messagesData = registry.get(damageType.getKeyOrThrow().getKey());
        if (messagesData == null) {
            return null;
        }
        if (killer == null) {
            return getBaseMessage(messagesData, victim);
        }
        if (!(killer instanceof Player playerKiller)) {
            return getKillerMessage(messagesData, victim, killer);
        }
        final var playerInv = playerKiller.getInventory();
        final var mainHandItem = playerInv.getItemInMainHand();
        if (mainHandItem.getType() == Material.AIR) {
            return getPlayerMessage(messagesData, victim, playerKiller);
        }
        return getItemMessage(messagesData, victim, playerKiller, mainHandItem);
    }

    @Nullable
    private BaseComponent getBaseMessage(@NotNull MessagesData messagesData, @NotNull Player victim) {
        if (messagesData.base() == null) {
            return null;
        }
        final var message = getRandomMessage(messagesData.base());
        return replaceBasePlaceholders(message, victim);
    }

    @NotNull
    private BaseComponent replaceBasePlaceholders(@NotNull BaseComponent message, @NotNull Player victim) {
        // TODO Apply base placeholders
        return message;
    }

    @Nullable
    private BaseComponent getKillerMessage(@NotNull MessagesData messagesData, @NotNull Player victim,
            @NotNull Entity killer) {
        if (messagesData.base() == null) {
            return null;
        }
        final var message = getRandomMessage(messagesData.base());
        return replaceKillerPlaceholders(message, victim, killer);
    }

    @NotNull
    private BaseComponent replaceKillerPlaceholders(@NotNull BaseComponent message, @NotNull Player victim,
            @NotNull Entity killer) {
        // Replace entity type
        return replaceBasePlaceholders(message, victim);
    }

    @Nullable
    private BaseComponent getPlayerMessage(@NotNull MessagesData messagesData, @NotNull Player victim,
            @NotNull Player killer) {
        if (messagesData.player() == null) {
            return getKillerMessage(messagesData, victim, killer);
        }
        final var message = getRandomMessage(messagesData.player());
        return replacePlayerPlaceholders(message, victim, killer);
    }

    @NotNull
    private BaseComponent replacePlayerPlaceholders(@NotNull BaseComponent message, @NotNull Player victim,
            @NotNull Player killer) {
        // Replace player name
        return replaceBasePlaceholders(message, victim);
    }

    private BaseComponent getItemMessage(@NotNull MessagesData messagesData, @NotNull Player victim,
            @NotNull Player killer, @NotNull ItemStack itemStack) {
        if (messagesData.item() == null) {
            return getPlayerMessage(messagesData, victim, killer);
        }
        final var message = getRandomMessage(messagesData.item());
        return replaceItemPlaceholders(message, victim, killer, itemStack);
    }

    @NotNull
    private BaseComponent replaceItemPlaceholders(@NotNull BaseComponent message, @NotNull Player victim,
            @NotNull Player killer, @NotNull ItemStack itemStack) {
        // Replace item name
        return replacePlayerPlaceholders(message, victim, killer);
    }

    @NotNull
    private BaseComponent getRandomMessage(@NotNull BaseComponent[] messages) {
        if (messages.length == 1) {
            return messages[0];
        }
        return messages[random.nextInt(messages.length)];
    }

}
