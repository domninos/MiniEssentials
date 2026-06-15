package net.omni.miniEssentials.util;

import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public enum Sounds {
    FIX_SUCCESS("sounds.fix_success", "block.anvil.use"),
    FIX_ERROR("sounds.fix_error", "block.anvil.error"),
    TRASHED("sounds.trashed", "block.bamboo_wood.break");

    private final String path, defaultVal;
    private Sound sound;

    Sounds(String path, String defaultVal) {
        this.path = path;
        this.defaultVal = defaultVal.contains(":") ? defaultVal : "minecraft:" + defaultVal;
    }

    public void loadSound(FileConfiguration config, Logger logger) {
        Key fallback = Key.key(this.defaultVal);

        String sound_name = config.getString(path + ".key", fallback.asString());
        float volume = (float) config.getDouble(path + ".volume", 0.4f);
        float pitch = (float) config.getDouble(path + ".pitch", 1f);

        String formattedKey = sound_name.toLowerCase().trim();

        if (sound_name.equals(sound_name.toUpperCase()))
            formattedKey = formattedKey.replace("_", ".");

        Key finalKey = fallback;

        try {
            NamespacedKey key = NamespacedKey.minecraft(formattedKey);
            org.bukkit.Sound sound = Registry.SOUNDS.get(key);

            if (sound != null)
                finalKey = Registry.SOUNDS.getKey(sound);
            else
                logger.warning("No sound found: " + formattedKey);
        } catch (IllegalArgumentException | InvalidKeyException e) {
            logger.warning("Invalid sound configuration for " + path);
        }

        this.sound = Sound.sound(finalKey != null ? finalKey : fallback, Sound.Source.MASTER, volume, pitch);
    }

    public Sound getSound() {
        if (sound == null)
            return Sound.sound(Key.key("minecraft:entity.experience_orb.pickup"), Sound.Source.MASTER, 1f, 1f);

        return sound;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultVal() {
        return defaultVal;
    }
}
