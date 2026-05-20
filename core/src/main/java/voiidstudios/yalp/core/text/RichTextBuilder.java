package voiidstudios.yalp.core.text;

import java.util.ArrayList;
import java.util.List;

public final class RichTextBuilder {
    private String text = "";
    private String color = "";
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikethrough;
    private boolean obfuscated;
    private String hover;
    private ClickAction clickAction;
    private String clickValue;
    private final List<RichText> children = new ArrayList<>();

    public RichTextBuilder text(String text) { this.text = text; return this; }
    public RichTextBuilder color(String color) { this.color = color; return this; }
    public RichTextBuilder bold() { this.bold = true; return this; }
    public RichTextBuilder italic() { this.italic = true; return this; }
    public RichTextBuilder underline() { this.underline = true; return this; }
    public RichTextBuilder strikethrough() { this.strikethrough = true; return this; }
    public RichTextBuilder obfuscated() { this.obfuscated = true; return this; }
    public RichTextBuilder hover(String hover) { this.hover = hover; return this; }
    public RichTextBuilder clickRunCommand(String command) { this.clickAction = ClickAction.RUN_COMMAND; this.clickValue = command; return this; }
    public RichTextBuilder clickSuggestCommand(String command) { this.clickAction = ClickAction.SUGGEST_COMMAND; this.clickValue = command; return this; }
    public RichTextBuilder clickOpenUrl(String url) { this.clickAction = ClickAction.OPEN_URL; this.clickValue = url; return this; }
    public RichTextBuilder clickCopy(String value) { this.clickAction = ClickAction.COPY_TO_CLIPBOARD; this.clickValue = value; return this; }
    public RichTextBuilder append(RichText child) { if (child != null) children.add(child); return this; }

    public RichText build() {
        return new RichText(text, color, bold, italic, underline, strikethrough, obfuscated, hover, clickAction, clickValue, children);
    }
}
