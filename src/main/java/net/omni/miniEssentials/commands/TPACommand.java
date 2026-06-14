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

public class TPACommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public TPACommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, "<red>Only players can use this command.</red>");
            return true;
        }

        if (!(player.hasPermission("miniessentials.tpa"))) {
            plugin.sendMessage(player, "<red>You do not have permission to use this command.</red>");
            return true;
        }

        if (args.length == 0) {
            plugin.sendMessage(sender, "<red>Invalid arguments. Usage: /tpa <player></red>");
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("accept")) {
                // TODO get latest tpa request

            } else if (args[0].equalsIgnoreCase("deny")) {
                // TODO get latest tpa request

            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if (target.getPlayer() == null || !target.hasPlayedBefore()) {
                    plugin.sendMessage(sender, "<red>Player " + args[0] + " is not online.</red>");
                    return true;
                }

                // TODO send minimessage text accept or decline

                return true;
            }

            return true;
        } else if (args.length == 2) {
            // TODO get latest tpa request
            // /tpa accept|deny <player>

            return true;
        } else {
            plugin.sendMessage(sender, "<red>Unknown command.</red>");
            return true;
        }
    }

    public void register() {
        PluginCommand tpaCommand = Bukkit.getPluginCommand("tpa");
        if (tpaCommand == null) {
            plugin.getLogger().warning("/tpa command not found!");
            return;
        }

        tpaCommand.setExecutor(this);
    }
}
