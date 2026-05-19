package dev.maxi.yalp.api.message;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MessagesComponent implements YALPComponent {
    private String prefix = "&7[&aYALP&7]&r";

    @Override
    public String getId() {
        return "messages";
    }

    @Override
    public void onLoad(ComponentContext context) {
        context.getLogger().info("Messages ready. Fancy words may now be green.");
    }

    public String color(String text) {
        return text == null ? "" : ChatColor.translateAlternateColorCodes('&', text);
    }

    public void send(CommandSender sender, String message) {
        if (sender != null) {
            sender.sendMessage(color(format(message, Collections.singletonMap("prefix", prefix))));
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? "" : prefix;
    }
}
