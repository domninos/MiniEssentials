package net.omni.miniEssentials.listener;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

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

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
