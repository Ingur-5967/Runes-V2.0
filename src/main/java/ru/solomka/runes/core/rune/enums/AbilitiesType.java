package ru.solomka.runes.core.rune.enums;

import lombok.Getter;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.config.files.FileUtils;

public enum AbilitiesType {

    FIRE_ALL("Поджигает окружающие блоки"),
    FIRE_PROJECTILE("Выпускает огненный снаряд"),
    LIGHTING_STRIKE("Стреляет молнией по напрявлению взгляда"),
    HORIZONTAL_ROW_SHELLS("Выпускает горизонтальные снаряды по направлению взгляда"),
    MAGIC_SHIELD("Магический щит, который не позволяет получать урон"),
    MAGIC_PROJECTILE("Выпускает магический снаряд"),
    EXPLOSIVE_POTION_ANY("Выбор любого взрывающегося зелья"),
    EFFECT_ANY("Выбор любого зелья, которое будет наложено на игрока"),
    DISCARD_ALL("Отрбасывать всех сущностей"),
    FIRE_RESISTANCE("Стойкость к огню"),
    INVISIBLE("Невидемость"),
    HARDNESS_ARMOUR("Увеличивает твердость брони"),
    POWER("Временно повышается сила"),
    DAMAGE_RESISTANCE("Сопротивление к урону");

    @Getter private final String description;

    @Getter private final Yaml currentFile;

    AbilitiesType(String description) {
        this.description = description;
        currentFile = FileUtils.getDirectoryFile(DirectorySource.SPECIAL.getType(), "abilities");
    }
}