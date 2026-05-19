package voiidstudios.yalp.bukkit.gui;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class GuiBuilder {
    private final String title;
    private final int size;
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Integer, GuiClickHandler> handlers = new HashMap<>();
    private boolean cancelClicks = true;

    GuiBuilder(String title, int size) {
        this.title = title;
        this.size = size;
    }

    public GuiBuilder item(int slot, ItemStack item) {
        if (item != null) {
            items.put(slot, item);
        }
        return this;
    }

    public GuiBuilder button(int slot, ItemStack item, GuiClickHandler handler) {
        item(slot, item);
        if (handler != null) {
            handlers.put(slot, handler);
        }
        return this;
    }

    public GuiBuilder cancelClicks(boolean cancelClicks) {
        this.cancelClicks = cancelClicks;
        return this;
    }

    public YALPGui build() {
        return new YALPGui(title, size, items, handlers, cancelClicks);
    }
}
