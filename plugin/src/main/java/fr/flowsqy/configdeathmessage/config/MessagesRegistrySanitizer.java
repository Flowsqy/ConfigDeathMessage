package fr.flowsqy.configdeathmessage.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.configdeathmessage.message.MessagesData;
import net.md_5.bungee.api.chat.BaseComponent;

public class MessagesRegistrySanitizer {

    @NotNull
    public Map<String, MessagesData> sanitize(@NotNull Logger logger,
            @NotNull Map<String, List<BaseComponent>> registry) {
        final var sanitizedRegistry = new HashMap<String, ModifiableMessagesData>();
        for (var entry : registry.entrySet()) {
            final var dotIndex = entry.getKey().indexOf('.');
            if (dotIndex == 0) {
                logger.warning("Empty key for '" + entry.getKey() + "'");
                continue;
            }
            final String key;
            final String modifier;
            if (dotIndex < 0) {
                key = entry.getKey();
                modifier = null;
            } else {
                key = entry.getKey().substring(0, dotIndex);
                modifier = dotIndex + 1 == entry.getKey().length() ? "" : entry.getKey().substring(dotIndex + 1);
            }
            final var data = sanitizedRegistry.computeIfAbsent(key, k -> new ModifiableMessagesData());
            final var messages = entry.getValue().toArray(new BaseComponent[0]);
            if (modifier != null) {
                if (modifier.equals("player")) {
                    data.player = messages;
                    continue;
                }
                if (modifier.equals("item")) {
                    data.item = messages;
                    continue;
                }
                logger.warning("Wrong modifier '" + modifier + "' for key '" + entry.getKey() + "'. Ignoring it");
            }
            data.base = messages;
        }
        final var unmodifiableSanitizedRegistry = new HashMap<String, MessagesData>(sanitizedRegistry.size());
        for (var entry : sanitizedRegistry.entrySet()) {
            unmodifiableSanitizedRegistry.put(entry.getKey(), entry.getValue().toUnmodifible());
        }
        return unmodifiableSanitizedRegistry;
    }

    private static class ModifiableMessagesData {
        private BaseComponent[] base, player, item;

        public MessagesData toUnmodifible() {
            return new MessagesData(base, player, item);
        }
    }

}
