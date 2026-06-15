package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
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
            plugin.sendMessage(sender, Messages.NO_PERMS.toString());
            return true;
        }

        // for console / staff only
        if (args.length == 0) {
            plugin.sendMessage(sender, Messages.USAGE.replace("usage", "/gamemode <type> [player]"));
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
                    plugin.sendMessage(sender, Messages.GM_NOT_FOUND.toString());
                    return true;
                }
            }

            if (gameMode == null) {
                plugin.sendMessage(sender, Messages.GM_NOT_FOUND.toString());
                return true;
            }

            if (args.length == 1) {
                if (sender instanceof Player player) {
                    player.setGameMode(gameMode);
                    plugin.sendMessage(sender, Messages.GM_SET_SELF.replace("gamemode", gameMode.name()));
                } else
                    plugin.sendMessage(sender, Messages.ONLY_PLAYERS.toString());

                return true;
            } else if (args.length == 2) {
                if (sender instanceof Player player && !player.hasPermission("miniessentials.gm.other")) {
                    plugin.sendMessage(player, Messages.NO_PERMS.toString());
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);

                if (target == null|| !target.hasPlayedBefore() || !target.isOnline()) {
                    plugin.sendMessage(sender, Messages.PLAYER_NOT_ONLINE.replace("player", args[0]));
                    return true;
                }

                target.setGameMode(gameMode);
                plugin.sendMessage(sender, Messages.GM_SET_OTHER.replace("player", target.getName(), "gamemode", gameMode.name()));
                return true;
            } else {
                plugin.sendMessage(sender, Messages.UNKNOWN_COMMAND.toString());
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
                    List<String> subcommands = new ArrayList<>();

                    subcommands.add("survival");
                    subcommands.add("creative");
                    subcommands.add("adventure");
                    subcommands.add("spectator");

                    List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[0], subcommands, completions);

                    return completions;
                }

                return Collections.emptyList();
            }
        });

        gameModeCommand.setExecutor(this);
    }
}
