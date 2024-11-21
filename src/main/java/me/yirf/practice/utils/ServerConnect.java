package me.yirf.practice.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yirf.practice.Practice;
import org.bukkit.entity.Player;

public class ServerConnect {

    private final Practice practice;

    public ServerConnect(Practice practice) {
        this.practice = practice;
    }

    public void connect(Player p, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(practice,"BungeeCord", out.toByteArray());
    }
}
