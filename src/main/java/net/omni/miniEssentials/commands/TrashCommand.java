package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrashCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public TrashCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, "<red>Only players can use this command.</red>");
            return true;
        }

        if (!player.hasPermission("miniessentials.trash")) {
            plugin.sendMessage(player, "<red>You do not have permission to use this command.</red>");
            return true;
        }

        plugin.getTrashManager().openTrashInventory(player);
        return true;
    }

    public void register() {
        PluginCommand trashCommand = plugin.getCommand("trash");
        if (trashCommand == null) {
            plugin.getLogger().warning("/trash command not found!");
            return;
        }

        trashCommand.setExecutor(this);
    }
}
