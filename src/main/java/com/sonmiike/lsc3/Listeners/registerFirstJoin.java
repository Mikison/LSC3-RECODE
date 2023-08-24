package com.sonmiike.lsc3.Listeners;

import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.zConfigs.dataConfig;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class registerFirstJoin implements Listener {

    private static LSC3 plugin;
     public static FileConfiguration dataCfg = dataConfig.getDataCfg();
    public registerFirstJoin(LSC3 plugin) {
        registerFirstJoin.plugin = plugin;
    }
//    beaconsToggled: true
//    profiles:
//          UUID:
    //          eliminated: false/true
    //          banTime: 0
    //          maxHealth:
    //          joinAfterBeacon:


//beacons:
    // Mikis:
        // time-to-break:


    @EventHandler
    public static void firstPlayerJoinRegisterToDataConfig(PlayerJoinEvent event) {
        ConfigurationSection profileSection = dataCfg.getConfigurationSection("profiles");
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            ConfigurationSection playerSection = profileSection.createSection(player.getUniqueId().toString());
            playerSection.set("eliminated", false);
            playerSection.set("banTime", 0);
            playerSection.set("maxHealth", plugin.getConfig().getInt(".settings.hearts.maximum"));
            playerSection.set("joinAfterBeacon", true);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(plugin.getConfig().getInt(".settings.hearts.starter"));
            dataConfig.save();
        }
    }


//    @EventHandler
//    public void blockPlaceEventTest(BlockPlaceEvent event) {
//        Player player = event.getPlayer();
//        List<Component> listToSend = (util.transferStringtoComponentNOTLORE(plugin.messagess.getMessageConfig().getStringList("messagess.EVENT_CONSUME_BEACON_BROADCAST")));
//        for (Component list: listToSend) {
//            player.sendMessage(list);
//        }
//    }
}

