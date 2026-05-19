package voiidstudios.yalp.bukkit.messages;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.messages.BasicMessageFormatter;

import java.util.List;
import java.util.Map;

public final class BukkitMessages extends BasicMessageFormatter implements YALPComponent {
    private final boolean adventureAvailable;

    public BukkitMessages() {
        this.adventureAvailable = hasClass("net.kyori.adventure.text.minimessage.MiniMessage");
    }

    @Override
    public String getId() {
        return "messages";
    }

    public void send(CommandSender sender, String message) {
        if (sender != null) {
            sender.sendMessage(format(message));
        }
    }

    public void send(Player player, String message) {
        send((CommandSender) player, message);
    }

    public void send(ConsoleCommandSender console, String message) {
        send((CommandSender) console, message);
    }

    public void send(CommandSender sender, String message, String... placeholders) {
        if (sender != null) {
            sender.sendMessage(format(message, placeholders));
        }
    }

    public void send(CommandSender sender, String message, Map<String, String> placeholders) {
        if (sender != null) {
            sender.sendMessage(format(message, placeholders));
        }
    }

    public void sendLines(CommandSender sender, List<String> lines) {
        if (sender == null || lines == null) {
            return;
        }
        for (String line : lines) {
            send(sender, line);
        }
    }

    public boolean isAdventureAvailable() {
        return adventureAvailable;
    }

    public boolean looksLikeMiniMessage(String text) {
        return adventureAvailable && text != null && text.contains("<") && text.contains(">");
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
