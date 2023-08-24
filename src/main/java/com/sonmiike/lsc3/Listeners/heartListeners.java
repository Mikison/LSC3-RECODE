package com.sonmiike.lsc3.Listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.sonmiike.lsc3.Listeners.ItemsManager.ItemManager;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.zConfigs.dataConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class heartListeners implements Listener {

    private LSC3 plugin;

    public heartListeners(LSC3 plugin) {
        this.plugin = plugin;
    }

    private HashMap<UUID, Long> PlayerCooldowns = new HashMap<>();

    @EventHandler
    public void onHeartEat(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            if (meta == null) return;
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (!data.has(new NamespacedKey(LSC3.getPlugin(), "heart"), PersistentDataType.STRING)) return;
            String heart_tier = data.get(new NamespacedKey(LSC3.getPlugin(), "heart"), PersistentDataType.STRING);
            ConfigurationSection section = LSC3.getPlugin().items.getItemConfig().getConfigurationSection("items.hearts." + heart_tier + ".settings");
            if (section == null) {
                Bukkit.getLogger().severe("Settingsy sa puste kurwa");
                return;
            }
            int howManyHearts = section.getInt("hearts") * 2;
            int cooldown = section.getInt("cooldown");
            double PlayerHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            if (PlayerHealth / 2 + howManyHearts / 2 > dataConfig.getDataCfg().getInt("profiles." + player.getUniqueId() + ".maxHealth")) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.EVENT_CONSUME_HEART_OVER_THRESHOLD"),Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("amount", String.valueOf(dataConfig.getDataCfg().getInt("profiles." + player.getUniqueId() + ".maxHealth")))));
                return;
            }
            if (PlayerCooldowns.containsKey(player.getUniqueId())) {
                long secondsLeft = ((PlayerCooldowns.get(player.getUniqueId()) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.EVENT_CONSUME_HEART_COOLDOWN"),Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("timeleft", String.valueOf(secondsLeft))));

                    return;
                }
            }
            player.playSound(player, Sound.ENTITY_PLAYER_BURP, 0.5F, 1);
            PlayerCooldowns.put(player.getUniqueId(), System.currentTimeMillis());

            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth + howManyHearts);
            if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
                if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "heart"), PersistentDataType.STRING)) return;
            }
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "heart"), PersistentDataType.STRING)) player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);

            }
        }
    }

    @EventHandler
    public void onHeartPlace (BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "heart"), PersistentDataType.STRING)) {
                event.setCancelled(true);
                return;
            }
        }
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "heart"), PersistentDataType.STRING)) event.setCancelled(true);

        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        Player killer = event.getPlayer();
        Location location = victim.getLocation();
        if (killer != null && killer != victim && victim.getAddress() != killer.getAddress()) {
            victim.getWorld().dropItem(location, ItemManager.getItem("heart_tier1"));

        }
        double PlayerHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (PlayerHealth/2 > plugin.getConfig().getInt("settings.hearts.starter")) {
            victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth - 2);
        }
    }

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        Player victim = event.getPlayer();
        double PlayerHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (PlayerHealth /2  <=  plugin.getConfig().getInt("settings.hearts.unban")) {
            Date date = new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 24);
            victim.banPlayer("Zostales wyeliminowany!", date, "CONSOLE", true);
            dataConfig.getDataCfg().set("profiles." + victim.getUniqueId() + ".eliminated", true);
            long timeleft = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(victim.getName()).getExpiration().getTime() - new Date().getTime();
            dataConfig.getDataCfg().set("profiles." + victim.getUniqueId() + ".banTime", timeleft/1000);
            dataConfig.save();

        }
    }
}
