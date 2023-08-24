package com.sonmiike.lsc3.zConfigs;

import com.sonmiike.lsc3.LSC3;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class messageConfig {
    private LSC3 plugin;

    public messageConfig(LSC3 plugin) {
        this.plugin = plugin;
        saveDefaultMessagesConfig();
    }


    private FileConfiguration messageConfig = null;
    private File messageConfigFile = null;


    public void reloadConfig() {
        if (this.messageConfigFile == null)
            this.messageConfigFile = new File(this.plugin.getDataFolder(), "messagess.yml");

        this.messageConfig = YamlConfiguration.loadConfiguration(this.messageConfigFile);

        InputStream defaultStream = this.plugin.getResource("messagess.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.messageConfig.setDefaults(defaultConfig);
        }
    }


    public FileConfiguration getMessageConfig() {
        if (this.messageConfig == null) reloadConfig();
        return this.messageConfig;
    }

    public void saveConfig() {
        if (this.messageConfig == null || this.messageConfigFile == null)
            return;
        try {
            this.getMessageConfig().save(this.messageConfigFile);
        } catch (IOException e) {
            this.plugin.getLogger().severe("Nie dziala messagess");
        }
    }


    public void saveDefaultMessagesConfig() {
        if (this.messageConfigFile == null)
            this.messageConfigFile = new File(this.plugin.getDataFolder(), "messagess.yml");

        if (!this.messageConfigFile.exists())
            this.plugin.saveResource("messagess.yml", false);
    }
}
