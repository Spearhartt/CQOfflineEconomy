package com.conquestiamc;

import com.conquestiamc.logging.CqLogger;
import org.bukkit.Bukkit;

import java.util.UUID;

import static com.conquestiamc.EconTracker.plugin;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class PlayerHandler {
    Balances config = Balances.initialize();


    public void savePlayers() {
        for (UUID key: config.onlinePlayers.keySet()) {
            if (config.onlinePlayers.get(key) >= 5) {
                config.savePlayer(Bukkit.getPlayer(key));
            } else {
                config.onlinePlayers.put(key, config.onlinePlayers.get(key) + 1);
            }
        }
    }

    public void saveAllPlayers() {
        for (UUID key: config.onlinePlayers.keySet()) {
            config.savePlayer(Bukkit.getPlayer(key));
        }
    }

    public void isLoaded() {
        CqLogger.debug(plugin, "Loaded Periodic Check.");
    }
}
