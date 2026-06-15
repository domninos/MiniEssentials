package net.omni.miniEssentials.managers;

import net.kyori.adventure.text.Component;
import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.MessageUtil;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryEditManager {
    private final Map<UUID, UUID> activeViewers = new HashMap<>();
    private final Map<UUID, Long> errorCooldowns = new HashMap<>();

    private final MiniEssentials plugin;

    public InventoryEditManager(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    public void openInv(Player opener, Player target) {
        if (opener == null || target == null)
            return;

        // TODO config title
        Component title = Component.text("Viewing: " + target.getName() + "'s Inventory");
        Inventory inv = Bukkit.createInventory(null, 54, title);

        fillInventory(inv, target.getInventory());

        // armor
        inv.setItem(45, target.getInventory().getHelmet());
        inv.setItem(46, target.getInventory().getChestplate());
        inv.setItem(47, target.getInventory().getLeggings());
        inv.setItem(48, target.getInventory().getBoots());
        inv.setItem(49, target.getInventory().getItemInOffHand());

        // TODO config title unused slots / panes
        ItemStack lockedPane = createNamedPane(Material.RED_STAINED_GLASS_PANE, "<red>Unused Slot</red>");
        for (int i = 50; i < 54; i++)
            inv.setItem(i, lockedPane);

        activeViewers.put(opener.getUniqueId(), target.getUniqueId());
        opener.openInventory(inv);
    }


    public void fillInventory(Inventory inv, Inventory targetInv) {
        for (int i = 0; i < 36; i++)
            inv.setItem(i, targetInv.getItem(i));

        // TODO config title unused slots / panes
        inv.setItem(36, createNamedPane(Material.GRAY_STAINED_GLASS_PANE, "<yellow>↓ Helmet Slot ↓</yellow>"));
        inv.setItem(37, createNamedPane(Material.GRAY_STAINED_GLASS_PANE, "<yellow>↓ Chestplate Slot ↓</yellow>"));
        inv.setItem(38, createNamedPane(Material.GRAY_STAINED_GLASS_PANE, "<yellow>↓ Leggings Slot ↓</yellow>"));
        inv.setItem(39, createNamedPane(Material.GRAY_STAINED_GLASS_PANE, "<yellow>↓ Boots Slot ↓</yellow>"));
        inv.setItem(40, createNamedPane(Material.GRAY_STAINED_GLASS_PANE, "<yellow>↓ Off-Hand Slot ↓</yellow>"));

        ItemStack defaultFiller = createNamedPane(Material.GRAY_STAINED_GLASS_PANE, " ");

        for (int i = 41; i < 45; i++)
            inv.setItem(i, defaultFiller);
    }

    private ItemStack createNamedPane(Material material, String miniMessageText) {
        ItemStack pane = new ItemStack(material);

        pane.editMeta(meta ->
                meta.displayName(MessageUtil.parse(miniMessageText)));

        return pane;
    }

    public void setLastWarnedTime(Player player) {
        long currentTime = System.currentTimeMillis();

        errorCooldowns.put(player.getUniqueId(), currentTime);

        plugin.sendMessage(player, Messages.CANNOT_SHIFT_CLICK.toString());
    }

    public boolean shouldSendWarning(Player player) {
        return System.currentTimeMillis() - getLastWarnedTime(player) > 1000L;
    }

    public long getLastWarnedTime(Player player) {
        return errorCooldowns.getOrDefault(player.getUniqueId(), 0L);
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
        errorCooldowns.clear();
    }
}
