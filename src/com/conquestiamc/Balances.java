package com.conquestiamc;

import com.conquestiamc.logging.CqLogger;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static com.conquestiamc.OfflineEconomy.PLUGIN_LABEL;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class Balances {

    public void load() {
        File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");
        File file = new File(OfflineEconomy.dataFolder, "config.yml");

        if (!balanceFile.exists()) {
            OfflineEconomy.plugin.saveResource("balances.yml", false);
        }

        if (!file.exists()) {
            OfflineEconomy.plugin.saveResource("config.yml", false);
        }
    }

    public void savePlayer(OfflinePlayer player) {
        if (player != null && player.getPlayer() != null) {
            File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");
            FileConfiguration balanceData = YamlConfiguration.loadConfiguration(balanceFile);

            long balance = (long)new CQPlayer().getOfflineBalance(player) * 100;
            balanceData.set(player.getName(), balance);

            try {
                //CqLogger.debug(PLUGIN_LABEL + "[SAVE] Saving balance of " + balance + " for player " + player.getName());
                balanceData.save(balanceFile);
            } catch (IOException ex) {
                CqLogger.debug(PLUGIN_LABEL + "IOException while saving balances.yml");
            }
        }
    }

    public long loadLongBalance(OfflinePlayer player) {
        if (player != null) {
            File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");


            try {
                //CqLogger.debug(PLUGIN_LABEL + "[LOAD] Loading balance of " + YamlConfiguration.loadConfiguration(balanceFile).get(player.getName()) + " for player " + player.getName());
                return new Long((int)YamlConfiguration.loadConfiguration(balanceFile).get(player.getName()));
            } catch (NullPointerException e) {
                CqLogger.debug(PLUGIN_LABEL + "Player " + player.getName() + " has not been stored and cannot be loaded.");
            }
        }

        return 0;
    }

    public boolean canAfford(OfflinePlayer player, long amount) {
        if (player != null) {
            if (amount <= loadLongBalance(player)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isStored(OfflinePlayer player) {
        File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");
        FileConfiguration yml = null;

        if (player != null) {

            try {
                yml = YamlConfiguration.loadConfiguration(balanceFile);
            } catch (NullPointerException e) {
                CqLogger.debug(PLUGIN_LABEL + "Player " + player.getName() + " has not been stored and cannot be loaded.");
            }

            if (yml != null) {
                return true;
            }
        }
        return false;
    }

    public void setLongBalance(OfflinePlayer player, long newBalance) {
        if (player != null) {
            File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");

            FileConfiguration balanceData = YamlConfiguration.loadConfiguration(balanceFile);
            balanceData.set(player.getName(), newBalance);

            try {
                //CqLogger.debug(PLUGIN_LABEL + "[SET] Saving balance of " + newBalance + " for player " + player.getName());
                balanceData.save(balanceFile);
                return;
            } catch (IOException ex) {
                CqLogger.debug(PLUGIN_LABEL + "IOException while saving balances.yml");
            }

        } else {
            CqLogger.debug(PLUGIN_LABEL + "Could not set balance for player: " + player.getName());
        }
    }
}
