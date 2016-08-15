package com.conquestiamc.Listeners;

import com.conquestiamc.Balances;
import com.conquestiamc.OfflineEconomy;
import com.conquestiamc.logging.CqLogger;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.conquestiamc.OfflineEconomy.CQ;
import static com.conquestiamc.OfflineEconomy.PLUGIN_LABEL;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class PlayerListener implements Listener {
    Balances config = new Balances();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (event.getPlayer() != null) {
            if (config.isStored(event.getPlayer())) {
                double storedBalance = config.loadLongBalance(event.getPlayer()) * 0.01;
                double currentBalance = OfflineEconomy.econ.getBalance(event.getPlayer());
                if (storedBalance != currentBalance) {
                    if (currentBalance > storedBalance) {
                        OfflineEconomy.econ.withdrawPlayer(event.getPlayer(), currentBalance - storedBalance);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                event.getPlayer().sendMessage(CQ + " You spent " + ChatColor.RED + (currentBalance - storedBalance) + " Edens" + ChatColor.GRAY + " while offline.");
                            }
                        }.runTaskLater(OfflineEconomy.plugin, 60L);

                    } else if (storedBalance > currentBalance) {
                        OfflineEconomy.econ.depositPlayer(event.getPlayer(), storedBalance - currentBalance);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                event.getPlayer().sendMessage(CQ + " You gained " + ChatColor.GREEN + (storedBalance - currentBalance) + " Edens" + ChatColor.GRAY + " while offline.");
                            }
                        }.runTaskLater(OfflineEconomy.plugin, 60L);
                    }
                }
            } else {
                config.savePlayer(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        config.savePlayer(event.getPlayer());
    }

    public void isLoaded() {
        CqLogger.debug(PLUGIN_LABEL + "Loaded Player Listener.");
    }
}
