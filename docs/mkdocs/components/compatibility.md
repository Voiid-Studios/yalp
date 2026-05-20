# Compatibility Component

`CompatibilityComponent` provides best-effort runtime checks. These checks are helpers, not a replacement for defensive coding.

```java
if (yalp.compatibility().isFolia()) {
    yalp.logger().info("Folia detected.");
}
```

Useful methods:

```java
compatibility.getServerSoftware();
compatibility.getMinecraftVersion();
compatibility.isBukkit();
compatibility.isSpigot();
compatibility.isPaper();
compatibility.isFolia();
compatibility.isPurpur();
compatibility.isLegacy();
compatibility.isModern();
compatibility.supportsAdventure();
compatibility.supportsItemFlags();
compatibility.supportsHexColors();
compatibility.supportsPlayerHeadsModernApi();
```

YALP does not compile against Paper, Folia, Purpur, or Adventure. Detection uses safe class checks and server strings.

