package me.yirf.practice;

import me.yirf.mapRefresh.MapRefresh;
import me.yirf.practice.commands.HubCommand;
import me.yirf.practice.listeners.*;
import me.yirf.practice.managers.NPCManager;
import me.yirf.practice.managers.PvPManager;
import me.yirf.practice.managers.StatManager;
import me.yirf.practice.placeholder.PlayerExpansion;
import me.yirf.practice.sql.PlayerDB;
import me.yirf.practice.sql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Practice extends JavaPlugin {

    PlayerDB playerDB;
    public MapRefresh mapRefresh;
    private NPCManager npcManager;
    private StatManager statManager;
    private PvPManager pvpManager = new PvPManager();;
    public static Location spawnLocation;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        initDB();
        new PlayerExpansion(playerDB).register();
        accessMapRefresh();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");


        getServer().getPluginManager().registerEvents(new NPCManager(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(playerDB, statManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(playerDB), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(pvpManager), this);
        getServer().getPluginManager().registerEvents(new PlayerKill(pvpManager, statManager), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);

        this.getCommand("hub").setExecutor(new HubCommand(this));
        generateScheduler();
    }

    @Override
    public void onDisable() {
        playerDB.savePlayers();
        playerDB.close();
    }

    private void initDB() {
        playerDB = new PlayerDB(getDataFolder().getAbsolutePath() + "/player.db");
        playerDB.open();

        if (playerDB == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.statManager = new StatManager(playerDB);
    }

    private void accessMapRefresh() {
        Plugin plugin = getServer().getPluginManager().getPlugin("MapRefresh");
        if (plugin == null) {
            getLogger().severe("MapRefresh plugin not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (plugin.isEnabled()) {
            mapRefresh = (MapRefresh) plugin;
            getLogger().info("MapRefresh plugin found and linked successfully.");
        } else {
            getLogger().warning("MapRefresh plugin is not found or not of the expected type.");
        }

        this.spawnLocation = mapRefresh.getSpawn();
    }

    private void generateScheduler() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            playerDB.savePlayers();
        }, 0L, (20L * 60) * getConfig().getInt("sql.dump-time"));
    }
}
