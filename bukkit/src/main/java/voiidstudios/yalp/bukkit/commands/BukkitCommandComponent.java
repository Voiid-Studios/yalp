package voiidstudios.yalp.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.core.commands.CommandBuilder;
import voiidstudios.yalp.core.commands.CommandContext;
import voiidstudios.yalp.core.commands.CommandService;
import voiidstudios.yalp.core.commands.SubCommand;
import voiidstudios.yalp.core.component.YALPComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class BukkitCommandComponent implements CommandService, YALPComponent {
    private final BukkitMessages messages;

    public BukkitCommandComponent(BukkitMessages messages) {
        this.messages = messages;
    }

    @Override
    public String getId() { return "commands"; }

    @Override
    public CommandBuilder create(String rootCommand) { return new Builder(rootCommand, messages); }

    private static final class Builder implements CommandBuilder, org.bukkit.command.CommandExecutor, TabCompleter {
        private final String root;
        private final BukkitMessages messages;
        private final Map<String, SubCommand> subcommands = new LinkedHashMap<>();
        private String permission = "";
        private String description = "";

        private Builder(String root, BukkitMessages messages) {
            this.root = root;
            this.messages = messages;
        }

        @Override
        public CommandBuilder description(String description) { this.description = description; return this; }

        @Override
        public CommandBuilder permission(String permission) { this.permission = permission; return this; }

        @Override
        public CommandBuilder subcommand(SubCommand subCommand) {
            subcommands.put(subCommand.getName().toLowerCase(Locale.ROOT), subCommand);
            return this;
        }

        @Override
        public CommandBuilder subcommand(String name, CommandBuilder.CommandExecutor executor) {
            return subcommand(new SimpleSubCommand(name, executor));
        }

        @Override
        public void register(Object owner) {
            JavaPlugin plugin = (JavaPlugin) owner;
            PluginCommand command = plugin.getCommand(root);
            if (command == null) {
                throw new IllegalStateException("Command /" + root + " must be declared in plugin.yml for legacy Bukkit safety.");
            }
            command.setDescription(description);
            command.setExecutor(this);
            command.setTabCompleter(this);
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!permission.isEmpty() && !sender.hasPermission(permission)) {
                messages.send(sender, "{prefix} &cYou do not have permission.");
                return true;
            }
            if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
                help(sender, label);
                return true;
            }
            SubCommand sub = subcommands.get(args[0].toLowerCase(Locale.ROOT));
            if (sub == null) {
                messages.send(sender, "{prefix} &cUnknown subcommand. Try /" + label + " help.");
                return true;
            }
            if (sub.isPlayerOnly() && !(sender instanceof Player)) {
                messages.send(sender, "{prefix} &cOnly players can use this command.");
                return true;
            }
            if (!sub.getPermission().isEmpty() && !sender.hasPermission(sub.getPermission())) {
                messages.send(sender, "{prefix} &cYou do not have permission.");
                return true;
            }
            String[] childArgs = java.util.Arrays.copyOfRange(args, 1, args.length);
            sub.execute(new BukkitCommandContext(sender, label, childArgs));
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> results = new ArrayList<>();
            if (args.length == 1) {
                for (String name : subcommands.keySet()) {
                    if (name.startsWith(args[0].toLowerCase(Locale.ROOT))) results.add(name);
                }
            }
            return results;
        }

        private void help(CommandSender sender, String label) {
            messages.send(sender, "{prefix} &e/" + label + " help");
            for (SubCommand sub : subcommands.values()) {
                messages.send(sender, "&7/" + label + " " + sub.getUsage() + " &8- &f" + sub.getDescription());
            }
        }
    }

    private static final class BukkitCommandContext implements CommandContext {
        private final CommandSender sender;
        private final String label;
        private final String[] args;
        private BukkitCommandContext(CommandSender sender, String label, String[] args) {
            this.sender = sender; this.label = label; this.args = args;
        }
        @Override public Object sender() { return sender; }
        public CommandSender bukkitSender() { return sender; }
        @Override public String label() { return label; }
        @Override public String[] args() { return args; }
    }

    private static final class SimpleSubCommand implements SubCommand {
        private final String name;
        private final CommandBuilder.CommandExecutor executor;
        private SimpleSubCommand(String name, CommandBuilder.CommandExecutor executor) { this.name = name; this.executor = executor; }
        @Override public String getName() { return name; }
        @Override public void execute(CommandContext context) { executor.execute(context); }
    }
}
