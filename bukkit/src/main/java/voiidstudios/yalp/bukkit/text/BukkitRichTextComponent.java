package voiidstudios.yalp.bukkit.text;

import org.bukkit.command.CommandSender;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.text.RichText;
import voiidstudios.yalp.core.text.RichTextBuilder;
import voiidstudios.yalp.core.text.RichTextService;

public final class BukkitRichTextComponent implements RichTextService, YALPComponent {
    private static final String[] RAINBOW = {"&c", "&6", "&e", "&a", "&b", "&9", "&d"};
    private final BukkitMessages messages;

    public BukkitRichTextComponent(BukkitMessages messages) {
        this.messages = messages;
    }

    @Override
    public String getId() { return "rich-text"; }

    @Override
    public RichTextBuilder text(String text) { return new RichTextBuilder().text(text); }

    @Override
    public RichText rainbow(String text) {
        RichTextBuilder builder = new RichTextBuilder();
        if (text == null) return builder.text("").build();
        for (int i = 0; i < text.length(); i++) {
            builder.append(new RichTextBuilder().text(String.valueOf(text.charAt(i))).color(RAINBOW[i % RAINBOW.length]).build());
        }
        return builder.build();
    }

    @Override
    public String toPlain(RichText text) {
        if (text == null) return "";
        StringBuilder builder = new StringBuilder();
        builder.append(text.getColor() == null ? "" : text.getColor()).append(text.getText() == null ? "" : text.getText());
        for (RichText child : text.getChildren()) {
            builder.append(toPlain(child));
        }
        return messages.color(builder.toString());
    }

    @Override
    public void send(Object receiver, RichText text) {
        if (receiver instanceof CommandSender) {
            ((CommandSender) receiver).sendMessage(toPlain(text));
        }
    }
}
