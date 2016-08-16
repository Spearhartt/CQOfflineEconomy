package com.conquestiamc;

import com.conquestiamc.logging.CqLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

import static com.conquestiamc.OfflineEconomy.PLUGIN_LABEL;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class PeriodicCheck {
    Balances config = new Balances();

    public void periodicCheck() {
        BukkitScheduler scheduler = OfflineEconomy.plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(OfflineEconomy.plugin, new Runnable() {
            public void run() {
                savePlayers();
            }
        }, 200L, 1200L);
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

    public void isLoaded() {
        CqLogger.debug(PLUGIN_LABEL + "Loaded Periodic Check.");
    }
}
