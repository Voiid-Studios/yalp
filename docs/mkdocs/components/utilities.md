# Utilities

Core includes a small set of Java 8 helpers:

- `NumberUtils`
- `EnumUtils`
- `Pair`
- `StringUtils`

These are intentionally boring and small.

```java
int amount = NumberUtils.toInt(input, 1);
boolean valid = EnumUtils.contains(MyEnum.class, "VALUE");
String text = StringUtils.replacePlaceholders("{player}", placeholders);
Pair<String, Integer> pair = Pair.of("level", 5);
```
