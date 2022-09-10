package ru.solomka.runes.message;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import ru.solomka.runes.Main;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.files.FileUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public enum Messages {

    COOLDOWN_OVER,
    ERROR,
    DEV_RUNE,
    UPGRADE_RUNE,
    GIVEN_RUNE,
    TELEPORT_SUCCESS,
    CHOOSE_ABILITIES,
    SET_LOCATION,
    ACTIVATION_ABILITIES,
    ACTIVE_DELAY,
    CHOOSE_POTION,
    SUCCESSFUL_POTION_THROW;

    private List<String> msg;

    public static void load(FileConfiguration c) {
        for (Messages message : Messages.values()) {
            Object obj = c.get("Messages." + message.name().replace("__", "."));
            if (obj instanceof List)
                message.msg = ((List<String>) obj).stream().map(m -> translateAlternateColorCodes('&', m)).collect(Collectors.toList());
            else {
                message.msg = Lists.newArrayList(obj == null ? "СООБЩЕНИЕ НЕ НАЙДЕНО" : translateAlternateColorCodes('&', obj.toString()));
            }
        }
        Messages.logger("(MESSAGES) >> Messages success loaded (" + Messages.values().length + " message)");
    }

    public static void logger(@NotNull String prefix, Object @NotNull ...values) {
        for(Object obj : values) {
            Main.getInstance().getLogger().info( "<" + prefix + "> " + obj.toString());
        }
    }

    public Sender replace(String from, String to) {
        Sender sender = new Sender();
        return sender.replace(from, to);
    }

    public void send(CommandSender player) {
        new Sender().send(player);
    }

    public class Sender {

        private final Map<String, String> placeholders = new HashMap<>();

        public void send(CommandSender player) {
            for(String message : Messages.this.msg) {
                sendMessage(player, replacePlaceholders(message));
            }
        }

        public String getMessage() {
            return replacePlaceholders(Messages.this.msg.get(0));
        }

        public Sender replace(String from, String to) {
            placeholders.put(from, to);
            return this;
        }

        private void sendMessage(CommandSender player, @NotNull String message) {
            if (message.startsWith("json:")) {
                player.sendMessage(String.valueOf(new TextComponent(ComponentSerializer.parse(message.substring(5)))));
            } else {
                player.sendMessage(translateAlternateColorCodes('&', message));
            }
        }

        private String replacePlaceholders(@NotNull String message) {
            if (!message.contains("{")) return message;
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
            return message;
        }
    }
}