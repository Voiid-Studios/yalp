# Platform Architecture

YALP is split into platform-neutral contracts and platform implementations.

## Core

`core` contains:

- `YALPApi`
- `YALPProvider`
- component lifecycle and registry
- logger abstraction
- message formatter
- cooldown service
- scheduler interfaces
- config, hooks, and compatibility service interfaces
- platform metadata types
- platform adapter contracts
- translations, commands, resources, JSON, rich text, and YAML definition contracts
- small Java 8 utility helpers

`core` must not import Bukkit, Paper, Folia, Velocity, BungeeCord, or any other server API.

## Bukkit

`bukkit` depends on `core` and implements YALP for Bukkit, Spigot, Paper, Purpur, and Folia.

It owns:

- Bukkit plugin main class
- Bukkit service registration
- Bukkit YAML config implementation
- Bukkit message sending
- Bukkit item/material helpers
- Bukkit inventory GUI helper
- Bukkit hooks detection
- Bukkit/Folia scheduler wrappers
- Bukkit compatibility detection
- Bukkit platform adapter
- Bukkit translation, command, resource, JSON, rich text, and YAML definition implementations

## Future Platforms

Future modules should look like:

```text
velocity/
  pom.xml
  src/main/java/voiidstudios/yalp/velocity/...

bungeecord/
  pom.xml
  src/main/java/voiidstudios/yalp/bungeecord/...
```

Each future platform module should depend on `yalp-core`, implement `YALPApi`, register with `YALPProvider`, and keep platform-specific types out of core.

If a feature only makes sense on Bukkit, it belongs in `bukkit`. If a feature can be abstracted cleanly, define the contract in `core` and implement it per platform.
