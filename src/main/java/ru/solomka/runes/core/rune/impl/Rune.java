package ru.solomka.runes.core.rune.impl;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import ru.solomka.runes.core.rune.enums.RuneType;

import java.util.List;

public abstract class Rune {

    @Getter private final RuneType runeType;
    @Getter private final ItemStack viewItem;

    protected Rune(RuneType runeType, ItemStack viewItem, String currentArgument) {
        this.runeType = runeType;
        this.viewItem = viewItem;
    }

    public abstract void onToggle();
}