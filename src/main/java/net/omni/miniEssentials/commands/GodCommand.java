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

public class GodCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public GodCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player && !player.hasPermission("miniessentials.god")) {
            plugin.sendMessage(player, "<red>You do not have permission to use this command.</red>");
            return true;
        }

        if (args.length == 0) {
            if (sender instanceof Player player)
                plugin.getGodManager().toggleGod(player);
            else
                plugin.sendMessage(sender, "<red>Only players can use this command.</red>");

            return true;
        } else if (args.length == 1) {
            if (sender instanceof Player player && !player.hasPermission("miniessentials.god.other")) {
                plugin.sendMessage(player, "<red>You do not have permission to use this command.</red>");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null || target.getPlayer() == null || !target.hasPlayedBefore() || !target.isOnline()) {
                plugin.sendMessage(sender, "<red>Player " + args[0] + " is not online.</red>");
                return true;
            }

            plugin.getGodManager().toggleGod(target.getPlayer());
            plugin.sendMessage(sender, "<green>Successfully toggled GOD for " + target.getName() + ".</green>");
        } else
            plugin.sendMessage(sender, "<red>Unknown command.</red>");

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
