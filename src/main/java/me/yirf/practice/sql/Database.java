package me.yirf.practice.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;

public interface Database {
    boolean open();
    void close();
    void table();
    Connection connection();
    void create(Object key);
    boolean exists(Object key);

    Object getValue(Object key, String path);
    void setValue(Object key, String path, Object value);

}
