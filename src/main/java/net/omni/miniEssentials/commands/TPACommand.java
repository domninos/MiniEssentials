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

import java.util.UUID;

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
                UUID latest = plugin.getTPAManager().getLatestTPA(player.getUniqueId());

                if (latest == null || Bukkit.getPlayer(latest) == null) {
                    plugin.sendMessage(player, "<red>No TPA found.</red>");
                    return true;
                }

                plugin.getTPAManager().acceptTPA(player.getUniqueId(), latest);
            } else if (args[0].equalsIgnoreCase("deny")) {
                UUID latest = plugin.getTPAManager().getLatestTPA(player.getUniqueId());

                if (latest == null || Bukkit.getPlayer(latest) == null) {
                    plugin.sendMessage(player, "<red>No TPA found.</red>");
                    return true;
                }

                plugin.getTPAManager().denyTPA(player.getUniqueId(), latest);
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if (target.getPlayer() == null || !target.hasPlayedBefore()) {
                    plugin.sendMessage(sender, "<red>Player " + args[0] + " is not online.</red>");
                    return true;
                }

                plugin.getTPAManager().submitTPA(player.getUniqueId(), target.getUniqueId());
                return true;
            }

            plugin.sendMessage(player, "<red>Unknown command.</red>");
            return true;
        } else if (args.length == 2) {
            // /tpa accept|deny <player>
            if (!(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny"))) {
                plugin.sendMessage(player, "<red>Unknown command.</red>");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (target.getPlayer() == null || !target.hasPlayedBefore()) {
                plugin.sendMessage(sender, "<red>Player " + args[1] + " is not online.</red>");
                return true;
            }

            String res = "<red>Something went wrong. Please try again</red>";

            if (args[0].equalsIgnoreCase("accept"))
                res = plugin.getTPAManager().acceptTPA(player.getUniqueId(), target.getUniqueId());
            else if (args[0].equalsIgnoreCase("deny"))
                res = plugin.getTPAManager().denyTPA(player.getUniqueId(), target.getUniqueId());

            plugin.sendMessage(sender, res);

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
