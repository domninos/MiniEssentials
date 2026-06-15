package net.omni.miniEssentials.util;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {

    private final MiniEssentials plugin;
    private final FileConfiguration config;

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
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

    public void loadConfig() {
        loadSounds();

        int savedDefaults = 0;

        this.tpaCooldown = config.getInt("tpa.cooldown");
        if (tpaCooldown == 0) {
            config.set("tpa.cooldown", 30);
            savedDefaults++;
        }

        this.open_inv_title = config.getString("open_inv.title");
        if (open_inv_title == null) {
            config.set("open.inv.title", "Viewing: %player%'s Inventory");
            savedDefaults++;
        }

        this.unused_pane_material = config.getString("open_inv.unused_pane.material");
        if (unused_pane_material == null) {
            config.set("open_inv.unused_pane.material", "RED_STAINED_GLASS_PANE");
            savedDefaults++;
        }

        this.unused_pane_display_name = config.getString("open_inv.unused_pane.display_name");
        if (unused_pane_display_name == null) {
            config.set("open_inv.unused_pane.display_name", "<red>Unused Slot</red>");
            savedDefaults++;
        }

        this.armor_pane_material = config.getString("open_inv.armor_pane.material");
        if (armor_pane_material == null) {
            config.set("open_inv.armor_pane.material", "GRAY_STAINED_GLASS_PANE");
            savedDefaults++;
        }

        this.armor_pane_display_name = config.getString("open_inv.armor_pane.display_name");
        if (armor_pane_display_name == null) {
            config.set("open_inv.armor_pane.display_name", "<yellow>↓ %armor% Slot ↓</yellow>");
            savedDefaults++;
        }

        this.trash_inv_size = config.getInt("trash.inv.size");
        if (trash_inv_size == 0) {
            config.set("trash.inv.size", 27);
            savedDefaults++;
        }

        this.trash_inv_title = config.getString("trash_inv.title");
        if (trash_inv_title == null) {
            config.set("trash.inv.title", "<green>Trash Manager</green>");
            savedDefaults++;
        }

        if (savedDefaults > 0) {
            plugin.saveConfig();

            plugin.sendConsole("<green>Successfully loaded " + savedDefaults + " default configuration(s).</green>");
        }
    }

    public void loadSounds() {
        int savedDefaults = 0;

        for (Sounds sound : Sounds.values()) {
            if (!config.contains(sound.getPath() + ".sound")) {
                config.set(sound.getPath() + ".sound", sound);
                config.set(sound.getPath() + ".pitch", 0.4f);
                config.set(sound.getPath() + ".pitch", 1.0f);

                savedDefaults++;
            }

            sound.loadSound(config, plugin.getLogger());
        }

        if (savedDefaults > 0) {
            plugin.saveConfig();
            plugin.reloadConfig();

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
