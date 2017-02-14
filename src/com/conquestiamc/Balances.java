package com.conquestiamc;

import com.conquestiamc.logging.CqLogger;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class Balances {

    public static HashMap<UUID, Integer> onlinePlayers = new HashMap<>();
    public static HashMap<String, Double> offlineBalances = new HashMap<>();

    private static MongoClient mongoClient;     // How we talk to mongo
    private static DB cqDB;                     // The data base to look through
    private static DBCollection balances;       // The collection (essentially mysql table)

    private static Balances instance;           // We really don't want more than one created.

    /**
     * Need to provide some type of access to the class.
     *
     * @return - instance of this class.
     */
    public static Balances initialize() {
        if (instance == null) {
            instance = new Balances();
        }
        return instance;
    }

    /**
     * Establishes connection to mongo db.
     */
    private Balances() {
        try {
            mongoClient = new MongoClient();
        } catch (UnknownHostException e) {
            CqLogger.severe(OfflineEconomy.OfflineEconomy, "Error connecting to database!");
            Bukkit.getPluginManager().disablePlugin(OfflineEconomy.OfflineEconomy);
            return;
        }
        // Database is same in MySQL.
        cqDB = mongoClient.getDB("Conquestia");
        // Collections are similar to tables.
        balances = cqDB.getCollection("Balances");
    }

    /** Saves a players balance to the balances.yml */
    public void savePlayer(OfflinePlayer player) {
        // This if statement is a tad confusing why .getPlayer() null check?
        if (player != null && player.getPlayer() != null) {
            double balance = new CQPlayer().getOfflineBalance(player);

            // Used to determine if the player exists in DB.
            BasicDBObject search = new BasicDBObject("player", player.getUniqueId());
            DBObject found = balances.findOne(search);

            // Creates new document for updating player info.
            BasicDBObject playerInfo = new BasicDBObject("player", player.getUniqueId());
            playerInfo.put("balance", balance);

            // If the entry exists only update, if not then insert.
            if (found == null) {
                balances.insert(playerInfo);
            } else {
                balances.update(found, playerInfo);
            }

            offlineBalances.put(player.getName(), balance);
        }
    }

    /**
     *
     */
    public double loadBalance(OfflinePlayer player) {
        if (player != null) {
            if (offlineBalances.containsKey(player.getName())) {
                CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Loading balance of " + offlineBalances.get(player.getName()) + " for player " + player.getName());
                return offlineBalances.get(player.getName());
            } else {
                CqLogger.debug(OfflineEconomy.plugin,"[LOAD] Player " + player.getName() + " has no stored balance.");
                // How about we load the balance here?
                BasicDBObject search = new BasicDBObject("player", player.getUniqueId());
                DBObject found = balances.findOne(search);

                if (found == null) {
                    return 0;
                }

                if (found.get("balance") == null) {
                    return 0;
                } else {
                    offlineBalances.put(player.getName(), (Double) found.get("balance"));
                    return offlineBalances.get(player.getName());
                }
            }
        }
        return 0;
    }

    /** Checks if a player can afford a transaction */
    public boolean canAfford(OfflinePlayer player, long amount) {
        if (player != null) {
            CqLogger.debug(OfflineEconomy.plugin,"[AFFORD] Checking balance of " + player.getName());
            if (amount <= loadBalance(player)) {
                CqLogger.debug(OfflineEconomy.plugin,"[AFFORD] " + player.getName() + " can afford this transaction.");
                return true;
            } else {
                CqLogger.debug(OfflineEconomy.plugin,"[AFFORD] " + player.getName() + " cannot afford this transaction.");
                return false;
            }
        }
        return false;
    }

    /** Checks if a player is stored in the balances hashMap */
    public boolean isStored(OfflinePlayer player) {
        if (player != null) {
            CqLogger.debug(OfflineEconomy.plugin,"[STORED] Checking if " + player.getName() + " is stored.");
            if (offlineBalances.containsKey(player.getName())) {
                CqLogger.debug(OfflineEconomy.plugin,"[STORED] " + player.getName() + " is stored.");
                return true;
            }
        }
        CqLogger.debug(OfflineEconomy.plugin,"[STORED] " + player.getName() + " is NOT stored.");
        return false;
    }

    /** Sets the player's balance to the given value */
    public void setBalance(OfflinePlayer player, double newBalance) {
        if (player != null) {
            offlineBalances.put(player.getName(), newBalance);
            BasicDBObject search = new BasicDBObject("player", player.getUniqueId());
            DBObject find = balances.findOne(search);

            BasicDBObject entry = new BasicDBObject("player", player.getUniqueId());
            entry.put("balance", newBalance);

            if (find == null) {
                balances.insert(entry);
            } else {
                balances.update(find, entry);
            }
        }
    }
}
