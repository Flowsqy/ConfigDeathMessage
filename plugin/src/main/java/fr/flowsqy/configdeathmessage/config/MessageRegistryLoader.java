package fr.flowsqy.configdeathmessage.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class MessageRegistryLoader {

    private File file;

    public void load(@NotNull ConfigLoader configLoader, @NotNull Plugin plugin, @NotNull String fileName) {
        file = configLoader.initFile(plugin.getDataFolder(),
                Objects.requireNonNull(plugin.getResource(fileName)), fileName);
    }

    @NotNull
    public Map<String, List<BaseComponent>> loadMessages(@NotNull Logger logger) {
        final List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Can not read the message registry", e);
            return Collections.emptyMap();
        }
        final Map<String, List<BaseComponent>> registry = new HashMap<>();
        for (String line : lines) {
            line = line.trim();
            if (line.length() == 0 || line.charAt(0) == '#') {
                continue;
            }
            int assignationIndex = line.indexOf('=');
            if (assignationIndex < 1) {
                logger.warning("Invalid line in message registry : '" + line + "'");
                continue;
            }
            final String id = line.substring(0, assignationIndex);
            final boolean isJson = assignationIndex + 1 < line.length() && line.charAt(assignationIndex + 1) == 'j';
            if (isJson) {
                assignationIndex++;
            }
            final BaseComponent message;
            if (++assignationIndex == line.length()) {
                message = new TextComponent();
            } else {
                final String rawMessage = line.substring(assignationIndex);
                if (isJson) {
                    try {
                        message = ComponentSerializer.deserialize(rawMessage);
                    } catch (Exception e) {
                        logger.warning("Invalid json value for the id '" + id + "'");
                        continue;
                    }
                } else {
                    message = TextComponent.fromLegacy(ChatColor.translateAlternateColorCodes('&', rawMessage));
                }
            }
            final List<BaseComponent> messages = registry.computeIfAbsent(id, s -> new LinkedList<>());
            messages.add(message);
        }
        return registry;
    }

}
