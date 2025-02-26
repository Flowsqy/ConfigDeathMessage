package fr.flowsqy.configdeathmessage.message;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;

public record MessagesData(@Nullable BaseComponent[] base, @Nullable BaseComponent[] player,
        @Nullable BaseComponent[] item, @NotNull Map<String, BaseComponent[]> specifics) {
}
