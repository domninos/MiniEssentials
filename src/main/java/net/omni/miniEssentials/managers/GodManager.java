package net.omni.miniEssentials.managers;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GodManager {
    private final Set<Player> godPlayers = new HashSet<>();

    private final MiniEssentials plugin;

    public GodManager(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    public void toggleGod(Player player) {
        if (isGod(player)) {
            removeGod(player);
            plugin.sendMessage(player, Messages.GOD_DISABLED.toString());
        } else {
            setGod(player);
            plugin.sendMessage(player, Messages.GOD_ENABLED.toString());
        }
    }

    public boolean isGod(Player player) {
        return godPlayers.contains(player);
    }

    public void removeGod(Player player) {
        godPlayers.remove(player);
    }

    public void setGod(Player player) {
        godPlayers.add(player);
    }

    public void flush() {
        godPlayers.clear();
    }
}
