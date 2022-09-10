package ru.solomka.runes.core.rune;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.config.files.FileUtils;
import ru.solomka.runes.core.permission.UserManager;
import ru.solomka.runes.core.permission.enums.GroupSource;
import ru.solomka.runes.core.rune.enums.RuneType;
import ru.solomka.runes.message.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PotionManager {

    @Getter private final Player player;

    private final Yaml file;
    private final UserManager userManager;

    public PotionManager(Player player) {
        this.player = player;
        file = FileUtils.getDirectoryFile(DirectorySource.SPECIAL.getType(), "default");
        userManager = new UserManager(player);
    }

    public void togglePotion(String name) {
        Yaml playerFile = userManager.getPlayerFile();

        String currentPath = "Runes." + userManager.getGroup().name() + "." +
                RuneType.DEFAULT.name() + "." + getIndexRuneByName(name) + ".Enable";

        userManager.getPlayerFile().set(currentPath, !playerFile.getBoolean(currentPath));
        Messages.DEV_RUNE.replace("{action}", playerFile.getBoolean(currentPath) ? "&aвключили&f" : "&cвыключили&f").send(player);
    }

    public void clearOtherEffects() {
        for(PotionEffect potionEffect : player.getActivePotionEffects()) {
            if(potionEffect.getDuration() < Integer.MAX_VALUE ||
                    getGroupsEffects().get(userManager.getGroup()).contains(potionEffect)) continue;
            player.removePotionEffect(potionEffect.getType());
        }
    }

    private int getIndexRuneByName(String name) {
        for(int id : UserManager.GroupUtils.getRunesGroup(RuneType.DEFAULT, userManager.getGroup())) {
            if(file.getString("Slots." + userManager.getGroup().name() + "." + id).equals(name))
                return id;
        }
        return -1;
    }

    private @NotNull Map<GroupSource, List<PotionEffect>> getGroupsEffects() {
        Map<GroupSource, List<PotionEffect>> effects = new HashMap<>();
        List<PotionEffect> potionEffects;

        for(GroupSource groupSource : GroupSource.values()) {
            potionEffects = new ArrayList<>();
            for(int id : UserManager.GroupUtils.getRunesGroup(RuneType.DEFAULT, groupSource)) {
                if(!file.getBoolean("Slots." + groupSource.name() + "." + id + ".Enable")) continue;
                for(String effect : file.getStringList("Slots." + groupSource.name() + "." + id + ".Effects"))
                    potionEffects.add(
                            new PotionEffect(PotionEffectType.getByName(effect.split(";")[0]),
                            Integer.MAX_VALUE, Integer.parseInt(effect.split(";")[1]))
                    );
            }
            effects.put(groupSource, potionEffects);
        }
        return effects;
    }
}