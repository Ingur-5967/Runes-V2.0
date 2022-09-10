package ru.solomka.runes.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.solomka.runes.core.gui.GUIManager;
import ru.solomka.runes.core.gui.enums.InventoryType;
import ru.solomka.runes.core.permission.UserManager;
import ru.solomka.runes.message.Messages;

import java.util.List;

public class RunesCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {

        UserManager permManager;

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                Messages.logger("This command only for players");
                return true;
            }

            new GUIManager((Player) sender, InventoryType.MAIN, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            return true;
        }

        Player player = (Player) sender;

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < args.length; i++)
            builder.append(args[i]).append(" ");

        switch (args[0].toLowerCase()) {
            case "loc-group": {

                if (builder.length() < 1) return true;

                Player targetPlayer = Bukkit.getPlayer(args[1]);

                if (targetPlayer == null) {
                    targetPlayer = player;
                    permManager = new UserManager(targetPlayer);

                }
                else {
                    permManager = new UserManager(targetPlayer);
                }
                player.teleport(UserManager.GroupUtils.getLocationGroup(permManager.getGroup()));
                break;
            }

            case "" : break;

            default:
                Messages.ERROR.replace("{desc}", "Введены неверные аргументы команды").send(player);
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
