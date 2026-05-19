# Logger Component

`LoggerComponent` wraps YALP console output helpers.

```java
yalp.logger().info("yup");
yalp.logger().success("Component enabled.");
yalp.logger().warn("Something looks odd.");
yalp.logger().error("Something broke.");
yalp.logger().debug("Only prints when debug is enabled.");
yalp.logger().component("messages", "Prefix loaded.");
```

Debug mode is read from YALP's `config.yml`:

```yaml
debug: false
```

The logger keeps console output readable and intentionally avoids noisy color tricks.

