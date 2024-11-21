package me.yirf.practice.listeners;

import me.yirf.practice.managers.NPCManager;
import me.yirf.practice.managers.StatManager;
import me.yirf.practice.sql.PlayerDB;
import me.yirf.practice.sql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerJoin implements Listener {

    private final PlayerDB playerDB;
    private final StatManager statManager;

    public PlayerJoin(PlayerDB playerDB, StatManager statManager) {
       this.playerDB = playerDB;
       this.statManager = statManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Profile profile = playerDB.get(uuid);

        if (profile == null) {
            playerDB.create(uuid);
            profile = playerDB.get(uuid); // Fetch newly created profile
        }

        playerDB.getProfiles().put(uuid, profile);
        statManager.lastLogin(uuid);
    }
}
