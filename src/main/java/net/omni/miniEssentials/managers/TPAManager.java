package net.omni.miniEssentials.managers;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TPAManager {

    private final Map<UUID, Set<UUID>> tpaRequests = new HashMap<>();

    private final Map<UUID, Long> tpaCooldowns = new HashMap<>();

    private final Map<UUID, BukkitTask> tpaTasks = new HashMap<>();

    private final MiniEssentials plugin;

    public TPAManager(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    public void submitTPA(UUID fromPlayer, UUID toPlayer) {
        Player player = Bukkit.getPlayer(fromPlayer);

        if (player == null)
            return;

        Player targetPlayer = Bukkit.getPlayer(toPlayer);

        if (targetPlayer == null)
            return;

        if (fromPlayer.equals(toPlayer)) {
            plugin.sendMessage(player, Messages.CANNOT_USE_SELF.toString());
            return;
        }

        if (hasTPA(fromPlayer, toPlayer) && tpaCooldowns.containsKey(fromPlayer)) {
            long timeLeft = (tpaCooldowns.get(fromPlayer) - System.currentTimeMillis()) / 1000;

            if (timeLeft <= 0) timeLeft = 1;

            String cd = timeLeft > 1 ? timeLeft + " seconds" : timeLeft + " second";

            plugin.sendMessage(player, Messages.TPA_ALREADY.replace("time_left", cd));
            return;
        }

        plugin.sendMessage(targetPlayer, Messages.TPA_REQUEST_TO.replaceList("from_player", player.getName()), false);

        plugin.sendMessage(player, Messages.TPA_REQUEST_FROM.replace("to_player", targetPlayer.getName()));

        tpaTasks.put(fromPlayer, Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Set<UUID> requests = getTPARequests(toPlayer);

            if (!requests.isEmpty())
                requests.remove(fromPlayer);

            tpaCooldowns.remove(fromPlayer);

            tpaTasks.remove(fromPlayer);

            if (player.isOnline() && targetPlayer.isOnline())
                plugin.sendMessage(player, Messages.TPA_REQUEST_EXPIRED.replace("to_player", targetPlayer.getName()));
        }, 20L * plugin.getConfigUtil().getTPACooldown()));

        tpaCooldowns.put(fromPlayer, System.currentTimeMillis() + (plugin.getConfigUtil().getTPACooldown() * 1000L));

        tpaRequests.compute(toPlayer, (_, requests) -> {
            if (requests == null)
                requests = new HashSet<>();

            requests.add(fromPlayer);
            return requests;
        });
    }

    public boolean hasTPA(UUID fromPlayer, UUID toPlayer) {
        return tpaRequests.containsKey(toPlayer) && tpaRequests.get(toPlayer).contains(fromPlayer);
    }

    public Set<UUID> getTPARequests(UUID player) {
        return tpaRequests.get(player);
    }

    public boolean hasTPA(UUID player) {
        return tpaRequests.containsKey(player) || tpaRequests.values().stream().anyMatch(set -> set.contains(player));
    }

    public void acceptTPA(UUID fromPlayer, UUID toPlayer) {
        // used /tpaccept <player>

        Player player = Bukkit.getPlayer(fromPlayer);

        if (player == null)
            return;

        Player targetPlayer = Bukkit.getPlayer(toPlayer);

        if (targetPlayer == null)
            return;

        if (!hasTPA(fromPlayer, toPlayer)) {
            plugin.sendMessage(targetPlayer, Messages.TPA_NOT_FOUND.toString());
            return;
        }

        player.teleport(targetPlayer.getLocation());

        removeRequestFromPlayer(fromPlayer, toPlayer);

        plugin.sendMessage(player, Messages.TPA_ACCEPT.replace("to_player", targetPlayer.getName()));
    }

    public void removeRequestFromPlayer(UUID player, UUID toPlayer) {
        tpaCooldowns.remove(player);

        BukkitTask task = tpaTasks.get(player);

        if (task != null && !task.isCancelled())
            task.cancel();

        tpaRequests.get(toPlayer).remove(player);
    }

    public void denyTPA(UUID fromPlayer, UUID toPlayer) {
        // used /tpa deny <player>

        Player player = Bukkit.getPlayer(fromPlayer);

        if (player == null)
            return;

        Player targetPlayer = Bukkit.getPlayer(toPlayer);

        if (targetPlayer == null)
            return;

        if (!hasTPA(fromPlayer, toPlayer)) {
            plugin.sendMessage(targetPlayer, Messages.TPA_NOT_FOUND.toString());
            return;
        }

        removeRequestFromPlayer(fromPlayer, toPlayer);

        plugin.sendMessage(player, Messages.TPA_DENY.replace("to_player", targetPlayer.getName()));
    }

    public void removeRequests(UUID player) {
        tpaCooldowns.remove(player);

        BukkitTask task = tpaTasks.get(player);

        if (task != null && !task.isCancelled())
            task.cancel();

        tpaRequests.values().forEach(set -> set.remove(player));
    }

    public UUID getLatestTPA(UUID toPlayer) {
        if (tpaRequests.isEmpty())
            return null;

        if (tpaRequests.containsKey(toPlayer)) {
            Set<UUID> requests = tpaRequests.get(toPlayer);

            if (requests.isEmpty())
                return null;

            return requests.iterator().next();
        }

        return null;
    }

    public void flush() {
        if (!tpaRequests.isEmpty()) {
            tpaRequests.values().forEach(Set::clear);
            tpaRequests.clear();
        }

        if (!tpaTasks.isEmpty()) {
            tpaTasks.values().forEach(BukkitTask::cancel);
            tpaTasks.clear();
        }

        tpaCooldowns.clear();
    }
}
