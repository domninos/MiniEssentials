package net.omni.miniEssentials;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.omni.miniEssentials.commands.*;
import net.omni.miniEssentials.listener.OpenInvListener;
import net.omni.miniEssentials.listener.PlayerListener;
import net.omni.miniEssentials.managers.*;
import net.omni.miniEssentials.util.ConfigUtil;
import net.omni.miniEssentials.util.MessageUtil;
import net.omni.miniEssentials.util.MiniConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniEssentials extends JavaPlugin {

    private GodManager godManager;
    private TPAManager tpaManager;
    private TrashManager trashManager;
    private InventoryEditManager inventoryEditManager;

    private MiniConfig messagesConfig;
    private MessagesManager messagesManager;

    private ConfigUtil configUtil;

    @Override
    public void onDisable() {
        godManager.flush();
        tpaManager.flush();
        trashManager.flush();
        inventoryEditManager.flush();

        messagesManager.flush();

        sendConsole("<red>MiniEssentials is now disabled.</red>");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.configUtil = new ConfigUtil(this);
        configUtil.loadConfig();

        this.messagesConfig = new MiniConfig(this, "messages.yml");
        this.messagesManager = new MessagesManager(this);
        messagesManager.loadMessages();

        this.godManager = new GodManager(this);
        this.tpaManager = new TPAManager(this);
        this.trashManager = new TrashManager(this);
        this.inventoryEditManager = new InventoryEditManager(this);

        trashManager.loadTrashInventory();

        registerCommands();

        registerListeners();

        sendConsole("<green>Successfully enabled MiniEssentials-v" + getPluginMeta().getVersion() + "</green>");
    }

    private void registerCommands() {
        new EnderChestCommand(this).register();
        new FixCommand(this).register();
        new GameModeCommand(this).register();
        new GodCommand(this).register();
        new OpenInvCommand(this).register();
        new TPACommand(this).register();
        new TrashCommand(this).register();

        new MinieCommand(this).register();
    }

    private void registerListeners() {
        new PlayerListener(this).register();
        new OpenInvListener(this).register();
    }

    public void sendConsole(String message) {
        sendMessage(Bukkit.getConsoleSender(), message);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(MessageUtil.color(message));
    }

    public void sendMessage(CommandSender sender, String message, boolean prefix) {
        sender.sendMessage(prefix ? MessageUtil.color(message) : MessageUtil.parse(message));
    }

    public void sendConsole(String message, TagResolver... resolvers) {
        sendMessage(Bukkit.getConsoleSender(), message, resolvers);
    }

    public void sendMessage(CommandSender sender, String message, TagResolver... resolvers) {
        sender.sendMessage(MessageUtil.color(message, resolvers));
    }

    public GodManager getGodManager() {
        return godManager;
    }

    public TPAManager getTPAManager() {
        return tpaManager;
    }

    public TrashManager getTrashManager() {
        return trashManager;
    }

    public InventoryEditManager getInventoryEditManager() {
        return inventoryEditManager;
    }

    public MiniConfig getMessagesConfig() {
        return messagesConfig;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public ConfigUtil getConfigUtil() {
        return configUtil;
    }
}
