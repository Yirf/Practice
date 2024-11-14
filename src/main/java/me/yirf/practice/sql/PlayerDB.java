package me.yirf.practice.sql;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerDB implements Database {

    private Connection connection;
    private final String path;

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
                    gang TEXT DEFAULT '',
                    deaths INTEGER DEFAULT 0,
                    level INTEGER DEFAULT 1,
                    experience DOUBLE DEFAULT 0.00
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
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO player (uuid, username) VALUES (?, ?)")) {
                ps.setString(1, p.getUniqueId().toString());
                ps.setString(2, p.getName());
                ps.executeUpdate();
                Bukkit.getLogger().info("Statement sucessful for player: " + p.getUniqueId());
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
    public Object getValue(Object key, String path) {
        Object value = "null";  // Default value if no result is found
        // Validate path to ensure it's a valid column name (you might use a list of allowed columns for this validation)
        List<String> validColumns = Arrays.asList("uuid", "username", "kills", "gang", "deaths", "level", "experience"); // Example of valid columns
        if (!validColumns.contains(path)) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }

        String query = "SELECT " + path + " FROM player WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1, key);  // Assuming `key` is a UUID or some identifier
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    value = rs.getObject(path);  // Get the value from the result set
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Unable to retrieve object from ResultSet");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {  // SQL error handling
                return value;
            }
            Bukkit.getLogger().severe("Unable to retrieve object from PreparedStatement (Error: " + e.getErrorCode() + ")");
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void setValue(Object key, String path, Object value) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE player SET " + path + " = ? WHERE uuid = ?")) {
            ps.setObject(1, value);
            ps.setString(2, key.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Unable to set object from PreparedStatement");
            e.printStackTrace();
        }
    }

}
