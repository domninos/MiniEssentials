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

public class OpenInvCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public OpenInvCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, Messages.ONLY_PLAYERS.toString());
            return true;
        }

        if (!(player.hasPermission("miniessentials.openinv"))) {
            plugin.sendMessage(player, Messages.NO_PERMS.toString());
            return true;
        }

        if (args.length != 1) {
            plugin.sendMessage(player, Messages.USAGE.replace("usage", "/openinv <player>"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null || !target.hasPlayedBefore() || !target.isOnline()) {
            plugin.sendMessage(sender, Messages.PLAYER_NOT_ONLINE.replace("player", args[0]));
            return true;
        }

        if (player.getUniqueId().equals(target.getUniqueId())) {
            plugin.sendMessage(player, Messages.CANNOT_USE_SELF.toString());
            return true;
        }

        plugin.getInventoryEditManager().openInv(player, target);
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
