package ru.solomka.runes.message.enums.utils;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Placeholder {

    @Getter private final String[] target, replace;

    private final Map<String, String> holders = new LinkedHashMap<>();

    public Placeholder(String[] target, String[] replace) {
        this.target = target;
        this.replace = replace;
    }

    public Placeholder addToMap() {
        if(target.length == 0 || replace.length == 0) return this;
        for(int i = 0; i < target.length; i++) {
            holders.put(target[i], replace[i]);
        }
        return this;
    }

    public Map<String, String> getPlaceholders() {
        if(holders.isEmpty()) return new LinkedHashMap<>();
        return holders;
    }
}