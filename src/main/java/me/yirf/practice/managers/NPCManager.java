package me.yirf.practice.managers;

import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import me.yirf.mapRefresh.MapRefresh;
import me.yirf.mapRefresh.map.GameMap;
import me.yirf.practice.Practice;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;

public class NPCManager implements Listener {

    private Map<String, GameMap> npcs = new HashMap<>();
    Practice practice;

    public NPCManager(Practice practice) {
        this.practice = practice;
        MapRefresh mapRefresh = practice.mapRefresh;
        FileConfiguration config = practice.getConfig();

        ConfigurationSection npcSection = config.getConfigurationSection("npc");
        if (npcSection != null) {
            for (String key : npcSection.getKeys(false)) {
                GameMap map = mapRefresh.getMaps().get(npcSection.getString(key));
                npcs.put(key, map);
            }
        }
    }

    @EventHandler
    public void npcClick(NpcInteractEvent e) {
        String id = e.getEntry().getId();

        if (!npcs.containsKey(id)) {return;}
        GameMap map = npcs.get(id);

        if (!map.isLoaded()) {
            e.getPlayer().sendMessage(text("That maps seems to be unloaded!", NamedTextColor.RED));
            return;
        }

        Bukkit.getScheduler().runTaskLater(practice, () -> {
            e.getPlayer().sendMessage(text("Warping to map...", NamedTextColor.GREEN));
            e.getPlayer().teleport(map.spawn());
        }, 0L);

    }
}
