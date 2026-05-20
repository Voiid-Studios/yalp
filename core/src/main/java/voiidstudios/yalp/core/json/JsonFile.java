package voiidstudios.yalp.core.json;

public interface JsonFile<T> {
    String getId();

    T get();

    boolean load();

    boolean save();

    boolean reload();

    boolean resetFromDefault();
}
