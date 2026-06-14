package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.MessageUtil;
import org.bukkit.command.*;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinieCommand implements CommandExecutor {
    private final MiniEssentials plugin;

    public MinieCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender.hasPermission("miniessentials.use"))) {
            plugin.sendMessage(sender, "<red>You do not have permission to use this command.</red>");
            return true;
        }

        if (args.length == 0) {
            // send help
            sender.sendMessage(MessageUtil.parse(getHelpText(sender)));
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!(sender.hasPermission("miniessentials.reload"))) {
                    plugin.sendMessage(sender, "<red>You do not have permission to use this command.</red>");
                    return true;
                }

                plugin.reloadConfig();

                // TODO reload messages.yml
                plugin.sendMessage(sender, "<green>Messages have been reloaded.");
            } else if (args[0].equalsIgnoreCase("about"))
                sender.sendMessage(MessageUtil.parse(getAboutText()));
            else if (args[0].equalsIgnoreCase("help"))
                plugin.sendMessage(sender, getHelpText(sender));
            else
                plugin.sendMessage(sender, "<red>Unknown command.</red>");

            return true;
        }

        plugin.sendMessage(sender, "<red>Unknown command.</red>");
        return true;
    }

    private @NonNull String getHelpText(CommandSender sender) {
        StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append("<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>\n");
        helpBuilder.append("  <gradient:#ffaa00:#fdb813><bold>MiniEssentials</bold></gradient> <gray>• Help</gray>\n\n");

        if (sender.hasPermission("miniessentials.use")) {
            MessageUtil.append("minie", "Base command for MiniEssentials.", helpBuilder);
            MessageUtil.append("minie help", "Shows this help menu.", helpBuilder);

            if (sender.hasPermission("miniessentials.reload"))
                MessageUtil.append("minie reload", "Reloads config and messages.", helpBuilder);

            MessageUtil.append("minie about", "Shows basic information about this plugin.", helpBuilder);
        }

        if (sender.hasPermission("miniessentials.echest"))
            MessageUtil.appendWithAliases("enderchest [player]", "Opens your enderchest or someone else's.", helpBuilder, "echest", "ec");

        if (sender.hasPermission("miniessentials.fix"))
            MessageUtil.append("fix", "Restores the durability of your current item in main hand.", helpBuilder);

        if (sender.hasPermission("miniessentials.gm"))
            MessageUtil.appendWithAliases("gamemode <type> [player]", "Changes your gamemode or someone else's.", helpBuilder, "gm");

        if (sender.hasPermission("miniessentials.god"))
            MessageUtil.append("god [player]", "Gives you or someone else immunity to damage or hunger.", helpBuilder);

        if (sender.hasPermission("miniessentials.openinv"))
            MessageUtil.append("openinv <player>", "Opens the player's inventory and enables real-time editing.", helpBuilder);

        if (sender.hasPermission("miniessentials.tpa")) {
            MessageUtil.append("tpa <player>", "Sends a teleport request to the player.", helpBuilder);
            MessageUtil.append("tpa accept <player>", "Accepts the TPA request from the player.", helpBuilder);
            MessageUtil.append("tpa deny <player>", "Denies the TPA request from the player.", helpBuilder);
        }

        if (sender.hasPermission("miniessentials.trash"))
            MessageUtil.append("trash", "Opens a trash menu. All contents are cleared on close.", helpBuilder);

        helpBuilder.append("<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>");

        return helpBuilder.toString();
    }

    private @NonNull String getAboutText() {
        String pluginName = plugin.getPluginMeta().getName();
        String version = plugin.getPluginMeta().getVersion();
        String author = plugin.getPluginMeta().getAuthors().getFirst();
        String githubUrl = "https://github.com/domninos/MiniEssentials";
        String discordUrl = "https://discord.gg/7CuCtDHmQ3";

        return "<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>\n" +
                "  <gradient:#ffaa00:#fdb813><bold>" + pluginName + "</bold></gradient> <gray>• Plugin Information</gray>\n\n" +
                "  <yellow>Version:</yellow> <white>" + version + "</white>\n" +
                "  <yellow>Author:</yellow> <aqua>" + author + "</aqua>\n\n" +
                "  <gray>Links: </gray>" +
                "<click:open_url:'" + githubUrl + "'><hover:show_text:'<gray>Click to view open-source code'><dark_purple>[GitHub]</dark_purple></hover></click> " +
                "<click:open_url:'" + discordUrl + "'><hover:show_text:'<gray>Click to join support community'><blue>[Discord]</blue></hover></click>\n" +
                "<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>";

    }

    public void register() {
        PluginCommand minieCommand = plugin.getCommand("minie");
        if (minieCommand == null) {
            plugin.getLogger().warning("/minie command not found!");
            return;
        }

        minieCommand.setTabCompleter(new TabCompleter() {
            @Override
            public @NonNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
                if (args.length == 1) {
                    List<String> subcommands = new ArrayList<>();

                    subcommands.add("help");
                    subcommands.add("about");

                    if (sender.hasPermission("miniessentials.reload")) {
                        subcommands.add("reload");
                    }

                    List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[0], subcommands, completions);

                    Collections.sort(completions);
                    return completions;
                }

                return Collections.emptyList();
            }
        });

        minieCommand.setExecutor(this);
    }
}
