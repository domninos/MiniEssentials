package net.omni.miniEssentials.util;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {

    private final MiniEssentials plugin;

    private int tpaCooldown;
    private String open_inv_title;

    private String unused_pane_material;
    private String unused_pane_display_name;

    private String armor_pane_material;
    private String armor_pane_display_name;

    private int trash_inv_size;
    private String trash_inv_title;

    public ConfigUtil(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();

        // refresh trash
        plugin.getTrashManager().flush();
    }

    public void loadConfig() {
        loadSounds();

        FileConfiguration config = plugin.getConfig();

        int savedDefaults = 0;

        this.tpaCooldown = config.getInt("tpa.cooldown");
        if (tpaCooldown == 0) {
            this.tpaCooldown = 30;

            config.set("tpa.cooldown", tpaCooldown);
            savedDefaults++;
        }

        this.open_inv_title = config.getString("open_inv.title");
        if (open_inv_title == null) {
            this.open_inv_title = "Viewing: %player%'s Inventory";

            config.set("open_inv.title", open_inv_title);
            savedDefaults++;
        }

        this.unused_pane_material = config.getString("open_inv.unused_pane.material");
        if (unused_pane_material == null) {
            this.unused_pane_material = "RED_STAINED_GLASS_PANE";

            config.set("open_inv.unused_pane.material", unused_pane_material);
            savedDefaults++;
        }

        this.unused_pane_display_name = config.getString("open_inv.unused_pane.display_name");
        if (unused_pane_display_name == null) {
            this.unused_pane_display_name = "<red>Unused Slot</red>";

            config.set("open_inv.unused_pane.display_name", unused_pane_display_name);
            savedDefaults++;
        }

        this.armor_pane_material = config.getString("open_inv.armor_pane.material");
        if (armor_pane_material == null) {
            this.armor_pane_material = "GRAY_STAINED_GLASS_PANE";

            config.set("open_inv.armor_pane.material", armor_pane_material);
            savedDefaults++;
        }

        this.armor_pane_display_name = config.getString("open_inv.armor_pane.display_name");
        if (armor_pane_display_name == null) {
            this.armor_pane_display_name = "<yellow>↓ %armor% Slot ↓</yellow>";

            config.set("open_inv.armor_pane.display_name", armor_pane_display_name);
            savedDefaults++;
        }

        this.trash_inv_size = config.getInt("trash_inv.size");
        if (trash_inv_size == 0 || this.trash_inv_size < 9 || this.trash_inv_size > 54) {
            this.trash_inv_size = 27;

            plugin.sendConsole("<yellow>Warning! Trash Inventory size is out of bounds. Only multiples of 9 and less than 54 can be set.</yellow>");

            config.set("trash_inv.size", trash_inv_size);
            savedDefaults++;
        }

        this.trash_inv_title = config.getString("trash_inv.title");
        if (trash_inv_title == null) {
            this.trash_inv_title = "<green>Trash Manager</green>";

            config.set("trash_inv.title", trash_inv_title);
            savedDefaults++;
        }

        if (savedDefaults > 0) {
            plugin.saveConfig();

            plugin.sendConsole("<green>Successfully loaded " + savedDefaults + " default configuration(s).</green>");
        }
    }

    public void loadSounds() {
        FileConfiguration config = plugin.getConfig();

        int savedDefaults = 0;

        for (Sounds sound : Sounds.values()) {
            if (!config.contains(sound.getPath() + ".key")) {
                config.set(sound.getPath() + ".key", sound.getDefaultVal());
                config.set(sound.getPath() + ".volume", 0.4f);
                config.set(sound.getPath() + ".pitch", 1.0f);

                savedDefaults++;
            }

            sound.loadSound(config, plugin.getLogger());
        }

        if (savedDefaults > 0) {
            plugin.saveConfig();

            plugin.sendConsole("<green>Successfully loaded " + savedDefaults + " default sound(s).</green>");
        }
    }

    public int getTPACooldown() {
        return tpaCooldown;
    }

    public String getOpenInvTitle(String playerName) {
        return open_inv_title.replace("%player%", playerName);
    }

    public Material getUnusedPaneMaterial() {
        return Material.matchMaterial(this.unused_pane_material);
    }

    public Material getArmorPaneMaterial() {
        return Material.matchMaterial(this.armor_pane_material);
    }

    public String getUnusedPaneDisplayName() {
        return this.unused_pane_display_name;
    }

    public String getArmorPaneDisplayName(String armor) {
        return this.armor_pane_display_name.replace("%armor%", armor);
    }

    public int getTrashInvSize() {
        return trash_inv_size;
    }

    public String getTrashInvTitle() {
        return trash_inv_title;
    }
}
