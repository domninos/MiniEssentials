package net.omni.miniEssentials;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.omni.miniEssentials.commands.*;
import net.omni.miniEssentials.listener.PlayerListener;
import net.omni.miniEssentials.managers.GodManager;
import net.omni.miniEssentials.managers.TPAManager;
import net.omni.miniEssentials.managers.TrashManager;
import net.omni.miniEssentials.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniEssentials extends JavaPlugin {

    private GodManager godManager;
    private TPAManager tpaManager;
    private TrashManager trashManager;

    @Override
    public void onDisable() {
        godManager.flush();
        tpaManager.flush();
        trashManager.flush();

        sendConsole("<red>MiniEssentials is now disabled.</red>");
    }

    @Override
    public void onEnable() {
        this.godManager = new GodManager();
        this.tpaManager = new TPAManager(this);
        this.trashManager = new TrashManager();

        trashManager.loadTrashInventory();

        registerCommands();

        registerListeners();

        sendConsole("<green>Successfully enabled MiniEssentials-v" + getDescription().getVersion() + "</green>");
    }

    private void registerCommands() {
        new EnderChestCommand(this).register();
        new FixCommand(this).register();
        new GameModeCommand(this).register();
        new GodCommand(this).register();
        new OpenInvCommand(this).register();
        new TPACommand(this).register();
        new TrashCommand(this).register();
    }

    private void registerListeners() {
        new PlayerListener(this).register();
    }

    public void sendConsole(String message) {
        sendMessage(Bukkit.getConsoleSender(), message);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(MessageUtil.color(message));
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
}
