package voiidstudios.yalp.bukkit.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.logging.YALPLogger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class BukkitItemBuilder implements YALPComponent {
    private final Map<String, LegacyMaterial> legacyMaterials = new HashMap<>();
    private final BukkitMessages messages;
    private final YALPLogger logger;

    public BukkitItemBuilder(BukkitMessages messages, YALPLogger logger) {
        this.messages = messages;
        this.logger = logger;
        registerDefaults();
    }

    @Override
    public String getId() {
        return "items";
    }

    public Builder item(Material material) {
        return new Builder(new ItemStack(material == null ? Material.STONE : material), messages);
    }

    public Builder material(String materialName) {
        Optional<ResolvedMaterial> resolved = resolve(materialName);
        if (resolved.isPresent()) {
            return new Builder(resolved.get().create(1), messages);
        }
        logger.debug("Falling back to STONE for unknown material builder: " + materialName);
        return item(Material.STONE);
    }

    public Optional<ItemStack> create(String materialName) {
        return create(materialName, 1);
    }

    public Optional<ItemStack> create(String materialName, int amount) {
        Optional<ResolvedMaterial> material = resolve(materialName);
        if (!material.isPresent()) {
            logger.debug("Unknown material: " + materialName);
            return Optional.empty();
        }
        return Optional.of(material.get().create(amount));
    }

    public Optional<ResolvedMaterial> resolve(String materialName) {
        if (materialName == null || materialName.trim().isEmpty()) {
            return Optional.empty();
        }
        String normalized = normalize(materialName);
        Material material = Material.matchMaterial(normalized);
        if (material != null) {
            return Optional.of(new ResolvedMaterial(material, (short) 0));
        }
        LegacyMaterial legacyMaterial = legacyMaterials.get(normalized);
        if (legacyMaterial != null) {
            Material legacyBukkitMaterial = Material.matchMaterial(legacyMaterial.materialName);
            if (legacyBukkitMaterial != null) {
                return Optional.of(new ResolvedMaterial(legacyBukkitMaterial, legacyMaterial.data));
            }
        }
        return Optional.empty();
    }

    public void legacy(String modernName, String legacyName, int data) {
        legacyMaterials.put(normalize(modernName), new LegacyMaterial(legacyName, (short) data));
    }

    private void registerDefaults() {
        legacy("PLAYER_HEAD", "SKULL_ITEM", 3);
        legacy("WHITE_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 0);
        legacy("GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 7);
        legacy("LIGHT_GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 8);
        legacy("OAK_SIGN", "SIGN", 0);
        legacy("EXPERIENCE_BOTTLE", "EXP_BOTTLE", 0);
    }

    private String normalize(String materialName) {
        return materialName.trim().toUpperCase(Locale.ROOT);
    }

    public static final class Builder {
        private final ItemStack item;
        private final BukkitMessages messages;

        private Builder(ItemStack item, BukkitMessages messages) {
            this.item = item == null ? new ItemStack(Material.STONE) : item;
            this.messages = messages;
        }

        public Builder amount(int amount) {
            item.setAmount(Math.max(1, Math.min(64, amount)));
            return this;
        }

        @SuppressWarnings("deprecation")
        public Builder durability(short durability) {
            item.setDurability(durability);
            return this;
        }

        public Builder name(String displayName) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(messages.color(displayName));
                item.setItemMeta(meta);
            }
            return this;
        }

        public Builder lore(String... lore) {
            return lore(Arrays.asList(lore));
        }

        public Builder lore(List<String> lore) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setLore(lore == null ? null : messages.color(lore));
                item.setItemMeta(meta);
            }
            return this;
        }

        public Builder enchant(Enchantment enchantment, int level) {
            if (enchantment != null) {
                item.addUnsafeEnchantment(enchantment, level);
            }
            return this;
        }

        public Builder flags(String... flagNames) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || flagNames == null) {
                return this;
            }
            try {
                Class<?> itemFlagClass = Class.forName("org.bukkit.inventory.ItemFlag");
                Method valueOf = itemFlagClass.getMethod("valueOf", String.class);
                Object flagArray = java.lang.reflect.Array.newInstance(itemFlagClass, flagNames.length);
                for (int i = 0; i < flagNames.length; i++) {
                    java.lang.reflect.Array.set(flagArray, i, valueOf.invoke(null, flagNames[i]));
                }
                Method addItemFlags = meta.getClass().getMethod("addItemFlags", flagArray.getClass());
                addItemFlags.invoke(meta, flagArray);
                item.setItemMeta(meta);
            } catch (ReflectiveOperationException ignored) {
            }
            return this;
        }

        public ItemStack build() {
            return item.clone();
        }
    }

    public static final class ResolvedMaterial {
        private final Material material;
        private final short data;

        private ResolvedMaterial(Material material, short data) {
            this.material = material;
            this.data = data;
        }

        @SuppressWarnings("deprecation")
        public ItemStack create(int amount) {
            ItemStack item = new ItemStack(material, Math.max(1, amount));
            item.setDurability(data);
            return item;
        }
    }

    private static final class LegacyMaterial {
        private final String materialName;
        private final short data;

        private LegacyMaterial(String materialName, short data) {
            this.materialName = materialName;
            this.data = data;
        }
    }
}
