package ru.solomka.runes;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.solomka.runes.commands.RunesCommands;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.core.RegistrationService;
import ru.solomka.runes.events.RuneEvents;
import ru.solomka.runes.message.Messages;

import java.io.File;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        RegistrationService registrationService = new RegistrationService(this);

        registrationService.registrationCmd(new CommandExecutor[]{new RunesCommands()}, new TabCompleter[]{new RunesCommands()}, "runes");
        registrationService.registrationEvents(new RuneEvents());

        registrationService.initConfigs(DirectorySource.DATA, "locations");
        registrationService.initConfigs(DirectorySource.MENU, "main", "attributes", "abilities", "potions", "default");
        registrationService.initConfigs(DirectorySource.PLAYER, "example");
        registrationService.initConfigs(DirectorySource.SPECIAL, "default", "attributes", "abilities");
        registrationService.initConfigs(DirectorySource.NONE, "messages");

        Messages.load(new Yaml(new File(getDataFolder(), "messages.yml")).getFileConfiguration());


    }

    @Override
    public void onDisable() {
    }
}
