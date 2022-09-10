package ru.solomka.runes.core.gui.enums;

import lombok.Getter;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.config.files.FileUtils;
import ru.solomka.runes.core.rune.enums.RuneType;

public enum InventoryType {

    MAIN(FileUtils.getDirectoryFile(DirectorySource.MENU.getType(), "main")),
    POTIONS(FileUtils.getDirectoryFile(DirectorySource.MENU.getType(), "potions")),
    DEFAULT_RUNE(FileUtils.getDirectoryFile(DirectorySource.MENU.getType(), "default")),
    ABILITIES(FileUtils.getDirectoryFile(DirectorySource.MENU.getType(), "abilities")),
    ATTRIBUTES_RUNE(FileUtils.getDirectoryFile(DirectorySource.MENU.getType(), "attributes"));

    @Getter private final Yaml currentFile;

    InventoryType(Yaml currentFile) {
        this.currentFile = currentFile;
    }

}
