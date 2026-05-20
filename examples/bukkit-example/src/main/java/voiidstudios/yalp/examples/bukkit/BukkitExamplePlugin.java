package voiidstudios.yalp.examples.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.bukkit.api.BukkitYALPApi;
import voiidstudios.yalp.bukkit.gui.YALPGui;
import voiidstudios.yalp.core.api.YALPApi;
import voiidstudios.yalp.core.api.YALPProvider;
import voiidstudios.yalp.core.commands.CommandContext;
import voiidstudios.yalp.core.commands.SubCommand;
import voiidstudios.yalp.core.definitions.YamlDefinition;
import voiidstudios.yalp.core.definitions.YamlDefinitionRegistry;
import voiidstudios.yalp.core.json.JsonFile;
import voiidstudios.yalp.core.scheduler.ScheduledTask;
import voiidstudios.yalp.core.text.RichText;
import voiidstudios.yalp.core.translation.TranslationContext;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public final class BukkitExamplePlugin extends JavaPlugin {
    private BukkitYALPApi yalp;
    private TranslationContext translations;
    private YamlDefinitionRegistry definitions;
    private JsonFile<ExampleSettings> settings;

    @Override
    public void onEnable() {
        yalp = findYalp();
        if (yalp == null) {
            getLogger().severe("YALP is required but was not found. Install YALP and restart the server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        FileConfiguration config = yalp.configs().load(this, "config.yml");
        yalp.resources().copyDefault(this, "lang/en_US.yml");
        yalp.resources().copyDefault(this, "examples/rewards.yml");
        translations = yalp.translations().createContext(this, "lang", "en_US", config.getString("language", "en_US"));
        definitions = yalp.yamlFolders().load(this, "examples", "rewards");
        settings = yalp.jsonFiles().register(this, "settings", new File(getDataFolder(), "settings.json"), ExampleSettings.class, ExampleSettings::new);
        settings.load();

        registerCommands();
        yalp.messages().send(Bukkit.getConsoleSender(), config.getString("startup-message", "{prefix} &aExample plugin enabled using YALP."));
    }

    private void registerCommands() {
        yalp.commands().create("yalpexample")
                .description("Example YALP command")
                .subcommand(command("info", "Shows platform info", this::info))
                .subcommand(command("logger", "Prints logger examples", this::loggerDemo))
                .subcommand(command("lang", "Shows or reloads translations", this::lang))
                .subcommand(command("definitions", "Works with YAML definitions", this::definitions))
                .subcommand(command("resource", "Reads/copies a bundled resource", this::resource))
                .subcommand(command("json", "Works with JSON settings", this::json))
                .subcommand(command("text", "Sends clickable-style rich text fallback", this::text))
                .subcommand(command("rainbow", "Sends rainbow text", this::rainbow))
                .subcommand(command("cooldown", "Demonstrates cooldowns", this::cooldown))
                .subcommand(command("gui", "Opens a GUI", this::gui))
                .subcommand(command("hooks", "Shows hook detection", this::hooks))
                .subcommand(command("item", "Gives a version-safe item", this::item))
                .subcommand(command("scheduler", "Runs scheduler examples", this::scheduler))
                .register(this);
    }

    private SubCommand command(String name, String description, java.util.function.Consumer<CommandContext> executor) {
        return new SubCommand() {
            @Override public String getName() { return name; }
            @Override public String getDescription() { return description; }
            @Override public void execute(CommandContext context) { executor.accept(context); }
        };
    }

    private void info(CommandContext context) {
        CommandSender sender = sender(context);
        yalp.messages().sendLines(sender, Arrays.asList(
                "{prefix} &ePlatform: &f" + yalp.platform().getType(),
                "{prefix} &eSoftware: &f" + yalp.compatibility().getServerSoftware(),
                "{prefix} &eMinecraft: &f" + yalp.compatibility().getMinecraftVersion(),
                "{prefix} &eLegacy: &f" + yalp.compatibility().isLegacy(),
                "{prefix} &eFolia: &f" + yalp.compatibility().isFolia()
        ));
    }

    private void loggerDemo(CommandContext context) {
        yalp.logger().info("Logger info example.");
        yalp.logger().success("Logger success example.");
        yalp.logger().warning("Logger warning example.");
        yalp.logger().process("Logger process example.");
        yalp.logger().debug("Logger debug example. Enable debug in YALP config to see this.");
        yalp.messages().send(sender(context), "{prefix} &aLogger examples printed to console.");
    }

    private void lang(CommandContext context) {
        if (context.args().length > 0 && "reload".equalsIgnoreCase(context.args()[0])) {
            translations.reload();
            yalp.messages().send(sender(context), translations.get("commands.reload"));
            return;
        }
        yalp.messages().send(sender(context), translations.get("commands.cooldown", "time", "5s"));
    }

    private void definitions(CommandContext context) {
        CommandSender sender = sender(context);
        String[] args = context.args();
        if (args.length == 0 || "list".equalsIgnoreCase(args[0])) {
            yalp.messages().send(sender, "{prefix} &eDefinitions: &f" + definitions.getIds());
            return;
        }
        if (args.length >= 2 && "info".equalsIgnoreCase(args[0])) {
            Optional<YamlDefinition> definition = definitions.get(args[1]);
            yalp.messages().send(sender, definition.map(value -> "{prefix} &e" + value.getId() + ": &f" + value.getName() + " &7enabled=" + value.isEnabled())
                    .orElse("{prefix} &cUnknown definition."));
            return;
        }
        if (args.length >= 2 && "toggle".equalsIgnoreCase(args[0])) {
            Optional<YamlDefinition> definition = definitions.get(args[1]);
            if (definition.isPresent()) {
                definitions.setEnabled(args[1], !definition.get().isEnabled());
                definitions.save(args[1]);
                yalp.messages().send(sender, "{prefix} &aToggled " + args[1] + ".");
            }
            return;
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            definitions.reload();
            yalp.messages().send(sender, "{prefix} &aDefinitions reloaded.");
        }
    }

    private void resource(CommandContext context) {
        boolean copied = yalp.resources().copyDefault(this, "defaults/example.txt");
        String text = yalp.resources().readText(this, "defaults/example.txt").orElse("missing");
        yalp.messages().send(sender(context), "{prefix} &aResource copied={copied}, text={text}", "copied", String.valueOf(copied), "text", text);
    }

    private void json(CommandContext context) {
        String action = context.args().length == 0 ? "show" : context.args()[0];
        if ("save".equalsIgnoreCase(action)) {
            settings.get().uses++;
            settings.save();
        } else if ("reload".equalsIgnoreCase(action)) {
            settings.reload();
        }
        yalp.messages().send(sender(context), "{prefix} &eJSON settings: &fmessage={message}, uses={uses}",
                "message", settings.get().message, "uses", String.valueOf(settings.get().uses));
    }

    private void text(CommandContext context) {
        RichText text = yalp.richText().text("Click-ish YALP text").color("&a").hover("&7Runs /help").clickRunCommand("/help").build();
        yalp.richText().send(sender(context), text);
    }

    private void rainbow(CommandContext context) {
        yalp.richText().send(sender(context), yalp.richText().rainbow("YALP moment"));
    }

    private void cooldown(CommandContext context) {
        CommandSender sender = sender(context);
        UUID id = sender instanceof Player ? ((Player) sender).getUniqueId() : new UUID(0L, sender.getName().hashCode());
        if (yalp.cooldowns().hasCooldown(id, "example")) {
            yalp.messages().send(sender, "{prefix} &cWait {time}.", "time", yalp.cooldowns().formatRemaining(id, "example"));
            return;
        }
        yalp.cooldowns().setCooldown(id, "example", Duration.ofSeconds(30));
        yalp.messages().send(sender, "{prefix} &aCooldown started.");
    }

    private void gui(CommandContext context) {
        if (!(sender(context) instanceof Player)) {
            yalp.messages().send(sender(context), "{prefix} &cOnly players can open the GUI.");
            return;
        }
        Player player = (Player) sender(context);
        YALPGui gui = yalp.guis().create("&8Example Menu", 27)
                .button(13, yalp.items().material("DIAMOND").name("&aClick me").build(),
                        event -> event.getWhoClicked().sendMessage(yalp.messages().color("&aClicked.")))
                .build();
        gui.open(player);
    }

    private void hooks(CommandContext context) {
        yalp.messages().sendLines(sender(context), Arrays.asList(
                "{prefix} &ePlaceholderAPI: &f" + yalp.hooks().hasPlaceholderAPI(),
                "{prefix} &eVault: &f" + yalp.hooks().hasVault(),
                "{prefix} &eLuckPerms: &f" + yalp.hooks().hasLuckPerms()
        ));
    }

    private void item(CommandContext context) {
        if (!(sender(context) instanceof Player)) return;
        Player player = (Player) sender(context);
        ItemStack base = yalp.items().create("GRAY_STAINED_GLASS_PANE").orElse(new ItemStack(Material.STONE));
        player.getInventory().addItem(yalp.items().item(base.getType()).durability(base.getDurability()).name("&aVersion-safe item").build());
    }

    private void scheduler(CommandContext context) {
        CommandSender sender = sender(context);
        yalp.scheduler().runAsync(() -> yalp.logger().debug("Async example task ran."));
        ScheduledTask task = yalp.scheduler().runLater(() -> yalp.messages().send(sender, "{prefix} &aDelayed task finished."), 20L);
        yalp.messages().send(sender, "{prefix} &7Scheduled task: &f" + task.getClass().getSimpleName());
    }

    private CommandSender sender(CommandContext context) {
        return (CommandSender) context.sender();
    }

    private BukkitYALPApi findYalp() {
        if (YALPProvider.isAvailable() && YALPProvider.get() instanceof BukkitYALPApi) {
            return (BukkitYALPApi) YALPProvider.get();
        }
        RegisteredServiceProvider<BukkitYALPApi> registration = Bukkit.getServicesManager().getRegistration(BukkitYALPApi.class);
        return registration == null ? null : registration.getProvider();
    }

    public static final class ExampleSettings {
        public String message = "hello json";
        public int uses = 0;
    }
}
