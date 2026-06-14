package net.omni.miniEssentials.listener;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        // cancel tpa

        UUID uuid = event.getPlayer().getUniqueId();

        if (plugin.getTPAManager().hasTPA(uuid))
            plugin.getTPAManager().removeRequests(uuid);
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
