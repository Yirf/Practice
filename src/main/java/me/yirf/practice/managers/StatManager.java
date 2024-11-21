package me.yirf.practice.managers;

import me.yirf.practice.sql.PlayerDB;
import me.yirf.practice.sql.Profile;

import java.util.UUID;

public class StatManager {

    private final PlayerDB playerDB;

    public StatManager(PlayerDB playerDB) {
        this.playerDB = playerDB;
    }

    private Profile ensureProfile(UUID uuid) {
        Profile profile = playerDB.getProfiles().get(uuid);
        if (profile == null) {
            profile = new Profile(0, 0, 1, 0.0, System.currentTimeMillis());
            playerDB.getProfiles().put(uuid, profile); // Add to in-memory storage
            playerDB.set(uuid, profile); // Save to database
        }
        return profile;
    }

    public void kills(UUID uuid, int amount) {
        Profile old = ensureProfile(uuid);
        Profile updated = new Profile(
                old.kills() + amount,
                old.deaths(),
                old.level(),
                old.experience(),
                old.lastLogin()
        );
        playerDB.getProfiles().put(uuid, updated);
        playerDB.set(uuid, updated);
    }

    public void deaths(UUID uuid, int amount) {
        Profile old = ensureProfile(uuid);
        Profile updated = new Profile(
                old.kills(),
                old.deaths() + amount,
                old.level(),
                old.experience(),
                old.lastLogin()
        );
        playerDB.getProfiles().put(uuid, updated);
        playerDB.set(uuid, updated);
    }

    public void experience(UUID uuid, double amount) {
        Profile old = ensureProfile(uuid);
        double exp = old.experience() + amount;
        if (exp >= xpNeeded(old.experience(), old.level())) {
            level(uuid, 1); // Increment level if XP threshold is reached
            exp = 0;
        }
        Profile updated = new Profile(
                old.kills(),
                old.deaths(),
                old.level(),
                exp,
                old.lastLogin()
        );
        playerDB.getProfiles().put(uuid, updated);
        playerDB.set(uuid, updated);
    }

    public void level(UUID uuid, int amount) {
        Profile old = ensureProfile(uuid);
        Profile updated = new Profile(
                old.kills(),
                old.deaths(),
                old.level() + amount,
                old.experience(),
                old.lastLogin()
        );
        playerDB.getProfiles().put(uuid, updated);
        playerDB.set(uuid, updated);
    }

    public void lastLogin(UUID uuid) {
        Profile old = ensureProfile(uuid);
        Profile updated = new Profile(
                old.kills(),
                old.deaths(),
                old.level(),
                old.experience(),
                System.currentTimeMillis()
        );
        playerDB.getProfiles().put(uuid, updated);
        playerDB.set(uuid, updated);
    }

    private double xpNeeded(double currentExperience, int level) {
        double multiplier = 10.0;
        return multiplier * (level * level + level) - currentExperience;
    }
}