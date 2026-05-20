package voiidstudios.yalp.core.commands;

public interface CommandBuilder {
    CommandBuilder description(String description);

    CommandBuilder permission(String permission);

    CommandBuilder subcommand(SubCommand subCommand);

    CommandBuilder subcommand(String name, CommandExecutor executor);

    void register(Object owner);

    interface CommandExecutor {
        void execute(CommandContext context);
    }
}
