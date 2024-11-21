package me.yirf.practice.sql;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerDB implements Database {

    private Connection connection;
    private final String path;

    private HashMap<UUID, Profile> profiles = new HashMap<>();

    public PlayerDB(String path) {
        this.path = "jdbc:sqlite:" + path;
    }

    @Override
    public boolean open() {
        try {
            connection = DriverManager.getConnection(path);
            table();
            return true;
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Unable to grab connection for sqlite!");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Unable to close connection for sqlite!");
            e.printStackTrace();
        }
    }

    @Override
    public void table() {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("""
                CREATE TABLE IF NOT EXISTS player (
                    uuid TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    kills INTEGER DEFAULT 0,
                    deaths INTEGER DEFAULT 0,
                    level INTEGER DEFAULT 1,
                    experience DOUBLE DEFAULT 0.00,
                    lastLogin LONG DEFAULT 0
                )
            """);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection connection() {
        return connection;
    }

    @Override
    public void create(Object key) {
        if (connection == null) return;

        OfflinePlayer p = Bukkit.getOfflinePlayer((UUID) key);
        if (!exists(p.getUniqueId())) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO player (uuid, username, kills, deaths, level, experience, lastLogin) VALUES (?, ?, 0, 0, 1, 0.0, ?)")) {
                ps.setString(1, p.getUniqueId().toString());
                ps.setString(2, p.getName());
                ps.setLong(3, System.currentTimeMillis());
                ps.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Unable to create statement for player: " + p.getUniqueId());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean exists(Object key) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM player WHERE uuid = ?")){
            ps.setObject(1, key);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return true;
                }
                return false;
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Unable to create ResultSet for check.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Unable to create PreparedStatement for check.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Profile get(UUID uuid) {
        String query = "SELECT kills, deaths, level, experience, lastLogin FROM player WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString()); // Assuming `key` is a UUID

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int kills = rs.getInt("kills");
                    int deaths = rs.getInt("deaths");
                    int level = rs.getInt("level");
                    double experience = rs.getDouble("experience");
                    long lastLogin = rs.getLong("lastLogin");

                    return new Profile(kills, deaths, level, experience, lastLogin); // Create and return the Profile object
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Unable to retrieve profile from ResultSet");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Unable to retrieve profile from database");
            e.printStackTrace();
        }

        return null; // Return null if no profile is found or an error occurs
    }

    @Override
    public void set(UUID uuid, Profile profile) {
        String query = "UPDATE player SET kills = ?, deaths = ?, level = ?, experience = ?, lastLogin = ? WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Set each field of the Profile record
            ps.setInt(1, profile.kills());
            ps.setInt(2, profile.deaths());
            ps.setInt(3, profile.level());
            ps.setDouble(4, profile.experience());
            ps.setLong(5, profile.lastLogin());
            ps.setString(6, uuid.toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Unable to set profile in database.");
            e.printStackTrace();
        }
    }

    public HashMap<UUID, Profile> getProfiles() {
        return profiles;
    }

    public void savePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = getProfiles().get(player.getUniqueId());
            set(player.getUniqueId(), profile);
        }
    }
}
