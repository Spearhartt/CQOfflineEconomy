package com.conquestiamc;

import com.conquestiamc.Listeners.PlayerListener;
import com.conquestiamc.commands.CommandModule;
import com.conquestiamc.logging.CqLogger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Created by Spearhartt on 8/14/2016.
 */
public class EconTracker extends JavaPlugin {
    public static EconTracker OfflineEconomy;
    private static PlayerHandler pHandler;
    public static Economy econ;
    public static Plugin plugin;



    public static String PLUGIN_LABEL = ChatColor.GOLD + "[" + ChatColor.GREEN + "Offline Economy" + ChatColor.GOLD + "]" + ChatColor.WHITE;
    public static String CQ = ChatColor.BLUE + "[" + ChatColor.YELLOW + "CQ" + ChatColor.BLUE + "]" + ChatColor.GRAY;

    public void onEnable() {
        plugin = this;
        CqLogger.debug(plugin, "Loading plugin");
        OfflineEconomy = this;
        plugin = this;

        CommandModule commandHandler = new CommandModule();

        pHandler = new PlayerHandler();
        pHandler.isLoaded();

        registerEvents();

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (rsp != null) {
            econ = rsp.getProvider();
            CqLogger.debug(plugin, "Loaded economy provider.");
        } else {
            CqLogger.debug(plugin, "Economy provider not detected. This plugin will be disabled.");
            getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void onDisable() {
        pHandler.saveAllPlayers();
    }

    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new PlayerListener(), this);
    }

}