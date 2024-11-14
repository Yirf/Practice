package me.yirf.practice.sql;

import java.sql.Connection;
import java.sql.Statement;

public class GangDB implements Database {
    @Override
    public boolean open() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public void table() {

    }

    @Override
    public Connection connection() {
        return null;
    }

    @Override
    public void create(Object key) {

    }

    @Override
    public boolean exists(Object key) {
        return false;
    }

    @Override
    public Object getValue(Object key, String path) {
        return null;
    }

    @Override
    public void setValue(Object key, String path, Object value) {

    }
}
