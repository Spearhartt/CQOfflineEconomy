package com.conquestiamc;

import com.conquestiamc.logging.CqLogger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

import static com.conquestiamc.OfflineEconomy.plugin;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class PeriodicCheck {
    Balances config = Balances.initialize();

    // I don't actually see the need in this.
    public void periodicCheck() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
//        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                savePlayers();
//            }
//        }, 200L, 1200L);
    }

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
