package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.Bukkit;
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
            plugin.sendMessage(sender, Messages.ONLY_PLAYERS.toString());
            return true;
        }

        if (!(player.hasPermission("miniessentials.tpa"))) {
            plugin.sendMessage(player, Messages.NO_PERMS.toString());
            return true;
        }

        if (args.length == 0) {
            plugin.sendMessage(sender, Messages.USAGE.replace("usage", "/tpa <player>"));
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("accept")) {
                UUID latest = plugin.getTPAManager().getLatestTPA(player.getUniqueId());

                if (latest == null || Bukkit.getPlayer(latest) == null) {
                    plugin.sendMessage(player, Messages.TPA_NONE.toString());
                    return true;
                }

                plugin.getTPAManager().acceptTPA(player.getUniqueId(), latest);
            } else if (args[0].equalsIgnoreCase("deny")) {
                UUID latest = plugin.getTPAManager().getLatestTPA(player.getUniqueId());

                if (latest == null || Bukkit.getPlayer(latest) == null) {
                    plugin.sendMessage(player, Messages.TPA_NONE.toString());
                    return true;
                }

                plugin.getTPAManager().denyTPA(player.getUniqueId(), latest);
            } else {
                Player target = Bukkit.getPlayerExact(args[0]);

                if (target == null || target.getPlayer() == null || !target.hasPlayedBefore() || !target.isOnline()) {
                    plugin.sendMessage(sender, Messages.PLAYER_NOT_ONLINE.replace("player", args[0]));
                    return true;
                }

                plugin.getTPAManager().submitTPA(player.getUniqueId(), target.getUniqueId());
                return true;
            }

            plugin.sendMessage(player, Messages.UNKNOWN_COMMAND.toString());
            return true;
        } else if (args.length == 2) {
            // /tpa accept|deny <player>
            if (!(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny"))) {
                plugin.sendMessage(player, Messages.UNKNOWN_COMMAND.toString());
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[1]);

            if (target == null || !target.hasPlayedBefore() || !target.isOnline()) {
                plugin.sendMessage(sender, Messages.PLAYER_NOT_ONLINE.replace("player", args[1]));
                return true;
            }

            if (args[0].equalsIgnoreCase("accept"))
                plugin.getTPAManager().acceptTPA(target.getUniqueId(), player.getUniqueId());
            else if (args[0].equalsIgnoreCase("deny"))
                plugin.getTPAManager().denyTPA(target.getUniqueId(), player.getUniqueId());

            return true;
        } else {
            plugin.sendMessage(sender, Messages.UNKNOWN_COMMAND.toString());
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
