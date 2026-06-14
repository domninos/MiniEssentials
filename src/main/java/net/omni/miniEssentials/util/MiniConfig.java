package net.omni.miniEssentials.util;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MiniConfig {
    private final MiniEssentials plugin;
    private final File file;
    private final String configName;
    private FileConfiguration config;

    public MiniConfig(MiniEssentials plugin, String fileName) {
        this.plugin = plugin;

        if (plugin.getDataFolder().mkdir())
            plugin.sendConsole("&aSuccessfully created .../MiniConfig");

        this.configName = fileName;

        this.file = new File(plugin.getDataFolder(), fileName);

        if (!(file.exists())) {
            try {
                plugin.saveResource(fileName, false);
                plugin.sendConsole("&aSuccessfully created " + fileName);
            } catch (Exception e) {
                plugin.getLogger().warning("Could not create " + fileName);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void set(String path, Object object) {
        getConfig().set(path, object);
        save();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.sendConsole("<red>Couldn't save " + configName + "</red>");
        }
    }

    public void setNoSave(String path, Object object) {
        getConfig().set(path, object);
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public void reload() {
        if (!file.exists()) {
            plugin.saveResource(configName, false);
            plugin.sendConsole("&aSuccessfully created " + configName);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }
}
