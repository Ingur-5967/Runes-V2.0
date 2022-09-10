package ru.solomka.runes.core.gui;

import lombok.Data;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.solomka.runes.core.gui.enums.InventoryType;
import ru.solomka.runes.core.gui.enums.ItemElement;
import ru.solomka.runes.message.Messages;
import ru.solomka.runes.message.utils.Placeholder;
import ru.solomka.runes.message.utils.ListHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

@Data
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(ItemStack item) {
        this.item = item;
        meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        if(name.equals("")) return this;
        meta.setDisplayName(translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder initPlaceholders(ItemElement element, Object obj, Object[] targets, Object[] replacement) {

        Placeholder placeholder = new Placeholder(targets, replacement).addToMap();

        switch (element) {
            case NAME:
                return setName(placeholder.getReplacedString(obj.toString()));

            case LORE:
                return setLore(new ListHandler(((List<String>) obj).stream()
                        .map(Object::toString).collect(Collectors.toList()), targets, replacement)
                        .getReplacedList());
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if(lore.isEmpty()) return this;
        lore.replaceAll(line -> translateAlternateColorCodes('&', line));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack getReplacedItem() {
        return item;
    }
}