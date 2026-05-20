package voiidstudios.yalp.core.commands;

import java.util.Arrays;
import java.util.List;

public interface CommandContext {
    Object sender();

    String label();

    String[] args();

    default List<String> arguments() {
        return Arrays.asList(args());
    }
}
