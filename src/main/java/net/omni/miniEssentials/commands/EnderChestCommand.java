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

public class EnderChestCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public EnderChestCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, Messages.ONLY_PLAYERS.toString());
            return true;
        }

        if (!(player.hasPermission("miniessentials.echest"))) {
            plugin.sendMessage(player, Messages.NO_PERMS.toString());
            return true;
        }

        if (args.length == 0) {
            player.openInventory(player.getEnderChest());
        } else if (args.length == 1) {
            if (!(player.hasPermission("miniessentials.echest.other"))) {
                plugin.sendMessage(player, Messages.NO_PERMS.toString());
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null || !target.hasPlayedBefore() || !target.isOnline()) {
                plugin.sendMessage(sender, Messages.PLAYER_NOT_ONLINE.replace("player", args[0]));
                return true;
            }

            player.openInventory(target.getEnderChest());
        } else {
            plugin.sendMessage(sender, Messages.USAGE.replace("usage", "/enderchest [player]"));
            return true;
        }

        return true;
    }

    public void register() {
        PluginCommand enderChestCommand = plugin.getCommand("enderchest");

        if (enderChestCommand == null) {
            plugin.getLogger().warning("/enderchest command not found!");
            return;
        }

        enderChestCommand.setExecutor(this);
    }
}
