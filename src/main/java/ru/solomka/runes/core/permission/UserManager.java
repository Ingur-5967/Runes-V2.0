package ru.solomka.runes.core.permission;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.config.files.FileUtils;
import ru.solomka.runes.core.permission.enums.GroupSource;
import ru.solomka.runes.core.rune.enums.RuneType;
import ru.solomka.runes.message.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserManager {

    @Getter
    private final Player player;
    @Getter
    private final Yaml playerFile;

    public UserManager(@NotNull Player player) {
        this.player = player;
        playerFile = FileUtils.getDirectoryFile(DirectorySource.PLAYER.getType(), player.getUniqueId().toString());
    }

    public void initPlayersGroup() {
        String[] lines = {"Name", "Group", "Runes"};
        for (GroupSource group : GroupSource.values()) {
            if (!player.isOp() && player.hasPermission("Rune." + group.name())) {
                Object[] params = {player.getName(), group.name(), ""};
                for (int i = 0; i < lines.length; i++)
                    playerFile.set(lines[i], params[i]);
            }
        }
        if (getGroup() == null && !player.isOp())
            Messages.ERROR.replace("{desc}", "Не удалось определить вашу группу!" +
                    "\n&7>> &fВозможные причины:&c вы являетесь оператором, некорректная группа").send(player);
    }

    public GroupSource getGroup() {
        return getPlayerFile().getString("Group") == null ? null :
                GroupSource.valueOf(getPlayerFile().getString("Group"));
    }

    public static class GroupUtils {

        public static @NotNull Location getLocationGroup(@NotNull GroupSource group) {

            String[] compareLocation = FileUtils.getDefaultCfg("config").getString("GroupLocations." + group.name()).split(";");

            return new Location(Bukkit.getWorld(compareLocation[0]),
                    Double.parseDouble(compareLocation[1]),
                    Double.parseDouble(compareLocation[2]),
                    Double.parseDouble(compareLocation[3]));
        }

        public static @NotNull List<Integer> getRunesGroup(RuneType runeType, GroupSource targetGroup) {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i <= 1000; i++) {
                if (FileUtils.getDirectoryFile(DirectorySource.SPECIAL.getType(), runeType.name().toLowerCase())
                        .getString("Slots." + targetGroup.name() + "." + i) != null) ids.add(i);
            }
            return ids;
        }

        public static synchronized @NotNull Map<RuneType, List<Integer>> getPlayerRunes(Player player) {
            Map<RuneType, List<Integer>> runes = new HashMap<>();
            List<Integer> list = new ArrayList<>();

            UserManager userManager = new UserManager(player);

            Yaml playerFile = userManager.getPlayerFile();
            GroupSource playerGroup = userManager.getGroup();

            for (RuneType runeType : RuneType.values()) {
                for (int id : GroupUtils.getRunesGroup(runeType, playerGroup)) {
                    if (playerFile.getString("Rune." + playerGroup.name() + "." + runeType.name() + "." + id) != null)
                        list.add(id);
                }
                runes.put(runeType, list);
                list.clear();
            }
            return runes;
        }
    }
}