package ru.solomka.runes.core.gui;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.config.files.FileUtils;
import ru.solomka.runes.core.gui.enums.InventoryType;
import ru.solomka.runes.core.gui.enums.ItemElement;
import ru.solomka.runes.core.permission.UserManager;
import ru.solomka.runes.core.permission.enums.GroupSource;

@Data
@NoArgsConstructor
public class GUIManager {

    private Player p;
    private ItemStack background;
    private InventoryType inventoryType;

    private UserManager permManager;

    public GUIManager(@NotNull Player p, InventoryType inventoryType, ItemStack background) {
        this.p = p;
        this.inventoryType = inventoryType;
        this.background = background;
        permManager = new UserManager(p);
        p.openInventory(initInventory());
    }


    private Inventory initInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, inventoryType.getCurrentFile().getString("Title"));

        String targetPath;

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(background).setName(" ").getItem());

        if (inventoryType != InventoryType.MAIN) {
            inventory.setItem(0, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4));

            if (inventoryType.getCurrentFile().getBoolean("Back.Enable")) {
                String currentItemString = inventoryType.getCurrentFile().getString("Back.Item");

                byte durability = currentItemString.contains(";") ? Byte.parseByte(currentItemString.split(";")[1]) : 0;

                inventory.setItem(inventoryType.getCurrentFile().getInt("Back.Slot"),
                        new ItemBuilder(new ItemStack(Material.getMaterial(currentItemString.split(";")[0]), 1, durability))
                                .setName(inventoryType.getCurrentFile().getString("Back.Name")).getReplacedItem()
                );
            }
        }

        for (int slot = 0; slot < GUIUtils.getCountRecordsItems(inventoryType, permManager.getGroup(), inventoryType.getCurrentFile()); slot++) {

            targetPath = (inventoryType == InventoryType.MAIN || inventoryType == InventoryType.POTIONS)
                    ? "Slots." + slot : "Slots." + permManager.getGroup().name() + "." + slot;

            if(!inventoryType.getCurrentFile().getBoolean(targetPath + ".Enable") && inventoryType != InventoryType.MAIN) continue;

            ItemStack targetItem = new ItemStack(Material.getMaterial(inventoryType.getCurrentFile().getString(targetPath + ".ItemView")));

            inventory.setItem(inventoryType.getCurrentFile().getInt(targetPath + ".Slot"),
                    new ItemBuilder(targetItem)
                            .setName(inventoryType.getCurrentFile().getString(targetPath + ".Name"))
                            .initPlaceholders(
                                    ItemElement.LORE, inventoryType == InventoryType.MAIN
                                                      ? inventoryType.getCurrentFile().getStringList(targetPath + ".Lore")
                                                      : inventoryType.getCurrentFile().getStringList("Menu.StandardLore"),
                                    new Object[]{
                                            "{level}", "{max_level}", "{effects}", "{character}",
                                            "{attribute}", "{ability}", "{line_one}", "{character}",
                                            "{selected}", "{enabled}", "{purchased}"
                                    },
                                    new Object[]{
                                            inventoryType == InventoryType.MAIN  ? "" : "TODO-LEVEL", inventoryType == InventoryType.MAIN ? "" : "TODO-maxLEVEL",
                                            inventoryType == InventoryType.DEFAULT_RUNE || inventoryType == InventoryType.POTIONS ? "TODO-EFFECTS" : "",
                                            (inventoryType == InventoryType.MAIN || inventoryType == InventoryType.POTIONS) ? "" : "TODO-CHARACTER",
                                            (inventoryType != InventoryType.ATTRIBUTES_RUNE) ? "" : "TODO-ATTRIBUTE",
                                            (inventoryType != InventoryType.ABILITIES) ? "" : "TODO-ABILITY", inventoryType == InventoryType.MAIN ? "" : "TODO-LINE",
                                            (inventoryType == InventoryType.MAIN || inventoryType == InventoryType.POTIONS) ? "" : "TODO-CHARACTER",
                                            inventoryType == InventoryType.POTIONS || inventoryType == InventoryType.ABILITIES ? "TODO-SELECTED" : "",
                                            inventoryType != InventoryType.DEFAULT_RUNE ? "" : "TODO-ENABLED", inventoryType == InventoryType.MAIN ? "" : "TODO-PURCHASED"
                                    }
                            ).getReplacedItem()
            );
        }
        return inventory;
    }

    public static class GUIUtils {
        public static InventoryType getInventoryTypeByTitle(String title) {
            Yaml file;
            for (InventoryType type : InventoryType.values()) {

                file = FileUtils.getDirectoryFile(DirectorySource.MENU.getType(), type.name().contains("_")
                        ? type.name().toLowerCase().split("_")[0] : type.name().toLowerCase());

                if (file.getString("Title").equals(title))
                    return type;
            }
            return null;
        }

        public static int getCountRecordsItems(InventoryType inventoryType, GroupSource group, Yaml file) {
            int counter = 0;
            for (int i = 0; i <= 100; i++)
                if (file.getString((inventoryType == InventoryType.MAIN || inventoryType == InventoryType.POTIONS)
                        ? "Slots." + i : "Slots." + group + "." + i) != null)
                    counter++;
            return counter;
        }
    }
}