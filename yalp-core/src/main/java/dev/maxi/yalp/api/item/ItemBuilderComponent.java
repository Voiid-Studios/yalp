package dev.maxi.yalp.api.item;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import dev.maxi.yalp.api.message.MessagesComponent;
import dev.maxi.yalp.api.platform.ServerCompatibility;
import dev.maxi.yalp.api.util.YALPLogger;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ItemBuilderComponent implements YALPComponent {
    private final Map<String, LegacyMaterial> legacyMaterials = new HashMap<>();
    private YALPLogger logger;
    private MessagesComponent messages;
    private boolean legacy;

    @Override
    public String getId() {
        return "itembuilder";
    }

    @Override
    public List<String> getDependencies() {
        return Collections.singletonList("messages");
    }

    @Override
    public void onLoad(ComponentContext context) {
        this.logger = context.getLogger();
        this.messages = context.getApi().messages();
        ServerCompatibility compatibility = context.getCompatibility();
        this.legacy = compatibility.isLegacy();
        registerDefaults();
    }

    public Builder item(Material material) {
        return new Builder(material, messages);
    }

    public Optional<ItemStack> create(String materialName) {
        return create(materialName, 1);
    }

    public Optional<ItemStack> create(String materialName, int amount) {
        Optional<ResolvedMaterial> material = resolve(materialName);
        if (!material.isPresent()) {
            debug("Unknown material: " + materialName);
            return Optional.empty();
        }
        return Optional.of(material.get().create(amount));
    }

    public Builder material(String materialName) {
        Optional<ResolvedMaterial> resolved = resolve(materialName);
        if (resolved.isPresent()) {
            ResolvedMaterial material = resolved.get();
            return new Builder(material.create(1), messages);
        }
        debug("Falling back to STONE for unknown material builder: " + materialName);
        return item(Material.STONE);
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

    private void registerDefaults() {
        legacy("PLAYER_HEAD", "SKULL_ITEM", 3);
        legacy("WHITE_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 0);
        legacy("GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 7);
        legacy("LIGHT_GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 8);
        legacy("OAK_SIGN", "SIGN", 0);
        legacy("EXPERIENCE_BOTTLE", "EXP_BOTTLE", 0);
    }

    public void legacy(String modernName, String legacyName, int data) {
        legacyMaterials.put(normalize(modernName), new LegacyMaterial(legacyName, (short) data));
    }

    private String normalize(String materialName) {
        return materialName.trim().toUpperCase(Locale.ROOT);
    }

    private void debug(String message) {
        if (logger != null) {
            logger.debug(message);
        }
    }

    public static final class Builder {
        private final ItemStack item;
        private final MessagesComponent messages;

        private Builder(Material material, MessagesComponent messages) {
            this.item = new ItemStack(material == null ? Material.STONE : material);
            this.messages = messages;
        }

        private Builder(ItemStack item, MessagesComponent messages) {
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
                meta.setDisplayName(messages == null ? displayName : messages.color(displayName));
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
                meta.setLore(lore == null ? null : color(lore));
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
                // Item flags are not available on some legacy-compatible APIs.
            }
            return this;
        }

        public ItemStack build() {
            return item.clone();
        }

        private List<String> color(List<String> lore) {
            if (messages != null) {
                return messages.color(lore);
            }
            return new ArrayList<>(lore);
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

        public Material getMaterial() {
            return material;
        }

        public short getData() {
            return data;
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
