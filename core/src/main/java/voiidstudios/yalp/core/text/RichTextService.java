package voiidstudios.yalp.core.text;

public interface RichTextService {
    RichTextBuilder text(String text);

    RichText rainbow(String text);

    String toPlain(RichText text);

    void send(Object receiver, RichText text);
}
