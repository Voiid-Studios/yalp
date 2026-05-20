# Hooks Component

`HooksComponent` avoids copy-pasting optional plugin detection.

```java
if (yalp.hooks().hasPlaceholderAPI()) {
    yalp.logger().info("PlaceholderAPI detected.");
}
```

Built-in checks:

```java
hooks.hasPlaceholderAPI();
hooks.hasVault();
hooks.hasLuckPerms();
hooks.hasProtocolLib();
hooks.hasItemsAdder();
hooks.hasWorldGuard();
hooks.hasWorldEdit();
hooks.getPlugin("SomePlugin");
hooks.isPluginEnabled("SomePlugin");
```

YALP does not depend on these plugins. Actual API integrations should live in optional modules later.

