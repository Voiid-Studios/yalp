package voiidstudios.yalp.core.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RichText {
    private final String text;
    private final String color;
    private final boolean bold;
    private final boolean italic;
    private final boolean underline;
    private final boolean strikethrough;
    private final boolean obfuscated;
    private final String hover;
    private final ClickAction clickAction;
    private final String clickValue;
    private final List<RichText> children;

    RichText(String text, String color, boolean bold, boolean italic, boolean underline, boolean strikethrough,
             boolean obfuscated, String hover, ClickAction clickAction, String clickValue, List<RichText> children) {
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.hover = hover;
        this.clickAction = clickAction;
        this.clickValue = clickValue;
        this.children = children == null ? new ArrayList<>() : new ArrayList<>(children);
    }

    public String getText() { return text; }
    public String getColor() { return color; }
    public boolean isBold() { return bold; }
    public boolean isItalic() { return italic; }
    public boolean isUnderline() { return underline; }
    public boolean isStrikethrough() { return strikethrough; }
    public boolean isObfuscated() { return obfuscated; }
    public String getHover() { return hover; }
    public ClickAction getClickAction() { return clickAction; }
    public String getClickValue() { return clickValue; }
    public List<RichText> getChildren() { return Collections.unmodifiableList(children); }
}
