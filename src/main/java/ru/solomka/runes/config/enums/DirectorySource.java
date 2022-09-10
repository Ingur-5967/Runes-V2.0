package ru.solomka.runes.config.enums;

import lombok.Getter;

public enum DirectorySource {
    PLAYER("playerdata"),
    MENU("menu"),
    DATA("data"),
    SPECIAL("runes"),
    NONE("");

    @Getter
    private final String type;

    DirectorySource(String type) {
        this.type = type;
    }

}
