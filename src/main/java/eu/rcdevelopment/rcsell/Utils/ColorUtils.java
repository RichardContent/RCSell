package eu.rcdevelopment.rcsell.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ColorUtils {

    private static final LegacyComponentSerializer LEGACY_FORMATTER = LegacyComponentSerializer.legacyAmpersand();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

        if (version.startsWith("v1_7") || version.startsWith("v1_8") || version.startsWith("v1_9") || version.startsWith("v1_10") ||
                version.startsWith("v1_11") || version.startsWith("v1_12") || version.startsWith("v1_13") || version.startsWith("v1_14") ||
                version.startsWith("v1_15")) {
            LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('§').build();
        } else {
            LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('&').build();
        }
    }

    @NotNull
    public static String format(@NotNull String message) {
        message = message.replace('§', '&');
        message = toLegacy(MINI_MESSAGE.deserialize(message).applyFallbackStyle(TextDecoration.ITALIC.withState(false)));
        message = LEGACY_FORMATTER.serialize(LEGACY_FORMATTER.deserialize(message));
        message = org.bukkit.ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }

    @NotNull
    public static String toLegacy(@NotNull Component component) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(component);
    }

    @NotNull
    public static List<String> formatList(@NotNull List<String> messages) {
        return messages.stream()
                .map(ColorUtils::format)
                .collect(Collectors.toList());
    }
}