package fr.flowsqy.configdeathmessage.message;

import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;

public record MessagesData(@Nullable BaseComponent[] base, @Nullable BaseComponent[] player,
        @Nullable BaseComponent[] item) {
}
