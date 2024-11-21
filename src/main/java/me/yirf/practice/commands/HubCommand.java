package me.yirf.practice.commands;

import me.yirf.practice.Practice;
import me.yirf.practice.utils.ServerConnect;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class HubCommand implements CommandExecutor {

    private final ServerConnect connecter;

    public HubCommand(Practice practice) {
        this.connecter = new ServerConnect(practice);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        player.sendMessage(text("Sending you to the lobby.", NamedTextColor.GREEN));
        connecter.connect(player, "hub-1");

        return true;
    }
}
