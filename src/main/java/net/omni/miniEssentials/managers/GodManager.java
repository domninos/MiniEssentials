package net.omni.miniEssentials.managers;

import net.omni.miniEssentials.MiniEssentials;
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
            plugin.sendMessage(player, "<red>You are not in GOD mod anymore.</red>");
        } else {
            setGod(player);
            plugin.sendMessage(player, "<green>You are now in GOD mode!</green>");
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
