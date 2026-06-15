package net.omni.miniEssentials.util;

import java.util.ArrayList;
import java.util.List;

public enum Messages {
    NO_PERMS("no_perms", "<red>You do not have permission to use this command.</red>"),
    ONLY_PLAYERS("only_players", "<red>Only players can use this command.</red>"),
    PLAYER_NOT_ONLINE("player_not_online", "<red>Player %player% is not online.</red>"),

    USAGE("usage", "<red>Invalid arguments. Usage: %usage%</red>"),
    UNKNOWN_COMMAND("unknown_cmd", "<red>Unknown command.</red>"),

    FIX_ERROR("fix_error", "<red>Could not fix item. Please try a different item.</red>"),
    FIX_SUCCESS("fix_success", "<green>Successfully fixed your item!</green>"),

    GM_NOT_FOUND("gm_not_found", "<red>Could not find gamemode.</red>"),
    GM_SET_SELF("gm_set_self", "<yellow>You are now in %gamemode% MODE.</yellow>"),
    GM_SET_OTHER("gm_set_other", "<yellow>%player% is now in %gamemode% MODE.</yellow>"),

    GOD_TOGGLED("god_toggled", "<green>Successfully toggled GOD for %player%.</green>"),
    GOD_ENABLED("god_enabled", "<green>You are now in GOD mode!</green>"),
    GOD_DISABLED("god_disabled", "<red>You are not in GOD mod anymore.</red>"),

    RELOADED("reloaded", "<green>config.yml and messages.yml have been reloaded.</green>"),
    INVALID_PAGE("invalid_page", "<red>Invalid page number. Defaulting to page 1.</red>"),

    CANNOT_USE_SELF("cannot_use_self", "<red>You cannot use this on yourself.</red>"),
    CANNOT_SHIFT_CLICK("cannot_shift_click", "<red>You cannot shift-click items.</red>"),

    TPA_NOT_FOUND("tpa_not_found", "<red>Could not find your TPA request. Please try again.</red>"),
    TPA_REQUEST_TO("tpa_request_to",
            new ArrayList<>(List.of(
                    " ",
                    " <yellow>%from_player%</yellow> is requesting to teleport to you.",
                    "          <bold><click:run_command:/tpa accept %from_player%><hover:show_text:'<green>Click to accept'><green>[ACCEPT]</green></hover></click></bold> <bold><click:run_command:/tpa deny %from_player%><hover:show_text:'<red>Click to deny'><red>[DENY]</red></hover></click></bold>",
                    " "
            ))),
    TPA_REQUEST_FROM("tpa_request_from", "<yellow>You have sent %to_player% a TPA request.</yellow>"),
    TPA_REQUEST_EXPIRED("tpa_request_expired", "<red>Your TPA request to %to_player% has expired.</red>"),
    TPA_ALREADY("tpa_already", "<red>You already have an ongoing TPA request. Please wait for %time_left%.</red>"),
    TPA_NONE("tpa_none", "<red>No TPA found.</red>"),
    TPA_ACCEPT("tpa_accept", "<yellow>You have teleported to %to_player%'s location."),
    TPA_DENY("tpa_deny", "<red>Your TPA request to %to_player% was denied.</red>"),

    TRASHED_SUCCESS("trashed_success", "<green>Successfully trashed %amount% item(s).</green>");

    private final String path;
    private final Object defaultVal;
    private Object cachedVal;

    Messages(String path, Object defaultVal) {
        this.path = path;
        this.defaultVal = defaultVal;
    }

    public String getPath() {
        return path;
    }

    public Object getDefaultVal() {
        return defaultVal;
    }

    public void setCachedVal(Object val) {
        this.cachedVal = val;
    }

    public String replace(String... pairs) {
        String result = this.toString();

        return replace(result, pairs);
    }

    @Override
    public String toString() {
        if (cachedVal instanceof List<?>)
            return "";

        return cachedVal instanceof String ? (String) cachedVal : (String) defaultVal;
    }

    private String replace(String result, String... pairs) {
        if (result.isEmpty())
            return "";

        for (int i = 0; i < pairs.length - 1; i += 2) {
            String key = pairs[i];
            String val = pairs[i + 1];

            if (key != null && val != null) {
                result = result.replace("%" + key + "%", val);
            }
        }

        return result;
    }

    public String replaceList(String... pairs) {
        List<String> originalList = this.asList();

        if (originalList.isEmpty())
            return "";

        List<String> modifiedList = new ArrayList<>();

        for (String line : originalList) {
            if (line != null)
                modifiedList.add(replace(line, pairs));
        }

        return String.join("\n", modifiedList);
    }

    @SuppressWarnings("unchecked")
    public List<String> asList() {
        return cachedVal instanceof List<?> ? (List<String>) cachedVal : (List<String>) defaultVal;
    }

    public void flush() {
        if (cachedVal instanceof List<?> cachedList)
            cachedList.clear();

        if (defaultVal instanceof List<?> defaultList)
            defaultList.clear();

        this.cachedVal = null;
    }
}
