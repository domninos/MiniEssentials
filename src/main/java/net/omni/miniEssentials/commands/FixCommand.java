package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
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
            plugin.sendMessage(sender, "<red>Only players can use this command.</red>");
            return true;
        }

        if (!(player.hasPermission("miniessentials.fix"))) {
            plugin.sendMessage(player, "<red>You do not have permission to use this command.</red>");
            return true;
        }

        if (args.length != 0) {
            plugin.sendMessage(player, "<red>Unknown command.</red>");
            return true;
        }

        ItemStack currentHand = player.getInventory().getItemInMainHand();

        if (currentHand.getType() == Material.AIR || currentHand.getItemMeta() instanceof Damageable) {
            plugin.sendMessage(player, "<red>Could not fix item. Please try a different item.</red>");
            return true;
        }

        Damageable damageable = (Damageable) currentHand.getItemMeta();

        damageable.setDamage(0);

        currentHand.setItemMeta(damageable);

        player.updateInventory();

        player.playSound(player, Sound.BLOCK_ANVIL_USE, 0.4f, 1f);

        plugin.sendMessage(player, "<green>Successfully fixed your item!</green>");
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
