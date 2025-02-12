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

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MessageRegistryLoader {

    private File file;

    public void load(@NotNull ConfigLoader configLoader, @NotNull Plugin plugin, @NotNull String fileName) {
        file = configLoader.initFile(plugin.getDataFolder(),
                Objects.requireNonNull(plugin.getResourceAsStream(fileName)), fileName);
    }

    @NotNull
    public Map<String, List<String>> getMessages(@NotNull Logger logger) {
        final List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Can not read the message registry", e);
            return Collections.emptyMap();
        }
        final Map<String, List<String>> registry = new HashMap<>();
        for (String line : lines) {
            line = line.trim();
            if (line.length() == 0 || line.charAt(0) == '#') {
                continue;
            }
            final String[] parts = line.split("=", 2);
            if (parts.length != 2) {
                logger.warning("Invalid line in message registry : '" + line + "'");
                continue;
            }
            final String id = parts[0];
            final String rawMessage = parts[1];
            final String message;
            try {
                message = ChatColor.translateAlternateColorCodes('&', rawMessage);
            } catch (Exception e) {
                logger.warning("Invalid json value for the id '" + id + "'");
                continue;
            }
            final List<String> messages = registry.computeIfAbsent(id, s -> new LinkedList<>());
            messages.add(message);
            registry.put(id, messages);
        }
        return registry;
    }

}
