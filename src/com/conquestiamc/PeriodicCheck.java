package com.conquestiamc;

import com.conquestiamc.logging.CqLogger;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

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
                saveAllPlayers();
            }
        }, 200L, 6000L);
    }

    public void saveAllPlayers() {
        for (Player player : OfflineEconomy.plugin.getServer().getOnlinePlayers()) {
            if (player != null) {
                config.savePlayer(player);
            }
        }
    }

    public void isLoaded() {
        CqLogger.debug(PLUGIN_LABEL + "Loaded Periodic Check.");
    }
}
