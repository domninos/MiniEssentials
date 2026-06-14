package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameModeCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public GameModeCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("miniessentials.gm")) {
            plugin.sendMessage(sender, "<red>You do not have permission to use this command.</red>");
            return true;
        }

        // for console / staff only
        if (args.length == 0) {
            plugin.sendMessage(sender, "<red>Usage: /gamemode <type> [player]</red>");
            return true;
        } else {
            GameMode gameMode = null;

            try {
                gameMode = GameMode.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore
            }

            if (gameMode == null) {
                // still null
                // try with value

                try {
                    gameMode = GameMode.getByValue(Integer.parseInt(args[0]));
                } catch (NumberFormatException e) {
                    plugin.sendMessage(sender, "<red>Invalid game mode.</red>");
                    return true;
                }
            }

            if (gameMode == null) {
                plugin.sendMessage(sender, "<red>Could not find gamemode.</red>");
                return true;
            }

            if (args.length == 1) {
                if (sender instanceof Player player) {
                    player.setGameMode(gameMode);
                    plugin.sendMessage(sender, "<yellow>You are now in " + gameMode.name() + " MODE.</yellow>");
                } else
                    plugin.sendMessage(sender, "<red>Only players can use this command.</red>");

                return true;
            } else if (args.length == 2) {
                if (sender instanceof Player player && !player.hasPermission("miniessentials.g,.other")) {
                    plugin.sendMessage(player, "<red>You do not have permission to use this command.</red>");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);

                if (target == null || target.getPlayer() == null || !target.hasPlayedBefore() || !target.isOnline()) {
                    plugin.sendMessage(sender, "<red>Player " + args[0] + " is not online.</red>");
                    return true;
                }

                target.getPlayer().setGameMode(gameMode);
                plugin.sendMessage(sender, "<yellow>" + target.getName() + " is now in " + gameMode.name() + "MODE.</yellow>");
                return true;
            } else {
                plugin.sendMessage(sender, "<red>Unknown command.</red>");
                return true;
            }
        }
    }

    public void register() {
        PluginCommand gameModeCommand = plugin.getCommand("gamemode");
        if (gameModeCommand == null) {
            plugin.getLogger().warning("/gamemode command not found!");
            return;
        }

        gameModeCommand.setTabCompleter(new TabCompleter() {
            @Override
            public @NonNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
                if (args.length == 1) {
                    List<String> subcommands = List.of(Arrays.toString(GameMode.values()));

                    List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[0], subcommands, completions);

                    Collections.sort(completions);
                    return completions;
                }

                return Collections.emptyList();
            }
        });

        gameModeCommand.setExecutor(this);
    }
}
