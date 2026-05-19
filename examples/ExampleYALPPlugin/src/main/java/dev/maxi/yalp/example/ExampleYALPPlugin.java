package dev.maxi.yalp.example;

import dev.maxi.yalp.api.YALPApi;
import dev.maxi.yalp.api.YALPProvider;
import dev.maxi.yalp.api.cooldown.CooldownComponent;
import dev.maxi.yalp.api.message.MessagesComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Collections;
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
        MessagesComponent messages = yalp.messages();
        CooldownComponent cooldowns = yalp.cooldowns();

        UUID id = sender instanceof Player ? ((Player) sender).getUniqueId() : new UUID(0L, sender.getName().hashCode());
        if (cooldowns.hasCooldown(id, "yalpexample")) {
            long seconds = Math.max(1L, cooldowns.getRemaining(id, "yalpexample").getSeconds());
            messages.send(sender, messages.format("{prefix} &cSlow down. Try again in {seconds}s.",
                    Collections.singletonMap("seconds", String.valueOf(seconds))));
            return true;
        }

        cooldowns.setCooldown(id, "yalpexample", Duration.ofSeconds(5));
        messages.send(sender, "{prefix} &aYALP is available and the example command works.");
        return true;
    }

    private YALPApi findYalp() {
        if (YALPProvider.isAvailable()) {
            return YALPProvider.get();
        }
        RegisteredServiceProvider<YALPApi> registration = Bukkit.getServicesManager().getRegistration(YALPApi.class);
        return registration == null ? null : registration.getProvider();
    }
}
