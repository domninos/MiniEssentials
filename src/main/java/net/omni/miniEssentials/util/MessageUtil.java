package net.omni.miniEssentials.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Component PREFIX =
            MiniMessage.miniMessage().deserialize(
                    "<gray>[</gray><gradient:#00AAFF:#55FFFF>MiniEssentials</gradient><gray>]</gray> "
            );

    private MessageUtil() {
    }

    /**
     * Color your text.
     * Note: Uses prefix
     *
     * @param msg String to deserialize
     * @return the deserialized colored text
     */
    public static Component color(String msg) {
        return PREFIX.append(MINI_MESSAGE.deserialize(msg));
    }

    /**
     * Color your text.
     * Note: Uses prefix
     *
     * @param msg       String to deserialize
     * @param resolvers parsed keys to values
     * @return the deserialized colored text
     */
    public static Component color(String msg, TagResolver... resolvers) {
        return PREFIX.append(MINI_MESSAGE.deserialize(msg, resolvers));
    }

    /**
     * Parses a string whether using the legacy '&' color codes or the new minimessage format
     *
     * @param msg String to deserialize
     * @return the deserialized colored text
     */
    public static Component parse(String msg) {
        if (msg.contains("<") && msg.contains(">"))
            return MINI_MESSAGE.deserialize(msg);
        else
            return LegacyComponentSerializer.legacyAmpersand().deserialize(msg);
    }

    /**
     * For my commands.
     * Appends commands its description to a {@link StringBuilder}.
     *
     * @param command     the command label and arguments (without `/`)
     * @param description description of the command
     * @param builder     the {@link StringBuilder} to append to
     */
    public static void append(String command, String description, StringBuilder builder) {
        builder.append("  <gold>/")
                .append(command)
                .append("</gold> <dark_gray>-</dark_gray> <gray>")
                .append(description)
                .append("</gray>")
                .append("\n");
    }

    /**
     * For my commands.
     * Appends the plugin command labels, descriptions, and aliases to a {@link StringBuilder}.
     *
     * @param command     the command label and arguments (without `/`)
     * @param description description of the command
     * @param builder     the {@link StringBuilder} to append to
     * @param aliases     command aliases (if any).
     */
    public static void appendWithAliases(String command, String description, StringBuilder builder, String... aliases) {
        builder.append("  <gold>/")
                .append(command)
                .append("</gold> <dark_gray>-</dark_gray> <gray>")
                .append(description)
                .append("</gray>")
                .append("\n");

        if (aliases != null && aliases.length > 0) {
            builder.append("  <dark_gray>↳ <italic>Aliases: ")
                    .append(java.util.Arrays.toString(aliases))
                    .append("</italic></dark_gray>\n");
        }
    }
}