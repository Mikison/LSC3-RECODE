package com.sonmiike.lsc3;

import com.sonmiike.lsc3.Commands.A_Commands.A_CommandManager;
import com.sonmiike.lsc3.Commands.beacons;
import com.sonmiike.lsc3.Commands.bind;
import com.sonmiike.lsc3.Commands.check;
import com.sonmiike.lsc3.Listeners.ItemsManager.ItemManager;
import com.sonmiike.lsc3.Listeners.beaconListener;
import com.sonmiike.lsc3.Listeners.heartListeners;
import com.sonmiike.lsc3.Listeners.registerFirstJoin;
import com.sonmiike.lsc3.Tasks.Timer;
import com.sonmiike.lsc3.zConfigs.dataConfig;
import com.sonmiike.lsc3.zConfigs.itemConfig;
import com.sonmiike.lsc3.zConfigs.messageConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class LSC3 extends JavaPlugin {

    public static LSC3 getPlugin() {
        return plugin;
    }

    private ItemManager itemManager;
    private Timer timer;
    private static LSC3 plugin;
    public itemConfig items;
    public messageConfig messagess;


    @Override
    public  void onLoad() {
        plugin = this;
    }
    @Override
    public void onEnable() {

        this.items = new itemConfig(this);
        this.messagess = new messageConfig(this);

        itemManager = new ItemManager(items.getItemConfig());
        timer = new Timer(false, 0);
        setupConfigs();


        getCommand("life").setExecutor(new A_CommandManager(this));
        getCommand("bind").setExecutor(new bind(this));
        getCommand("check").setExecutor(new check(this));
        getCommand("beacons").setExecutor(new beacons(this));

        getServer().getPluginManager().registerEvents(new registerFirstJoin(this), this);
        getServer().getPluginManager().registerEvents(new heartListeners(this), this);
        getServer().getPluginManager().registerEvents(new beaconListener(this), this);
    }

    @Override
    public void onDisable() {
        saveConfig();
        dataConfig.save();
        this.items.saveConfig();
    }
    public void setupConfigs() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        dataConfig.setupDataConfig();
        dataConfig.getDataCfg().options().copyDefaults(true);
        if (dataConfig.getDataCfg().getKeys(false).size() == 0) {
            dataConfig.getDataCfg().addDefault("beaconsToggled", false);
            dataConfig.getDataCfg().createSection(".profiles");
        }
        dataConfig.save();
    }

    public Timer getTimer() {
        return timer;
    }
}
