package net.omni.miniEssentials.listener;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class OpenInvListener implements Listener {

    private final MiniEssentials plugin;

    public OpenInvListener(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player opener = (Player) event.getWhoClicked();

        if (!plugin.getInventoryEditManager().isViewer(opener))
            return;

        int slot = event.getRawSlot();

        if (slot >= 36 && slot <= 44) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() == null)
            return;

        Player target = Bukkit.getPlayer(plugin.getInventoryEditManager().getViewing(opener));
        if (target == null || !target.isOnline()) {
            opener.closeInventory();
            plugin.sendMessage(opener, "<red>Player went offline.</red>");
            return;
        }

        ItemStack itemToPlace = event.getCursor();

        if (event.getClick() == ClickType.NUMBER_KEY) {
            int hotbar = event.getHotbarButton();

            if (hotbar >= 0 && hotbar <= 9)
                itemToPlace = opener.getInventory().getItem(hotbar);
        }

        if (slot >= 45 && slot <= 49 && itemToPlace != null && itemToPlace.getType() != Material.AIR) {
            if (!isValidForSlot(slot, itemToPlace.getType())) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.isShiftClick()) {
            event.setCancelled(true);
            plugin.sendMessage(opener, "<red>You cannot shift-click items.</red>");
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> this.updateInventory(slot, event.getInventory(), target));
    }

    private boolean isValidForSlot(int slot, Material type) {
        String name = type.name();

        return switch (slot) {
            case 45 ->
                    name.endsWith("_HELMET") || name.endsWith("_CAP") || type == Material.CARVED_PUMPKIN || type == Material.PLAYER_HEAD;
            case 46 -> name.endsWith("_CHESTPLATE") || name.endsWith("_TUNIC") || type == Material.ELYTRA;
            case 47 -> name.endsWith("_LEGGINGS") || name.endsWith("_PANTS");
            case 48 -> name.endsWith("_BOOTS");
            default -> true;
        };
    }

    private void updateInventory(int slot, Inventory inv, Player target) {
        if (slot >= 45 && slot <= 49) {
            if (slot == 45)
                target.getInventory().setHelmet(inv.getItem(slot));
            else if (slot == 46)
                target.getInventory().setChestplate(inv.getItem(slot));
            else if (slot == 47)
                target.getInventory().setLeggings(inv.getItem(slot));
            else if (slot == 48)
                target.getInventory().setBoots(inv.getItem(slot));
            else target.getInventory().setItemInOffHand(inv.getItem(slot));
        } else if (slot >= 0 && slot <= 35) {
            target.getInventory().setItem(slot, inv.getItem(slot));
        }

        target.updateInventory();
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player opener = (Player) event.getWhoClicked();

        if (!plugin.getInventoryEditManager().isViewer(opener))
            return;

        Player target = Bukkit.getPlayer(plugin.getInventoryEditManager().getViewing(opener));
        if (target == null || !target.isOnline()) {
            event.setCancelled(true);
            opener.closeInventory();
            return;
        }

        for (Map.Entry<Integer, ItemStack> entry : event.getNewItems().entrySet()) {
            int slot = entry.getKey();
            ItemStack item = entry.getValue();

            if (slot >= 36 && slot <= 44) {
                event.setCancelled(true);
                return;
            }

            if (slot >= 45 && slot <= 49 && item != null && item.getType() != Material.AIR) {
                if (!isValidForSlot(slot, item.getType())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            Inventory inv = event.getInventory();

            for (int slot : event.getNewItems().keySet())
                updateInventory(slot, inv, target);
        });
    }

    // when target changes something to their inventory or picks up an item

    @EventHandler
    public void onTargetInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        updateViewerInv(player, true);
    }

    private void updateViewerInv(Player player, boolean updateArmor) {
        if (!plugin.getInventoryEditManager().isBeingViewed(player))
            return;

        Player viewer = plugin.getInventoryEditManager().getViewerOf(player);

        if (viewer == null || !viewer.isOnline())
            return;

        if (!plugin.getInventoryEditManager().isViewer(viewer))
            return;

        Inventory topInv = viewer.getOpenInventory().getTopInventory();

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (int i = 0; i < 36; i++)
                topInv.setItem(i, player.getInventory().getItem(i));

            if (updateArmor) {
                topInv.setItem(45, player.getInventory().getHelmet());
                topInv.setItem(46, player.getInventory().getChestplate());
                topInv.setItem(47, player.getInventory().getLeggings());
                topInv.setItem(48, player.getInventory().getBoots());
            }

            // offhand
            topInv.setItem(49, player.getInventory().getItemInOffHand());

            viewer.updateInventory();
        });
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player)
            updateViewerInv(player, false);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        updateViewerInv(event.getPlayer(), true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        plugin.getInventoryEditManager().remove((Player) event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getInventoryEditManager().remove(event.getPlayer());
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
