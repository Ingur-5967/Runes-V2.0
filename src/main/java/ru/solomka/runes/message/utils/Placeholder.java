package ru.solomka.runes.message.utils;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Placeholder {

    @Getter
    private final Object[] target, replace;

    private final Map<String, String> holders = new LinkedHashMap<>();

    public Placeholder(Object[] target, Object[] replace) {
        this.target = target;
        this.replace = replace;
    }

    public Placeholder addToMap() {
        if(target.length == 0 || replace.length == 0) return this;
        for(int i = 0; i < target.length; i++) {
            holders.put(target[i].toString(), replace[i].toString());
        }
        return this;
    }

    public Map<String, String> getPlaceholders() {
        return holders;
    }

    public String getReplacedString(String target) {
        for(Map.Entry<String, String> aMap : getPlaceholders().entrySet()) {
            target.replace(aMap.getKey(), aMap.getValue());
        }
        return target;
    }
}