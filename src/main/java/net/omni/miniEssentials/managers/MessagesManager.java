package net.omni.miniEssentials.managers;

import net.omni.miniEssentials.MiniEssentials;

import java.util.List;

public class MessagesManager {
    // TODO messages.yml

    private final MiniEssentials plugin;

    private String no_perms;
    private String only_players;
    private String player_not_online; // have {player}

    private String usage; // have {usage}
    private String unknown_cmd;

    private String fix_error;
    private String fix_success;

    private String gm_not_found;
    private String gm_set; // have {player}, {gamemode}

    private String god_toggled; // have {target}
    private String god_enabled;
    private String god_disabled;

    private String reloaded;
    private String invalid_page;

    private String cannot_use_self;

    private String tpa_not_found;
    private String tpa_already; // have {time_left}
    private List<String> tpa_request; // have <from_player>
    private String tpa_request_from; // have <to_player>
    private String tpa_request_expired; // have {player}
    private String tpa_none;
    private String tpa_accept;
    private String tpa_deny; // have {player}

    private String trashed_success; // have <amount>

    public MessagesManager(MiniEssentials plugin) {
        this.plugin = plugin;
    }

    public void loadMessages() {
        this.no_perms = plugin.getMessagesConfig().getString("no_perms");
        this.only_players = plugin.getMessagesConfig().getString("only_players");
        this.player_not_online = plugin.getMessagesConfig().getString("player_not_online");

        this.usage = plugin.getMessagesConfig().getString("usage");
        this.unknown_cmd = plugin.getMessagesConfig().getString("unknown_cmd");

        this.fix_error = plugin.getMessagesConfig().getString("fix_error");
        this.fix_success = plugin.getMessagesConfig().getString("fix_success");

        this.gm_not_found = plugin.getMessagesConfig().getString("gm_not_found");
        this.gm_set = plugin.getMessagesConfig().getString("gm_set");

        this.god_toggled = plugin.getMessagesConfig().getString("god_toggled");
        this.god_enabled = plugin.getMessagesConfig().getString("god_enabled");
        this.god_disabled = plugin.getMessagesConfig().getString("god_disabled");

        this.reloaded = plugin.getMessagesConfig().getString("reloaded");
        this.invalid_page = plugin.getMessagesConfig().getString("invalid_page");

        this.cannot_use_self = plugin.getMessagesConfig().getString("cannot_use_self");

        this.tpa_not_found = plugin.getMessagesConfig().getString("tpa_not_found");
        this.tpa_already = plugin.getMessagesConfig().getString("tpa_already");
        this.tpa_request = plugin.getMessagesConfig().getStringList("tpa_request");
        this.tpa_request_from = plugin.getMessagesConfig().getString("tpa_request_from");
        this.tpa_request_expired = plugin.getMessagesConfig().getString("tpa_request_expired");
        this.tpa_none = plugin.getMessagesConfig().getString("tpa_none");
        this.tpa_accept = plugin.getMessagesConfig().getString("tpa_accept");
        this.tpa_deny = plugin.getMessagesConfig().getString("tpa_deny");

        this.trashed_success = plugin.getMessagesConfig().getString("trashed_success");

        plugin.sendConsole("<green>Successfully loaded messages.</green>");
    }

    public String parsed(String message, String key, String value) {
        return message.replace("<" + key + ">", value);
    }

    public void flush() {
        tpa_request.clear();
    }
}