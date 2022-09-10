package ru.solomka.runes.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ru.solomka.runes.Main;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.core.gui.GUIManager;
import ru.solomka.runes.core.gui.enums.InventoryType;
import ru.solomka.runes.core.permission.UserManager;
import ru.solomka.runes.core.rune.RuneManager;
import ru.solomka.runes.core.rune.enums.RuneType;
import ru.solomka.runes.core.rune.impl.Rune;
import ru.solomka.runes.message.Messages;

public class RuneEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        UserManager permManager = new UserManager(p);



        new BukkitRunnable() {
            @Override
            public void run() {
                permManager.initPlayersGroup();
            }
        }.runTaskLater(Main.getInstance(), 20L);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int slot = e.getSlot();
        Inventory clickInventory = e.getClickedInventory();

        if(clickInventory == null || clickInventory == p.getInventory()) return;

        InventoryType inventoryType = GUIManager.GUIUtils.getInventoryTypeByTitle(clickInventory.getTitle());

        if(inventoryType == null) return;

        e.setCancelled(true);

        Yaml currentFile = inventoryType.getCurrentFile();

        if(inventoryType != InventoryType.MAIN && (currentFile.getBoolean("Back.Enable") && slot == currentFile.getInt("Back.Slot"))) {
            new GUIManager(p, InventoryType.MAIN, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            return;
        }

        switch (inventoryType) {
            case MAIN: {
                switch (slot) {
                    case 12: {
                        new GUIManager(p, InventoryType.DEFAULT_RUNE, new ItemStack(Material.STAINED_GLASS_PANE, 1 ,(byte) 7));
                        break;
                    }
                    case 14: {
                        break;
                    }
                    case 31: {
                        break;
                    }
                }

                break;
            }
            case ABILITIES: {
                break;
            }

            case DEFAULT_RUNE: {
                ItemStack targetItem = clickInventory.getItem(slot);
                RuneManager runeManager = new RuneManager(p, RuneType.DEFAULT);
                int targetIdRune = runeManager.getRuneIndexGroup(InventoryType.DEFAULT_RUNE, new UserManager(p).getGroup(), targetItem);

                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> runeManager.changeActivity(targetIdRune));
                break;
            }

            case ATTRIBUTES_RUNE: {
                break;
            }
        }


    }


}
