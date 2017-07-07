package com.conquestiamc;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class CQPlayer {
    Economy econ = EconTracker.econ;

    public double getOnlineBalance(Player player) {
        return econ.getBalance(player);
    }

    public double getOfflineBalance(OfflinePlayer offlinePlayer) {
        return econ.getBalance(offlinePlayer);
    }
}