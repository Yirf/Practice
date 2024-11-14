package me.yirf.practice;

import me.yirf.practice.listeners.PlayerJoin;
import me.yirf.practice.placeholder.PlayerExpansion;
import me.yirf.practice.sql.Database;
import me.yirf.practice.sql.PlayerDB;
import org.bukkit.plugin.java.JavaPlugin;

public final class Practice extends JavaPlugin {

    PlayerDB playerDB;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initDB();
        new PlayerExpansion(playerDB).register();

        getServer().getPluginManager().registerEvents(new PlayerJoin(playerDB), this);
    }

    @Override
    public void onDisable() {
        playerDB.close();
    }

    private void initDB() {
        playerDB = new PlayerDB(getDataFolder().getAbsolutePath() + "/player.db");
        playerDB.open();

        if (playerDB == null) {
            getServer().getPluginManager().disablePlugin(this);
        }

    }
}
