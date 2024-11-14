package me.yirf.practice.listeners;

import me.yirf.practice.sql.PlayerDB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {

    PlayerDB playerDB;

    public PlayerJoin(PlayerDB playerDB) {
        this.playerDB = playerDB;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        playerDB.create(uuid);
    }
}
