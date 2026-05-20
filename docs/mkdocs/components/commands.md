# Commands

YALP includes a small Bukkit command wrapper. Commands should still be declared in `plugin.yml` for legacy Bukkit safety.

```java
yalp.commands().create("yalpexample")
        .description("Example YALP command")
        .subcommand("info", context -> {
            CommandSender sender = (CommandSender) context.sender();
            yalp.messages().send(sender, "{prefix} &aInfo.");
        })
        .register(plugin);
```

It supports subcommands, permissions, player-only checks in `SubCommand`, automatic help, and tab completion for subcommand names.

