package net.omni.miniEssentials.managers;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GodManager {
    private final Set<Player> godPlayers = new HashSet<>();

    public void toggleGod(Player player) {
        if (isGod(player))
            godPlayers.remove(player);
        else
            setGod(player);
    }

    public boolean isGod(Player player) {
        return godPlayers.contains(player);
    }

    public void setGod(Player player) {
        godPlayers.add(player);
    }

    public void removeGod(Player player) {
        godPlayers.remove(player);
    }

    public void flush() {
        godPlayers.clear();
    }
}
