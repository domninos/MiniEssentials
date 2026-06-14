package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenInvCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public OpenInvCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, "<red>Only players can use this command!</red>");
            return true;
        }

        if (!(player.hasPermission("miniessentials.openinv"))) {
            plugin.sendMessage(player, "<red>You do not have permission to use this command.</red>");
            return true;
        }

        if (args.length != 1) {
            plugin.sendMessage(player, "<red>Invalid arguments. Usage: /openinv <player></red>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (target.getPlayer() == null || !target.hasPlayedBefore()) {
            plugin.sendMessage(sender, "<red>Player " + args[0] + " is not online.</red>");
            return true;
        }

        player.openInventory(target.getPlayer().getInventory());

        // TODO handle inventory changing
        // TODO TEST


        return true;
    }

    public void register() {
        PluginCommand openInvCommand = plugin.getCommand("openinv");
        if (openInvCommand == null) {
            plugin.getLogger().warning("/openinv command not found!");
            return;
        }

        openInvCommand.setExecutor(this);
    }
}
