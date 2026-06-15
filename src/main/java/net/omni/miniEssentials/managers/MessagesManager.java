package net.omni.miniEssentials.managers;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MessagesManager {
    private final MiniEssentials plugin;

    public MessagesManager(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    public void loadMessages() {
        plugin.getMessagesConfig().reload();

        FileConfiguration config = plugin.getMessagesConfig().getConfig();

        int savedDefaults = 0;

        for (Messages message : Messages.values()) {
            if (message.getDefaultVal() instanceof List<?>) {
                if (!config.contains(message.getPath())) {
                    plugin.getMessagesConfig().setNoSave(message.getPath(), message.getDefaultVal());
                    savedDefaults++;
                }

                message.setCachedVal(plugin.getMessagesConfig().getStringList(message.getPath()));
            } else {
                if (!config.contains(message.getPath())) {
                    plugin.getMessagesConfig().setNoSave(message.getPath(), message.getDefaultVal());
                    savedDefaults++;
                }

                message.setCachedVal(plugin.getMessagesConfig().getString(message.getPath()));
            }
        }

        if (savedDefaults > 0) {
            plugin.getMessagesConfig().save();

            plugin.sendConsole("<green>Successfully loaded " + savedDefaults + " default message(s).</green>");
        }
    }

    public void flush() {
        for (Messages message : Messages.values())
            message.flush();
    }
}