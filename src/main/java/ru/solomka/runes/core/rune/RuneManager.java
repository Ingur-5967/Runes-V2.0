package ru.solomka.runes.core.rune;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.config.files.FileUtils;
import ru.solomka.runes.core.gui.GUIManager;
import ru.solomka.runes.core.gui.enums.InventoryType;
import ru.solomka.runes.core.permission.UserManager;
import ru.solomka.runes.core.permission.enums.GroupSource;
import ru.solomka.runes.core.rune.enums.ElementRune;
import ru.solomka.runes.core.rune.enums.RuneType;
import ru.solomka.runes.core.rune.impl.Rune;
import ru.solomka.runes.message.Messages;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RuneManager {

    private Player player;
    private UserManager permManager;
    private RuneType runeType;
    private GroupSource groupSource;

    private Yaml currentFileRune;

    private List<Rune> runes = new ArrayList<>();

    public RuneManager(Player player, @NotNull RuneType runeType) {
        this.player = player;
        this.runeType = runeType;
        currentFileRune = FileUtils.getDirectoryFile(DirectorySource.SPECIAL.getType(), runeType.name().toLowerCase());
        permManager = new UserManager(player);
    }

    public RuneManager(GroupSource groupSource) {
        this.groupSource = groupSource;
    }

    public RuneManager(@NotNull RuneType runeType) {
        this.runeType = runeType;
        currentFileRune = FileUtils.getDirectoryFile(DirectorySource.SPECIAL.getType(), runeType.name().toLowerCase());
    }

    public void changeActivity(int id) {
        if(!hasRune(id)) {
            Messages.ERROR.replace("{desc}", "Вы не можете управлять руной, которая не принадлежит вам").send(player);
            return;
        }

        String targetPath = "Runes." + permManager.getGroup().name() + "." + runeType.name() + "." + id;

        String action;

        permManager.getPlayerFile().set(targetPath + ".Enable", !permManager.getPlayerFile().getBoolean(targetPath + ".Enable"));

        action = permManager.getPlayerFile().getBoolean(targetPath + ".Enable") ? "&aвключили" : "&cвыключили";

        Messages.DEV_RUNE.replace("{action}", action).replace("{id}", String.valueOf(id)).send(player);

        new GUIManager(player, GUIManager.GUIUtils.getInventoryTypeByTitle(player.getOpenInventory().getTitle()),
                new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));

    }

    public void addRune(int id) {
        if (hasRune(id)) {
            Messages.logger("WARN","Player already has rune");
            return;
        }

        permManager.getPlayerFile().set("Rune." + permManager.getGroup().name() + "." + runeType.name() + "." + id + ".Level", 0);
        Messages.logger("RUNE", "Success given rune player!");
    }

    public void upgradeRune(int id) {
        if (!hasRune(id)) {
            Messages.logger("WARN", "Not found rune");
            return;
        }

        String targetPath = "Runes." + permManager.getGroup().name() + "." + runeType.name() + "." + id;

        permManager.getPlayerFile().set(targetPath, permManager.getPlayerFile().getInt(targetPath) + 1);

        Messages.UPGRADE_RUNE
                .replace("{id}", String.valueOf(id))
                .replace("{level}", String.valueOf(0))
                .replace("{max_level}", getElementRune(ElementRune.MAX_LEVEL, id))
                .send(player);

        Messages.logger("RUNE", "Success upgraded rune player!");
    }

    public boolean existsRune(int id) {
        return currentFileRune.getString("Slots." + permManager.getGroup().name() + "." + id) != null;
    }

    public boolean hasRune(int id) {
        return UserManager.GroupUtils.getPlayerRunes(player).get(runeType).contains(id);
    }

    public String getElementRune(@NotNull ElementRune elementRune, int id) {
        if (!existsRune(id)) return "Not found";

        String defPath = "Slots." + groupSource + "." + id + ".";
        String elementName = elementRune.name().toLowerCase();
        String element = String.valueOf(Character.toUpperCase(elementName.charAt(0)));

        return elementRune == ElementRune.ATTRIBUTE
                ? currentFileRune.getString(defPath + "Settings." + element)
                : currentFileRune.getString(defPath + element);
    }

    public int getRuneIndexGroup(InventoryType inventoryType, GroupSource group, ItemStack currentClickedItem) {
        for(int i = 0; i < GUIManager.GUIUtils.getCountRecordsItems(inventoryType, group, inventoryType.getCurrentFile()); i++) {
            if(!existsRune(i)) continue;

            ItemStack itemSection = new ItemStack(
                    Material.getMaterial(inventoryType.getCurrentFile().getString("Slots." + group.name() + "." + i))
            );

            if(itemSection == currentClickedItem)
                return i;

        }
        return -1;
    }

    public Rune getClassRuneByParam(ItemStack currentViewItem) {
        for (Rune rune : runes) {
            if (rune.getRuneType() == runeType && rune.getViewItem() == currentViewItem)
                return rune;
        }
        return null;
    }
}
