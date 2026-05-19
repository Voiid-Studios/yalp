package dev.maxi.yalp.api.item;

import dev.maxi.yalp.api.component.YALPComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ItemBuilderComponent implements YALPComponent {
    @Override
    public String getId() {
        return "itembuilder";
    }

    public Builder item(Material material) {
        return new Builder(material);
    }

    public static final class Builder {
        private final ItemStack item;

        private Builder(Material material) {
            this.item = new ItemStack(material == null ? Material.STONE : material);
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
                meta.setDisplayName(displayName);
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
                meta.setLore(lore == null ? null : new ArrayList<>(lore));
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
    }
}
