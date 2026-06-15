package net.omni.miniEssentials.listener;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerListener implements Listener {
    private final MiniEssentials plugin;

    public PlayerListener(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (plugin.getGodManager().isGod(player))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLoseHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (plugin.getGodManager().isGod(player))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        // cancel tpa

        if (plugin.getTPAManager().hasTPA(uuid))
            plugin.getTPAManager().removeRequests(uuid);

        // has trash inv

        if (plugin.getTrashManager().hasTrash(uuid))
            plugin.getTrashManager().remove(uuid);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();

        if (!plugin.getTrashManager().isTrashInventory(inv))
            return;

        Player player = (Player) event.getPlayer();

        int amount = 0;

        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType() == Material.AIR)
                continue;

            amount += item.getAmount();
        }

        // ignore
        if (amount == 0)
            return;

        plugin.sendMessage(player, Messages.TRASHED_SUCCESS.replace("amount", String.valueOf(amount)));

        // TODO playSound - config
        player.playSound(player, Sound.BLOCK_BAMBOO_WOOD_BREAK, 0.5f, 1f);

        inv.clear();
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
