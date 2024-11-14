package me.yirf.practice.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.yirf.practice.sql.Database;
import me.yirf.practice.sql.PlayerDB;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;

public class PlayerExpansion extends PlaceholderExpansion {

    PlayerDB db;

    public PlayerExpansion(PlayerDB db) {
        this.db = db;
    }

    @Override
    public String getAuthor() {
        return "Yirf";
    }

    @Override
    public String getIdentifier() {
        return "playerDB";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, String params) {
        return db.getValue(player.getUniqueId(), params).toString();
    }
}