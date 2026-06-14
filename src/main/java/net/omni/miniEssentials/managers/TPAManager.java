package net.omni.miniEssentials.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.MessageUtil;
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

        Player targetPlayer = Bukkit.getPlayer(fromPlayer);

        if (targetPlayer == null)
            return;

        if (hasTPA(fromPlayer, toPlayer) && tpaCooldowns.containsKey(fromPlayer)) {
            long timeLeft = (tpaCooldowns.get(fromPlayer) - System.currentTimeMillis()) / 1000;

            if (timeLeft < 0) timeLeft = 1;

            String cd = timeLeft > 1 ? timeLeft + " seconds" : timeLeft + " second";

            plugin.sendMessage(player, "<red>You already have an ongoing TPA request. Please wait for " + cd + ".</red>");
            return;
        }

        // TODO messages.yml
        String miniMessageString = """
                
                <yellow><from_player></yellow> is requesting to teleport to you.
                <bold><click:run_command:/tpa accept <from_player>><hover:show_text:'<green>Click to accept'><green>[ACCEPT]</green></hover></click></bold> \
                 <bold><click:run_command:/tpa deny <from_player>><hover:show_text:'<red>Click to deny'><red>[DENY]</red></hover></click></bold>
                
                """;

        Component text = MessageUtil.parse(miniMessageString, Placeholder.parsed("from_player", player.getName()));

        player.sendMessage(text);

        tpaTasks.put(fromPlayer, Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Set<UUID> requests = getTPARequests(toPlayer);

            if (!requests.isEmpty())
                requests.remove(fromPlayer);

            tpaCooldowns.remove(fromPlayer);

            tpaTasks.remove(fromPlayer);

            if (player.isOnline())
                plugin.sendMessage(player, "<red>Your TPA request to " + targetPlayer.getName() + " has expired.</red>");
        }, 20 * 30)); // TODO config - default: 30 seconds

        tpaCooldowns.put(fromPlayer, System.currentTimeMillis() + 60 * 100 * 1000);

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

    public String acceptTPA(UUID fromPlayer, UUID toPlayer) {
        // used /tpaccept <player>

        Player player = Bukkit.getPlayer(fromPlayer);

        if (player == null)
            return "<red>Something went wrong. Please try again.</red>";

        Player targetPlayer = Bukkit.getPlayer(fromPlayer);

        if (targetPlayer == null)
            return "<red>Something went wrong. Please try again.</red>";

        if (!hasTPA(fromPlayer, toPlayer))
            return "<red>Could not find your TPA request. Please try again.</red>";

        player.teleport(targetPlayer.getLocation());

        removeRequestFromPlayer(fromPlayer, toPlayer);

        // TODO messages.yml
        return "<yellow>You have teleported to " + targetPlayer.getName() + "'s location.";
    }

    public void removeRequestFromPlayer(UUID player, UUID toPlayer) {
        tpaCooldowns.remove(player);

        BukkitTask task = tpaTasks.get(player);

        if (task != null && !task.isCancelled())
            task.cancel();

        tpaRequests.get(toPlayer).remove(player);
    }

    public String denyTPA(UUID fromPlayer, UUID toPlayer) {
        // used /tpa deny <player>

        Player player = Bukkit.getPlayer(fromPlayer);

        if (player == null)
            return "<red>Something went wrong. Please try again.</red>";

        Player targetPlayer = Bukkit.getPlayer(fromPlayer);

        if (targetPlayer == null)
            return "<red>Something went wrong. Please try again.</red>";

        if (!hasTPA(fromPlayer, toPlayer))
            return "<red>Could not find your TPA request. Please try again.</red>";

        removeRequestFromPlayer(fromPlayer, toPlayer);

        // TODO messages.yml
        return "<red>Your TPA request to " + targetPlayer.getName() + " was denied.</red>";
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
