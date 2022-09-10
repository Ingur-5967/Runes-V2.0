package ru.solomka.runes.message.enums.utils;

import lombok.Data;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@Data
public class ListHandler {

    private final List<String> list;
    private String[] targets, replaces;
    private LivingEntity p;

    public ListHandler(List<String> list, LivingEntity p) {
        this.list = list;
        this.p = p;
    }

    public ListHandler(List<String> list, String[] targets, String[] replaces) {
        this.list = list;
        this.targets = targets;
        this.replaces = replaces;
    }

    public void sendList() {
        StringBuilder sb = new StringBuilder();
        for (String string : list) {
            sb.append(string).append("\n");
        }
        p.sendMessage(translateAlternateColorCodes('&', sb.toString()));
    }

    public List<String> getReplacedList() {
        Placeholder placeholder = new Placeholder(targets, replaces).addToMap();
        for(Map.Entry<String, String> plch : placeholder.getPlaceholders().entrySet()) {
            list.replaceAll(s -> s.replace(plch.getKey(), plch.getValue()));
        }
        return list;
    }
}
