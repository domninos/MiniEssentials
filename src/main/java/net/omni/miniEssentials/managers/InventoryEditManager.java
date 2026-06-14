package net.omni.miniEssentials.managers;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryEditManager {
    private final Map<UUID, UUID> activeViewers = new HashMap<>();

    public void openInv(Player opener, Player target) {
        if (opener == null || target == null)
            return;

        // TODO config title
        Component title = Component.text("Viewing: " + target.getName() + "'s Inventory");
        Inventory inv = Bukkit.createInventory(null, 54, title);

        for (int i = 0; i < 36; i++)
            inv.setItem(i, target.getInventory().getItem(i));

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(" "));
            filler.setItemMeta(meta);
        }

        for (int i = 36; i < 45; i++)
            inv.setItem(i, filler);

        // armor
        inv.setItem(45, target.getInventory().getHelmet());
        inv.setItem(46, target.getInventory().getChestplate());
        inv.setItem(47, target.getInventory().getLeggings());
        inv.setItem(48, target.getInventory().getBoots());
        inv.setItem(49, target.getInventory().getItemInOffHand());

        activeViewers.put(opener.getUniqueId(), target.getUniqueId());
        opener.openInventory(inv);
    }

    public boolean isViewer(Player player) {
        return activeViewers.containsKey(player.getUniqueId());
    }

    public Player getViewerOf(Player target) {
        if (!isBeingViewed(target)) return null;

        for (Map.Entry<UUID, UUID> entry : activeViewers.entrySet()) {
            if (entry.getValue().equals(target.getUniqueId()))
                return Bukkit.getPlayer(entry.getKey());
        }

        return null;
    }

    public boolean isBeingViewed(Player player) {
        return activeViewers.containsValue(player.getUniqueId());
    }

    public void remove(Player player) {
        activeViewers.remove(player.getUniqueId());
    }

    public UUID getViewing(Player player) {
        return activeViewers.get(player.getUniqueId());
    }

    public void flush() {
        activeViewers.clear();
    }
}
