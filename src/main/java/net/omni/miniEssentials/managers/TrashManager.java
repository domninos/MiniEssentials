package net.omni.miniEssentials.managers;

import net.omni.miniEssentials.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrashManager {
    private final Map<UUID, Inventory> trashInventories = new HashMap<>();

    public void openTrashInventory(Player player) {
        UUID uuid = player.getUniqueId();

        Inventory trashInventory = trashInventories.computeIfAbsent(uuid, (_) -> loadTrashInventory());

        player.openInventory(trashInventory);
    }

    public Inventory loadTrashInventory() {
        // TODO size, name

        TrashInventoryHolder holder = new TrashInventoryHolder();

        Inventory trashInventory = Bukkit.createInventory(holder, 54, MessageUtil.parse("Trash Manager"));

        // TODO mesages.yml
        holder.setTrashInventory(trashInventory);

        return trashInventory;
    }

    public boolean isTrashInventory(Inventory inventory) {
        return inventory != null && inventory.getHolder() instanceof TrashInventoryHolder;
    }

    public boolean hasTrash(UUID uuid) {
        return trashInventories.containsKey(uuid);
    }

    public void remove(UUID uuid) {
        Inventory inv = trashInventories.remove(uuid);

        if (inv != null)
            inv.clear();
    }

    public void flush() {
        if (!trashInventories.isEmpty()) {
            trashInventories.values().forEach(Inventory::clear);
            trashInventories.clear();
        }
    }

    private static class TrashInventoryHolder implements InventoryHolder {
        private Inventory inventory;

        public void setTrashInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public @NonNull Inventory getInventory() {
            return this.inventory;
        }
    }
}
