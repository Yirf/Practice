package me.yirf.practice.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public interface Database {
    boolean open();
    void close();
    void table();
    Connection connection();
    void create(Object key);
    boolean exists(Object key);

    Profile get(UUID uuid);
    void set(UUID uuid, Profile profile);

}
