package ru.solomka.runes.message.utils;

import lombok.Data;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@Data
public class ListHandler {

    private final List<String> list;
    private Object[] targets, replaces;
    private CommandSender p;

    public ListHandler(List<String> list, CommandSender p) {
        this.list = list;
        this.p = p;
    }

    public ListHandler(List<String> list, Object[] targets, Object[] replaces) {
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

    public String getDividedComponents(String separator, @NotNull List<?> list) {
        StringJoiner stringJoiner = new StringJoiner(separator);

        for (Object o : list)
            stringJoiner.add(o instanceof PotionEffectType ? ((PotionEffectType) o).getName() : o.toString());

        return stringJoiner.toString();
    }

    public List<String> getReplacedList() {
        Placeholder placeholder = new Placeholder(targets, replaces).addToMap();

        for(Map.Entry<String, String> holder : placeholder.getPlaceholders().entrySet())
            list.replaceAll(string -> string.replace(holder.getKey(), holder.getValue()));

        return list;
    }
}
