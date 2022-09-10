package ru.solomka.runes.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import ru.solomka.runes.Main;
import ru.solomka.runes.config.Yaml;
import ru.solomka.runes.config.enums.DirectorySource;
import ru.solomka.runes.message.Messages;

import java.util.logging.Level;

public class RegistrationService {

    @Getter private final Main plugin;

    public RegistrationService(Main plugin) {
        this.plugin = plugin;
    }

    public void initConfigs(DirectorySource directoryType, String ...files) {
        if(plugin.getDataFolder() == null) plugin.getDataFolder().mkdir();
        for (String str : files) {
            if (directoryType != DirectorySource.NONE)
                new Yaml(directoryType.getType(), str + ".yml", true);
            else
                new Yaml(str + ".yml");
        }
        Messages.logger("(LOAD) >> Files loaded: " + files.length);
    }

    public void registrationCmd(CommandExecutor[] executors, TabCompleter[] completes, String ...commands) {

        String command;
        boolean executorsSmall = executors.length == 1;

        for (int i = 0; i < executors.length; i++) {
            command = executorsSmall ? commands[0] : commands[i];

            plugin.getCommand(command).setExecutor(executorsSmall ? executors[0] : executors[i]);
            plugin.getCommand(command).setTabCompleter(executorsSmall ? completes[0] : completes[i]);
        }
        Messages.logger("(COMMANDS) >> Registered commands: " + commands.length,
                "(COMMANDS) >> Success set tab-completer for commands (" + executors.length + " commands)");
    }

    public void registrationEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, Main.getInstance());
        }
        Messages.logger("(EVENTS) >> Registered listeners: " + listeners.length);
    }
}