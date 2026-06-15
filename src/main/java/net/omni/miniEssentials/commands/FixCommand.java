package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

public class FixCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public FixCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, Messages.ONLY_PLAYERS.toString());
            return true;
        }

        if (!(player.hasPermission("miniessentials.fix"))) {
            plugin.sendMessage(player, Messages.NO_PERMS.toString());
            return true;
        }

        if (args.length != 0) {
            plugin.sendMessage(player, Messages.UNKNOWN_COMMAND.toString());
            return true;
        }

        ItemStack currentHand = player.getInventory().getItemInMainHand();

        if (currentHand.getType() == Material.AIR || !(currentHand.getItemMeta() instanceof Damageable damageable)) {
            plugin.sendMessage(player, Messages.FIX_ERROR.toString());
            return true;
        }

        damageable.setDamage(0);

        currentHand.setItemMeta(damageable);

        player.updateInventory();

        // TODO config
        player.playSound(player, Sound.BLOCK_ANVIL_USE, 0.4f, 1f);

        plugin.sendMessage(player, Messages.FIX_SUCCESS.toString());
        return true;
    }

    public void register() {
        PluginCommand fixCommand = plugin.getCommand("fix");
        if (fixCommand == null) {
            plugin.getLogger().warning("/fix command not found!");
            return;
        }

        fixCommand.setExecutor(this);
    }
}
