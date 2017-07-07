package com.conquestiamc.Listeners;

import com.conquestiamc.Balances;
import com.conquestiamc.EconTracker;
import com.conquestiamc.logging.CqLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

import static com.conquestiamc.EconTracker.CQ;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class PlayerListener implements Listener {
    static Balances config = Balances.initialize();

    @EventHandler
    public void playerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(EconTracker.OfflineEconomy, new Runnable() {
            @Override
            public void run() {
                handlePlayer(event.getPlayer());
            }
        }, 5L);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        config.savePlayer(event.getPlayer());
    }

    public void isLoaded() {
        CqLogger.debug(EconTracker.plugin, "Loaded Player Listener.");
    }

    public void handlePlayer(final Player player) {
        if (player != null) {
            if (config.isStored(player)) {
                final double storedBalance = config.loadBalance(player);
                final double currentBalance = EconTracker.econ.getBalance(player);
                final DecimalFormat df = new DecimalFormat("0.00");
                if (storedBalance != currentBalance) {
                    if (currentBalance > storedBalance) {
                        EconTracker.econ.withdrawPlayer(player, currentBalance - storedBalance);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage(CQ + " You spent " + ChatColor.RED + df.format(currentBalance - storedBalance) + " Edens" + ChatColor.GRAY + " while offline.");
                            }
                        }.runTaskLater(EconTracker.plugin, 60L);

                    } else if (storedBalance > currentBalance) {
                        EconTracker.econ.depositPlayer(player, storedBalance - currentBalance);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage(CQ + " You gained " + ChatColor.GREEN + df.format(storedBalance - currentBalance) + " Edens" + ChatColor.GRAY + " while offline.");
                            }
                        }.runTaskLater(EconTracker.plugin, 60L); // 0(10
                    }
                }
            } else {
                config.savePlayer(player);
            }
            config.offlineBalances.remove(player.getName()); // O(1)
            config.onlinePlayers.put(player.getUniqueId(), 1);   // O(1)
        }
    }
}
