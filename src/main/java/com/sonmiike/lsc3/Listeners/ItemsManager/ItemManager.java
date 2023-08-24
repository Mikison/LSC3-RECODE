package com.sonmiike.lsc3.Listeners.ItemsManager;

import com.sonmiike.lsc3.LSC3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {



    private static final Map<String, ItemStack> customItems = new HashMap<>();

    public ItemManager(FileConfiguration itemConfig) {
        ConfigurationSection itemsSection = itemConfig.getConfigurationSection("items");

        if (itemsSection == null) {
            Bukkit.getLogger().severe("Setup items.yml");
            return;
        }
        for (String itemCategory : itemsSection.getKeys(false)) {
                ConfigurationSection pdc_name = itemsSection.getConfigurationSection(itemCategory);
                for (String PDC_name : pdc_name.getKeys(false)) {
                    ConfigurationSection PDC_section = pdc_name.getConfigurationSection(PDC_name);
                    ConfigurationSection section = PDC_section.getConfigurationSection("item");
                    ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(section.getString("material")), 1);
                    itemBuilder.lore(section.getStringList("lore"));
                    itemBuilder.name(section.getString("name"));
                    itemBuilder.skullTexture(section.getString("customHeadTexture"));
                    if (itemCategory.equals("hearts")) {
                        itemBuilder.persistentData(new NamespacedKey(LSC3.getPlugin(), "heart"), PersistentDataType.STRING, PDC_name.toLowerCase());
                    } else {
                        itemBuilder.persistentData(new NamespacedKey(LSC3.getPlugin(), "beacon"), PersistentDataType.STRING, PDC_name.toLowerCase());
                    }
                    customItems.put(PDC_name.toLowerCase(), itemBuilder.build());
            }
        }
    }

    public static ItemStack getItem(String key) {
        if (customItems.get(key) == null) return null;
        return customItems.get(key.toLowerCase());
    }
}
