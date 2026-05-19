package dev.maxi.yalp.example;

import dev.maxi.yalp.api.YALPApi;
import dev.maxi.yalp.api.YALPProvider;
import dev.maxi.yalp.api.compat.CompatibilityComponent;
import dev.maxi.yalp.api.cooldown.CooldownComponent;
import dev.maxi.yalp.api.gui.YALPGui;
import dev.maxi.yalp.api.hooks.HooksComponent;
import dev.maxi.yalp.api.message.MessagesComponent;
import dev.maxi.yalp.api.scheduler.YALPTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public final class ExampleYALPPlugin extends JavaPlugin {
    private YALPApi yalp;

    @Override
    public void onEnable() {
        yalp = findYalp();
        if (yalp == null) {
            getLogger().severe("YALP is required but was not found. Install YALP and restart the server.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        FileConfiguration config = yalp.configs().load(this, "config.yml");
        String startup = config.getString("startup-message", "{prefix} &aExampleYALPPlugin enabled.");
        yalp.messages().send(Bukkit.getConsoleSender(), startup);
        yalp.scheduler().runLater(() -> yalp.messages().send(Bukkit.getConsoleSender(),
                "{prefix} &7Delayed task ran through YALP scheduler."), 20L);

        if (getCommand("yalpexample") != null) {
            getCommand("yalpexample").setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if ("info".equalsIgnoreCase(args[0])) {
            sendInfo(sender);
            return true;
        }
        if ("cooldown".equalsIgnoreCase(args[0])) {
            runCooldown(sender);
            return true;
        }
        if ("gui".equalsIgnoreCase(args[0])) {
            openGui(sender);
            return true;
        }
        if ("hooks".equalsIgnoreCase(args[0])) {
            sendHooks(sender);
            return true;
        }
        if ("item".equalsIgnoreCase(args[0])) {
            giveItem(sender);
            return true;
        }
        if ("scheduler".equalsIgnoreCase(args[0])) {
            runScheduler(sender);
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        yalp.messages().sendLines(sender, Arrays.asList(
                "{prefix} &7/yalpexample info &8- &fServer compatibility info",
                "{prefix} &7/yalpexample cooldown &8- &fUUID cooldown demo",
                "{prefix} &7/yalpexample gui &8- &fOpen a tiny GUI",
                "{prefix} &7/yalpexample hooks &8- &fShow optional plugin hooks",
                "{prefix} &7/yalpexample item &8- &fGet a version-safe item",
                "{prefix} &7/yalpexample scheduler &8- &fRun scheduler examples"
        ));
    }

    private void sendInfo(CommandSender sender) {
        CompatibilityComponent compatibility = yalp.compatibility();
        yalp.messages().sendLines(sender, Arrays.asList(
                "{prefix} &eSoftware: &f" + compatibility.getServerSoftware(),
                "{prefix} &eMinecraft: &f" + compatibility.getMinecraftVersion(),
                "{prefix} &eLegacy: &f" + compatibility.isLegacy(),
                "{prefix} &ePaper: &f" + compatibility.isPaper(),
                "{prefix} &eFolia: &f" + compatibility.isFolia(),
                "{prefix} &eAdventure: &f" + compatibility.supportsAdventure(),
                "{prefix} &eHex colors: &f" + compatibility.supportsHexColors()
        ));
    }

    private void runCooldown(CommandSender sender) {
        MessagesComponent messages = yalp.messages();
        CooldownComponent cooldowns = yalp.cooldowns();
        UUID id = sender instanceof Player ? ((Player) sender).getUniqueId() : new UUID(0L, sender.getName().hashCode());

        if (cooldowns.hasCooldown(id, "yalpexample")) {
            messages.send(sender, "{prefix} &cYou have {time} remaining.",
                    "time", cooldowns.formatRemaining(id, "yalpexample"));
            return;
        }

        cooldowns.setCooldown(id, "yalpexample", Duration.ofSeconds(30));
        messages.send(sender, "{prefix} &aCooldown started for 30 seconds.");
    }

    private void openGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            yalp.messages().send(sender, "{prefix} &cOnly players can open the GUI.");
            return;
        }

        Player player = (Player) sender;
        ItemStack button = yalp.items().material("DIAMOND")
                .name("&aClick me")
                .lore("&7A tiny YALP GUI button.")
                .build();
        YALPGui gui = yalp.guis()
                .create("&8Example Menu", 27)
                .button(13, button, event -> event.getWhoClicked().sendMessage(
                        yalp.messages().color("&aClicked through YALP GUI.")))
                .build();
        gui.open(player);
    }

    private void sendHooks(CommandSender sender) {
        HooksComponent hooks = yalp.hooks();
        yalp.messages().sendLines(sender, Arrays.asList(
                "{prefix} &ePlaceholderAPI: &f" + hooks.hasPlaceholderAPI(),
                "{prefix} &eVault: &f" + hooks.hasVault(),
                "{prefix} &eLuckPerms: &f" + hooks.hasLuckPerms(),
                "{prefix} &eProtocolLib: &f" + hooks.hasProtocolLib(),
                "{prefix} &eItemsAdder: &f" + hooks.hasItemsAdder(),
                "{prefix} &eWorldGuard: &f" + hooks.hasWorldGuard(),
                "{prefix} &eWorldEdit: &f" + hooks.hasWorldEdit()
        ));
    }

    private void giveItem(CommandSender sender) {
        if (!(sender instanceof Player)) {
            yalp.messages().send(sender, "{prefix} &cOnly players can receive items.");
            return;
        }
        Player player = (Player) sender;
        Optional<ItemStack> item = yalp.items().create("GRAY_STAINED_GLASS_PANE", 1);
        ItemStack display = item.orElseGet(() -> new ItemStack(Material.STONE));
        display = yalp.items().item(display.getType())
                .durability(display.getDurability())
                .name("&aVersion-safe example item")
                .lore("&7Resolved by YALP.")
                .build();
        player.getInventory().addItem(display);
        yalp.messages().send(player, "{prefix} &aItem added to your inventory.");
    }

    private void runScheduler(CommandSender sender) {
        yalp.scheduler().runAsync(() -> yalp.logger().debug("Async example task ran."));
        YALPTask delayed = yalp.scheduler().runLater(() ->
                yalp.messages().send(sender, "{prefix} &aDelayed task finished."), 20L);
        if (sender instanceof Player) {
            yalp.scheduler().runAtEntity((Player) sender, () ->
                    yalp.messages().send(sender, "{prefix} &aEntity-safe task ran."));
        }
        yalp.messages().send(sender, "{prefix} &7Scheduled tasks. Cancel handle class: &f{type}",
                "type", delayed.getClass().getSimpleName());
    }

    private YALPApi findYalp() {
        if (YALPProvider.isAvailable()) {
            return YALPProvider.get();
        }
        RegisteredServiceProvider<YALPApi> registration = Bukkit.getServicesManager().getRegistration(YALPApi.class);
        return registration == null ? null : registration.getProvider();
    }
}
