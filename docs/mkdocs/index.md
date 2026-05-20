# YALP

**Yet Another Library Plugin**

> yup, another one ¯\\_(ツ)_/¯

YALP is a lightweight shared utility library for Minecraft plugins. It is split into a platform-independent `core` module and a Bukkit-family implementation module.

Technically it is a plugin. Emotionally, it's a dependency.

## Quick Links

- [Getting Started](getting-started.md)
- [Dependency Setup](dependency-setup.md)
- [Platform Architecture](platform-architecture.md)
- [Components](components/logger.md)
- [JavaDocs](javadocs.md)

## Build

```bash
mvn clean package
```

Artifacts:

- `core/target/yalp-core-VERSION.jar`
- `bukkit/target/YALP-VERSION.jar`
- `examples/bukkit-example/target/yalp-bukkit-example-VERSION.jar`

Replace `VERSION` with the latest GitHub release tag.

## FAQ

**Is YALP a plugin?**  
Technically yes. Emotionally, it's a dependency.

**Why is the logo just ¯\\_(ツ)_/¯ ?**  
why not

**Can I use YALP in my own plugins?**  
yup

**Is this the Lua YALP?**  
No. This one is for Minecraft.

**Will YALP auto-install itself?**  
No.

