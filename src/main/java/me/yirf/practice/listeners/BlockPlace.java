package me.yirf.practice.listeners;

import me.yirf.practice.managers.PvPManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    private final PvPManager pvpManager;

    public BlockPlace(PvPManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();

        if (player.getWorld() == Bukkit.getWorld("spawn")) {
            event.setCancelled(true);
            return;
        }

        if (blockType == Material.END_CRYSTAL) {
            pvpManager.addPlacedBlock(event.getBlock().getLocation(), player);
        }
    }

}
