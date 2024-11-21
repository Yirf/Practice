package me.yirf.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {

    public static Location getLocation(FileConfiguration config, String path) {
        World world = Bukkit.getWorld(config.getString(path + ".world"));
        if (world == null) {world = Bukkit.getWorld("world");} // get or default world but stupider
        return new Location(world,
                config.getDouble(path + ".x"), // x coord
                config.getDouble(path + ".y"), // y coord
                config.getDouble(path + ".z"), // z coord
                config.getInt(path + ".yaw"), // yaw
                config.getInt(path + ".pitch")); // pitch
    }
}
