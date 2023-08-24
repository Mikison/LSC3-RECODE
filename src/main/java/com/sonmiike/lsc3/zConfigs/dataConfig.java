package com.sonmiike.lsc3.zConfigs;

import com.sonmiike.lsc3.LSC3;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class dataConfig {

    private LSC3 plugin;

    public dataConfig(LSC3 plugin) {
        this.plugin = plugin;
    }

    private static File dataFile;
    private static FileConfiguration dataConfigFile;


    public static void setupDataConfig() {

        dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("LSC3").getDataFolder(), "data.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        dataConfigFile = YamlConfiguration.loadConfiguration(dataFile);
    }

    public static FileConfiguration getDataCfg() {
        return dataConfigFile;
    }

    public static void save() {
        try {
            dataConfigFile.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reload() {
        dataConfigFile = YamlConfiguration.loadConfiguration(dataFile);

    }
}
