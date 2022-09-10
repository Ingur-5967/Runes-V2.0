package ru.solomka.runes.config.files;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.solomka.runes.Main;
import ru.solomka.runes.config.Yaml;

import java.io.File;

public class FileUtils {

    @Contract("_ -> new")
    public static @NotNull Yaml getDefaultCfg(String file) {
        return new Yaml(new File(Main.getInstance().getDataFolder(), file + ".yml"));
    }

    @Contract("_, _ -> new")
    public static @NotNull Yaml getDirectoryFile(String directory, String file) {
        return new Yaml(new File(Main.getInstance().getDataFolder() + "/" + directory + "/" + file + ".yml"));
    }
}