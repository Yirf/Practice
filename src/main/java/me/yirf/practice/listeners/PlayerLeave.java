package me.yirf.practice.listeners;

import me.yirf.practice.sql.PlayerDB;
import me.yirf.practice.sql.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerLeave implements Listener {
    private PlayerDB playerDB;

    public PlayerLeave(PlayerDB playerDB) {
        this.playerDB = playerDB;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Profile profile = playerDB.getProfiles().get(uuid);

        if (profile != null) {
            playerDB.set(uuid, profile); // Save to database
            playerDB.getProfiles().remove(uuid); // Remove from memory
        }
    }
}
