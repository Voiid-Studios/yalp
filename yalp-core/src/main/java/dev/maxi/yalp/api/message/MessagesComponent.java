package dev.maxi.yalp.api.message;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MessagesComponent implements YALPComponent {
    private static final int CENTER_PX = 154;

    private String prefix = "&7[&aYALP&7]&r";
    private boolean adventureAvailable;

    @Override
    public String getId() {
        return "messages";
    }

    @Override
    public void onLoad(ComponentContext context) {
        adventureAvailable = hasClass("net.kyori.adventure.text.minimessage.MiniMessage");
        context.getLogger().info("Messages ready. Fancy words may now be green.");
    }

    public String color(String text) {
        return text == null ? "" : ChatColor.translateAlternateColorCodes('&', text);
    }

    public List<String> color(List<String> lines) {
        List<String> colored = new ArrayList<>();
        if (lines == null) {
            return colored;
        }
        for (String line : lines) {
            colored.add(color(line));
        }
        return colored;
    }

    public void send(CommandSender sender, String message) {
        if (sender != null) {
            sender.sendMessage(color(format(message, Collections.singletonMap("prefix", prefix))));
        }
    }

    public void send(Player player, String message) {
        send((CommandSender) player, message);
    }

    public void send(ConsoleCommandSender console, String message) {
        send((CommandSender) console, message);
    }

    public void send(CommandSender sender, String message, String... placeholders) {
        send(sender, format(message, placeholders));
    }

    public void send(CommandSender sender, String message, Map<String, String> placeholders) {
        send(sender, format(message, placeholders));
    }

    public void sendLines(CommandSender sender, List<String> lines) {
        if (sender == null || lines == null) {
            return;
        }
        for (String line : lines) {
            send(sender, line);
        }
    }

    public void sendLines(CommandSender sender, String... lines) {
        if (lines == null) {
            return;
        }
        for (String line : lines) {
            send(sender, line);
        }
    }

    public String format(String message, Map<String, String> placeholders) {
        String output = message == null ? "" : message;
        Map<String, String> merged = new HashMap<>();
        merged.put("prefix", prefix);
        if (placeholders != null) {
            merged.putAll(placeholders);
        }
        for (Map.Entry<String, String> entry : merged.entrySet()) {
            output = output.replace("{" + entry.getKey() + "}", entry.getValue() == null ? "" : entry.getValue());
        }
        return color(output);
    }

    public String format(String message, String... placeholders) {
        Map<String, String> mapped = new HashMap<>();
        if (placeholders != null) {
            for (int i = 0; i + 1 < placeholders.length; i += 2) {
                mapped.put(placeholders[i], placeholders[i + 1]);
            }
        }
        return format(message, mapped);
    }

    public String stripColors(String text) {
        return ChatColor.stripColor(color(text));
    }

    public String center(String message) {
        String colored = color(message);
        String stripped = ChatColor.stripColor(colored);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean bold = false;

        for (char character : stripped.toCharArray()) {
            if (character == ChatColor.COLOR_CHAR) {
                previousCode = true;
                continue;
            }
            if (previousCode) {
                previousCode = false;
                bold = character == 'l' || character == 'L';
                continue;
            }
            messagePxSize += character == ' ' ? 4 : 6;
            if (bold) {
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = 4;
        StringBuilder builder = new StringBuilder();
        while (toCompensate > spaceLength) {
            builder.append(' ');
            toCompensate -= spaceLength;
        }
        return builder.append(colored).toString();
    }

    public boolean isAdventureAvailable() {
        return adventureAvailable;
    }

    public boolean looksLikeMiniMessage(String text) {
        return adventureAvailable && text != null && text.contains("<") && text.contains(">");
    }

    public Object tryDeserializeMiniMessage(String text) {
        if (!looksLikeMiniMessage(text)) {
            return null;
        }
        try {
            Class<?> miniMessage = Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            Object instance = miniMessage.getMethod("miniMessage").invoke(null);
            Method deserialize = miniMessage.getMethod("deserialize", String.class);
            return deserialize.invoke(instance, text);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? "" : prefix;
    }

    private boolean hasClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
