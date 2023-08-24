package com.sonmiike.lsc3.zConfigs;

import com.sonmiike.lsc3.LSC3;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class itemConfig {

    private LSC3 plugin;
    public itemConfig(LSC3 plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    private FileConfiguration itemConfig = null;
    private File itemConfigFile = null;

    public void reloadConfig() {
        if (this.itemConfigFile == null)
            this.itemConfigFile = new File(this.plugin.getDataFolder(), "items.yml");

        this.itemConfig = YamlConfiguration.loadConfiguration(this.itemConfigFile);

        InputStream defaultStream = this.plugin.getResource("items.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.itemConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getItemConfig() {
        if (this.itemConfig == null) reloadConfig();
        return this.itemConfig;
    }

    public void saveConfig() {
        if (this.itemConfig == null || this.itemConfigFile == null)
            return;
        try {
            this.getItemConfig().save(this.itemConfigFile);
        } catch (IOException e) {
            this.plugin.getLogger().severe("Nie dziala kurwa");
        }
    }

    public void saveDefaultConfig() {
        if (this.itemConfigFile == null) {
            this.itemConfigFile = new File(this.plugin.getDataFolder(), "items.yml");
        }

        if (!this.itemConfigFile.exists())
            this.plugin.saveResource("items.yml", false);
    }
}
