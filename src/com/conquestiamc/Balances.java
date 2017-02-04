package com.conquestiamc;

import com.conquestiamc.logging.CqLogger;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static com.conquestiamc.OfflineEconomy.PLUGIN_LABEL;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class Balances {

    public static HashMap<UUID, Integer> onlinePlayers = new HashMap<>();
    public static HashMap<String, Long> offlineBalances = new HashMap<>();

    public void load() {
        CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Loading balances.yml");
        CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Loading config.yml");
        File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");
        File file = new File(OfflineEconomy.dataFolder, "config.yml");

        if (!balanceFile.exists()) {
            CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Creating balances.yml");
            OfflineEconomy.plugin.saveResource("balances.yml", false);
        }

        if (!file.exists()) {
            CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Creating config.yml");
            OfflineEconomy.plugin.saveResource("config.yml", false);
        }

        FileConfiguration balanceData = YamlConfiguration.loadConfiguration(balanceFile);

        CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Loading all stored player balances into memory.");
        for (String playerName : balanceData.getValues(false).keySet()) {
            if ((int) balanceData.get(playerName) > 0) {
                Long bal = new Long((int) balanceData.get(playerName));
                offlineBalances.put(playerName, bal);
            }
        }
    }

    public void savePlayer(OfflinePlayer player) {
        if (player != null && player.getPlayer() != null) {
            File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");
            FileConfiguration balanceData = YamlConfiguration.loadConfiguration(balanceFile);

            long balance = (long)new CQPlayer().getOfflineBalance(player) * 100;
            balanceData.set(player.getName(), balance);
            offlineBalances.put(player.getName(), balance);

            try {
                CqLogger.debug(OfflineEconomy.plugin,"[SAVE] Saving balance of " + balance + " for player " + player.getName());
                balanceData.save(balanceFile);
            } catch (IOException ex) {
                CqLogger.debug(OfflineEconomy.plugin,"[SAVE] IOException while saving balances.yml");
            }
        }
    }

    public long loadLongBalance(OfflinePlayer player) {
        if (player != null) {
            if (offlineBalances.get(player.getName()) != null) {
                CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Loading balance of " + offlineBalances.get(player.getName()) + " for player " + player.getName());
                return offlineBalances.get(player.getName());
            } else {
                CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Player " + player.getName() + " has no stored balance.");
                return 0;
            }
        }

        return 0;
    }

    public boolean canAfford(OfflinePlayer player, long amount) {
        if (player != null) {
            CqLogger.debug(OfflineEconomy.plugin,"[AFFORD] Checking balance of " + player.getName());
            if (amount <= loadLongBalance(player)) {
                CqLogger.debug(OfflineEconomy.plugin,"[AFFORD] " + player.getName() + " can afford this transaction.");
                return true;
            } else {
                CqLogger.debug(OfflineEconomy.plugin,"[AFFORD] " + player.getName() + " cannot afford this transaction.");
                return false;
            }
        }
        return false;
    }

    public boolean isStored(OfflinePlayer player) {
        if (player != null) {
            CqLogger.debug(OfflineEconomy.plugin,"[STORED] Checking if " + player.getName() + " is stored.");
            if (offlineBalances.get(player.getName()) != null) {
                CqLogger.debug(OfflineEconomy.plugin,"[STORED] " + player.getName() + " is stored.");
                return true;
            }
        }
        CqLogger.debug(OfflineEconomy.plugin,"[STORED] " + player.getName() + " is NOT stored.");
        return false;
    }

    public void setLongBalance(OfflinePlayer player, long newBalance) {
        if (player != null) {
            CqLogger.debug(OfflineEconomy.plugin,"[SET] Storing balance of " + newBalance + " for player " + player.getName());
            offlineBalances.put(player.getName(), newBalance);

            File balanceFile = new File(OfflineEconomy.dataFolder, "balances.yml");

            FileConfiguration balanceData = YamlConfiguration.loadConfiguration(balanceFile);
            balanceData.set(player.getName(), newBalance);

            try {
                //CqLogger.debug(OfflineEconomy.plugin,"[SET] Saving balance of " + newBalance + " for player " + player.getName());
                balanceData.save(balanceFile);
                return;
            } catch (IOException ex) {
                CqLogger.debug(OfflineEconomy.plugin,"[SET] IOException while saving balances.yml");
            }

        } else {
            CqLogger.debug(OfflineEconomy.plugin,"[SET] Could not set balance for player: " + player.getName());
        }
    }
}
