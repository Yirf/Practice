package me.yirf.practice.listeners;

import me.yirf.practice.Practice;
import me.yirf.practice.managers.PvPManager;
import me.yirf.practice.managers.StatManager;
import me.yirf.practice.sql.PlayerDB;
import me.yirf.practice.sql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerKill implements Listener {

    private final PvPManager pvpManager;
    private StatManager statManager;

    public PlayerKill(PvPManager pvpManager, StatManager statManager) {
        this.pvpManager = pvpManager;
        this.statManager = statManager;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        event.setDeathMessage("");
        Player victim = event.getPlayer();
        Player attacker;
        victim.teleport(Practice.spawnLocation);
        if (victim.getKiller() == null) {
            victim.sendMessage(ChatColor.GRAY + "You died! " + ChatColor.RED + "+1 death");
            statManager.deaths(victim.getUniqueId(), 1);
            return;
        }
        if (victim.getKiller() instanceof Player) {
            attacker = victim.getKiller();
        } else {
            attacker = pvpManager.getBlockPlacer(victim.getKiller().getLocation());
        }

        statManager.kills(attacker.getUniqueId(), 1);
        statManager.deaths(victim.getUniqueId(), 1);
        statManager.experience(attacker.getUniqueId(), 5);
        attacker.sendMessage(ChatColor.GRAY + "You killed " + victim.getName() + "! " + ChatColor.GREEN + "+1 kill");
        victim.sendMessage(ChatColor.GRAY + "You died to " + attacker.getName() + "! " + ChatColor.RED + "+1 death");
        event.setDeathMessage(ChatColor.RED + attacker.getName() + " killed " + victim.getName() + "!");
    }
}