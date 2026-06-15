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

public class GodCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public GodCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player && !player.hasPermission("miniessentials.god")) {
            plugin.sendMessage(player, Messages.NO_PERMS.toString());
            return true;
        }

        if (args.length == 0) {
            if (sender instanceof Player player)
                plugin.getGodManager().toggleGod(player);
            else
                plugin.sendMessage(sender, Messages.ONLY_PLAYERS.toString());

            return true;
        } else if (args.length == 1) {
            if (sender instanceof Player player && !player.hasPermission("miniessentials.god.other")) {
                plugin.sendMessage(player, Messages.NO_PERMS.toString());
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null || !target.hasPlayedBefore() || !target.isOnline()) {
                plugin.sendMessage(sender, Messages.PLAYER_NOT_ONLINE.replace("player", args[0]));
                return true;
            }

            plugin.getGodManager().toggleGod(target);
            plugin.sendMessage(sender, Messages.GOD_TOGGLED.replace("player", target.getName()));
        } else
            plugin.sendMessage(sender, Messages.UNKNOWN_COMMAND.toString());

        return true;
    }

    public void register() {
        PluginCommand godCommand = plugin.getCommand("god");
        if (godCommand == null) {
            plugin.getLogger().warning("/god command not found!");
            return;
        }

        godCommand.setExecutor(this);
    }
}
