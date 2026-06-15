package net.omni.miniEssentials.commands;

import net.omni.miniEssentials.MiniEssentials;
import net.omni.miniEssentials.util.MessageUtil;
import net.omni.miniEssentials.util.Messages;
import org.bukkit.command.*;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinieCommand implements CommandExecutor {
    private static final int COMMANDS_PER_PAGE = 4;
    private final MiniEssentials plugin;

    public MinieCommand(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender.hasPermission("miniessentials.use"))) {
            plugin.sendMessage(sender, Messages.NO_PERMS.toString());
            return true;
        }

        // send help
        if (args.length == 0) {
            sendHelpPage(sender, 1);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!(sender.hasPermission("miniessentials.reload"))) {
                    plugin.sendMessage(sender, Messages.NO_PERMS.toString());
                    return true;
                }

                plugin.getConfigUtil().reloadConfig();
                plugin.getMessagesManager().loadMessages();

                plugin.sendMessage(sender, "<green>config.yml and messages.yml have been reloaded.</green>");
            } else if (args[0].equalsIgnoreCase("about"))
                sender.sendMessage(MessageUtil.parse(getAboutText()));
            else if (args[0].equalsIgnoreCase("help"))
                sendHelpPage(sender, 1);
            else
                plugin.sendMessage(sender, Messages.UNKNOWN_COMMAND.toString());

            return true;
        } else if (args.length == 2) {
            if (!(args[0].equalsIgnoreCase("help"))) {
                plugin.sendMessage(sender, Messages.UNKNOWN_COMMAND.toString());
                return true;
            }

            int page = 1;

            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                plugin.sendMessage(sender, Messages.INVALID_PAGE.toString());
            }

            sendHelpPage(sender, page);
            return true;
        }

        plugin.sendMessage(sender, Messages.UNKNOWN_COMMAND.toString());
        return true;
    }

    private void sendHelpPage(CommandSender sender, int page) {
        List<String> lines = getAccessibleHelpLines(sender);

        int totalPages = (int) Math.ceil((double) lines.size() / COMMANDS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int startIdx = (page - 1) * COMMANDS_PER_PAGE;
        int endIdx = Math.min(startIdx + COMMANDS_PER_PAGE, lines.size());

        StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append("\n<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>\n");
        helpBuilder.append("  <gradient:#00AAFF:#55FFFF><bold>MiniEssentials</bold></gradient> <gray>• (")
                .append(page).append("/").append(totalPages).append(")</gray>\n\n");

        if (lines.isEmpty())
            helpBuilder.append("  <red>You do not have access to any commands.</red>\n");
        else
            for (int i = startIdx; i < endIdx; i++)
                helpBuilder.append(lines.get(i)).append("\n");

        helpBuilder.append("<dark_gray>▪▪▪▪▪▪▪▪</dark_gray>");

        if (page > 1) {
            helpBuilder.append(" <click:run_command:/minie help ").append(page - 1)
                    .append("><hover:show_text:'<gray>Click for page ").append(page - 1)
                    .append("'><aqua>[◀]</aqua></hover></click> ");
        } else {
            helpBuilder.append(" <gray>[◀]</gray> ");
        }

        helpBuilder.append("<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>");

        if (page < totalPages) {
            helpBuilder.append(" <click:run_command:/minie help ").append(page + 1)
                    .append("><hover:show_text:'<gray>Click for page ").append(page + 1)
                    .append("'><aqua><bold>[▶]</bold></aqua></hover></click> ");
        } else {
            helpBuilder.append(" <gray>[▶]</gray> ");
        }

        helpBuilder.append("<dark_gray>▪▪▪▪▪▪▪▪</dark_gray>\n");

        sender.sendMessage(MessageUtil.parse(helpBuilder.toString()));
    }

    private @NonNull String getAboutText() {
        String pluginName = plugin.getPluginMeta().getName();
        String version = plugin.getPluginMeta().getVersion();
        String author = plugin.getPluginMeta().getAuthors().getFirst();
        String githubUrl = "https://github.com/domninos/MiniEssentials";
        String discordUrl = "https://discord.gg/7CuCtDHmQ3";

        return "<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>\n" +
                "  <gradient:#00AAFF:#55FFFF><bold>" + pluginName + "</bold></gradient>\n\n" +
                "  <yellow>Version:</yellow> <white>" + version + "</white>\n" +
                "  <yellow>Author:</yellow> <aqua>" + author + "</aqua>\n\n" +
                "  <white>Links: </white>" +
                "<click:open_url:'" + githubUrl + "'><hover:show_text:'<gray>Click to view open-source code'><dark_purple>[GitHub]</dark_purple></hover></click> " +
                "<click:open_url:'" + discordUrl + "'><hover:show_text:'<gray>Click to join support community'><blue>[Discord]</blue></hover></click>\n" +
                "<dark_gray>▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪</dark_gray>";

    }

    private List<String> getAccessibleHelpLines(CommandSender sender) {
        List<String> helpLines = new ArrayList<>();

        if (sender.hasPermission("miniessentials.use")) {
            helpLines.add(MessageUtil.formatString("miniessentials", "Base command for MiniEssentials.", "minie"));
            helpLines.add(MessageUtil.formatString("minie <#55FFFF>help</#55FFFF>", "Shows this help menu."));

            if (sender.hasPermission("miniessentials.reload"))
                helpLines.add(MessageUtil.formatString("minie <#55FFFF>reload</#55FFFF>", "Reloads config and messages."));

            helpLines.add(MessageUtil.formatString("minie <#55FFFF>about</#55FFFF>", "Shows basic information about this plugin."));
        }

        if (sender.hasPermission("miniessentials.echest"))
            helpLines.add(MessageUtil.formatString("enderchest <italic><#55FFFF>[player]</#55FFFF></italic>", "Opens your enderchest or someone else's.", "echest", "ec"));

        if (sender.hasPermission("miniessentials.fix"))
            helpLines.add(MessageUtil.formatString("fix", "Restores the durability of your current item in main hand."));

        if (sender.hasPermission("miniessentials.gm"))
            helpLines.add(MessageUtil.formatString("gamemode <#55FFFF><bold><type></bold> <italic>[player]</italic></#55FFFF>", "Changes your gamemode or someone else's.", "gm"));

        if (sender.hasPermission("miniessentials.god"))
            helpLines.add(MessageUtil.formatString("god <italic><#55FFFF>[player]</#55FFFF></italic>", "Gives you or someone else immunity to damage or hunger."));

        if (sender.hasPermission("miniessentials.openinv"))
            helpLines.add(MessageUtil.formatString("openinv <bold><#55FFFF><player></#55FFFF></bold>", "Opens the player's inventory and enables real-time editing."));

        if (sender.hasPermission("miniessentials.tpa")) {
            helpLines.add(MessageUtil.formatString("tpa <bold><#55FFFF><player><#55FFFF></bold>", "Sends a teleport request to the player."));
            helpLines.add(MessageUtil.formatString("tpa <#55FFFF>accept <bold><player></bold></#55FFFF>", "Accepts the TPA request from the player."));
            helpLines.add(MessageUtil.formatString("tpa <#55FFFF>deny <bold><player></bold></#55FFFF>", "Denies the TPA request from the player."));
        }

        if (sender.hasPermission("miniessentials.trash"))
            helpLines.add(MessageUtil.formatString("trash", "Opens a trash menu. All contents are cleared on close."));

        return helpLines;
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

                    if (sender.hasPermission("miniessentials.reload"))
                        subcommands.add("reload");

                    List<String> completions = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[0], subcommands, completions);

                    return completions;
                }

                return Collections.emptyList();
            }
        });

        minieCommand.setExecutor(this);
    }
}
