package fr.flowsqy.configdeathmessage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.flowsqy.configdeathmessage.config.ConfigLoader;
import fr.flowsqy.configdeathmessage.config.MessageRegistryLoader;

public class ConfigDeathMessagePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final var configLoader = new ConfigLoader();
        final var dataFolder = getDataFolder();
        final var logger = getLogger();
        if (!configLoader.checkDataFolder(dataFolder)) {
            Bukkit.getPluginManager().disablePlugin(this);
            logger.warning("Can't write in the plugin directory. Disable the plugin");
            return;
        }
        final var messageRegistryLoader = new MessageRegistryLoader();
        messageRegistryLoader.load(configLoader, this, "messages.lang");
    }

}
