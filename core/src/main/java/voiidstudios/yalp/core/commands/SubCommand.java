package voiidstudios.yalp.core.commands;

import java.util.Collections;
import java.util.List;

public interface SubCommand {
    String getName();

    default String getPermission() {
        return "";
    }

    default String getDescription() {
        return "";
    }

    default String getUsage() {
        return getName();
    }

    default boolean isPlayerOnly() {
        return false;
    }

    void execute(CommandContext context);

    default List<String> tabComplete(CommandContext context) {
        return Collections.emptyList();
    }
}
