package me.yirf.practice.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PvPManager {

    private final Map<Location, Player> placedBlocks = new HashMap<>();


    public void addPlacedBlock(Location location, Player player) {
        placedBlocks.put(location, player);
    }


    public Player getBlockPlacer(Location location) {
        return placedBlocks.get(location);
    }


    public void removeBlock(Location location) {
        placedBlocks.remove(location);
    }


    public void clearAll() {
        placedBlocks.clear();
    }
}
