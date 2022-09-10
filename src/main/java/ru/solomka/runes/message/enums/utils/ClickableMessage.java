package ru.solomka.runes.message.enums.utils;

import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ClickableMessage {

    private final List<TextComponent> components = new ArrayList<>();
    private TextComponent current;

    public ClickableMessage(String msg) {
        add(msg);
    }

    public ClickableMessage add(String msg) {
        TextComponent component = new TextComponent(msg);
        components.add(component);
        current = component;
        return this;
    }

    private void hover(@NotNull TextComponent component, String msg) {
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(msg).create()));
    }

    public ClickableMessage hover(String msg) {
        hover(current, msg);
        return this;
    }

    private void command(@NotNull TextComponent component, String command) {
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    }

    private void link(@NotNull TextComponent component, String url) {
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    }

    public ClickableMessage command(String command) {
        command(current, command);
        return this;
    }

    public ClickableMessage link(String url) {
        link(current, url);
        return this;
    }

    public void sendToPlayer(@NotNull Player player) {
        player.spigot().sendMessage(components.toArray(new BaseComponent[0]));
    }
}
