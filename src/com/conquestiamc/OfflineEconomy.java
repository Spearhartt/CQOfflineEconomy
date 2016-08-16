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

import java.io.File;

/**
 * Created by Spearhartt on 8/14/2016.
 */
public class OfflineEconomy extends JavaPlugin {
    public static OfflineEconomy OfflineEconomy;
    private static OfflineEconomy instance;
    private static CommandModule commandHandler;
    private static Balances config;
    private static PeriodicCheck checker;
    public static Economy econ;
    static String dataFolder;
    public static Plugin plugin;

    public static String PLUGIN_LABEL = ChatColor.GOLD + "[" + ChatColor.GREEN + "Offline Economy" + ChatColor.GOLD + "]" + ChatColor.WHITE;
    public static String CQ = ChatColor.BLUE + "[" + ChatColor.YELLOW + "CQ" + ChatColor.BLUE + "]" + ChatColor.GRAY;

    public void onEnable() {
        CqLogger.debug("Loading plugin: " + PLUGIN_LABEL);
        instance = this;
        OfflineEconomy = this;
        plugin = this;

        commandHandler = new CommandModule();
        config = new Balances();

        checker = new PeriodicCheck();
        checker.periodicCheck();
        checker.isLoaded();

        File dir = this.getDataFolder();
        if (!dir.isDirectory()) {
            dir.mkdir();
            CqLogger.debug(PLUGIN_LABEL + "Creating plugin folder.");
        }

        registerEvents();

        dataFolder = dir.getPath();

        config.load();

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (rsp != null) {
            econ = rsp.getProvider();
            CqLogger.debug(PLUGIN_LABEL + "Loaded economy provider.");
        } else {
            CqLogger.debug(PLUGIN_LABEL + "Economy provider not detected. This plugin will be disabled.");
            getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void onDisable() {
        checker.savePlayers();
    }

    private void registerEvents() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new PlayerListener(), this);
    }

}
