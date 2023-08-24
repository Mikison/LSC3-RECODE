package com.sonmiike.lsc3.Listeners;

import com.sonmiike.lsc3.Data.beaconsData;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Tasks.Timer;
import com.sonmiike.lsc3.Utils.util;
import com.sonmiike.lsc3.zConfigs.dataConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class beaconListener implements Listener {

    private LSC3 plugin;

    public beaconListener(LSC3 plugin) {
        this.plugin = plugin;

    }
    Timer timer = LSC3.getPlugin().getTimer();
    private static Map<Location, beaconsData > beaconsDataMap = new HashMap<>();
    private Map<Player, Boolean > hasBeacon = new HashMap<>();

    OfflinePlayer playerFromPCD;
    @SuppressWarnings("null")
    @EventHandler
    public void beaconPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (hasBeacon.get(player) != null) {
            event.setCancelled(true);
            return;
        }
        if (!dataConfig.getDataCfg().getBoolean("beaconsToggled") && meta.getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "beacon"))) {
            player.sendMessage(util.mm(plugin.messagess.getMessageConfig().getString("messagess.EVENT_CONSUME_BEACON_TOGGLED").replace("<prefix>", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            event.setCancelled(true);
            return;
        }
        if (event.getBlock().getType() == Material.PLAYER_WALL_HEAD) {
            player.sendMessage(util.mm(plugin.messagess.getMessageConfig().getString("messagess.EVENT_PLACE_BEACON_ON_WALL").replace("<prefix>", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));

            event.setCancelled(true);
            return;
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "beacon")) && player.getInventory().getItemInOffHand().getItemMeta() != null) {
                player.sendMessage("Only mainhand");
                event.setCancelled(true);
            }
        }
        if (meta != null) {
            if (meta.getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "beacon"))) {
                if (timer.isRunning()) {
                    //TO DO MESSAGE
                    event.setCancelled(true);
                    return;
                }
                if (!meta.getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "whoRevive"), PersistentDataType.STRING)) {
                    player.sendMessage(util.mm(plugin.messagess.getMessageConfig().getString("messagess.EVENT_CONSUME_BEACON_NOTBOUND").replace("<prefix>", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
                    event.setCancelled(true);
                    return;
                }
                String tier = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(LSC3.getPlugin(), "beacon"), PersistentDataType.STRING);
                ConfigurationSection section = plugin.items.getItemConfig().getConfigurationSection("items.beacons." + tier + ".settings");
                int reviveTime = section.getInt("reviveTime");
                int durability = section.getInt("durability");
                boolean silent = section.getBoolean("silent");
                playerFromPCD = Bukkit.getOfflinePlayer(UUID.fromString(meta.getPersistentDataContainer().get(new NamespacedKey(LSC3.getPlugin(), "whoRevive"), PersistentDataType.STRING)));
                if (dataConfig.getDataCfg().get("profiles." + player.getUniqueId() + ".eliminated") == null) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_PLAYER_NOT_IN_DATABASE"), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", player.getName())));
                    event.setCancelled(true);
                    return;
                }

                if (!dataConfig.getDataCfg().getBoolean("profiles." + playerFromPCD.getUniqueId() + ".eliminated")) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.EVENT_CONSUME_BEACON_NOTDEAD"), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", playerFromPCD.getName())));
                    event.setCancelled(true);
                    return;
                }
                beaconsData newBeaconData = new beaconsData(player, location, reviveTime, durability, silent, playerFromPCD);
                hasBeacon.put(player, true);
                timer.setTime(reviveTime);
                timer.setRunning(true);
                task(player ,location, playerFromPCD);
                beaconsDataMap.put(location, newBeaconData);
                player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.EVENT_CONSUME_BEACON_SUCCESS"), Placeholder.parsed("prefix",plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", Objects.requireNonNull(playerFromPCD.getName()))));
                if (!newBeaconData.isSilent()) {
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        List<String> listToSend = plugin.messagess.getMessageConfig().getStringList("messagess.EVENT_CONSUME_BEACON_BROADCAST");
                        for (String line : listToSend) {
                            target.sendMessage(MiniMessage.miniMessage().deserialize(line, Placeholder.parsed("whoplaced", player.getName()), Placeholder.parsed("worldname", location.getWorld().getName()), Placeholder.parsed("coordx", String.valueOf(location.getBlockX())), Placeholder.parsed("coordy", String.valueOf(location.getBlockY())), Placeholder.parsed("coordz", String.valueOf(location.getBlockZ()))));
                            target.playSound(target, Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, SoundCategory.HOSTILE, 0.05F , 1);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("null")
    @EventHandler
    public void beaconBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.PLAYER_HEAD) {
            Player player = event.getPlayer();
            Location location = event.getBlock().getLocation();
            beaconsData beacon = this.beaconsDataMap.get(location);

            if (beacon == null) return;
            if (beacon.getDurability() == 1) {
                event.setDropItems(false);
                event.setCancelled(true);
                List<String> listToSendBeaconbroken = plugin.messagess.getMessageConfig().getStringList("messagess.EVENT_BEACON_BREAK_BROKEN");
                Player placerThatPlaced = beacon.getWhoPlaced();
                for (String line2 : listToSendBeaconbroken) placerThatPlaced.sendMessage(MiniMessage.miniMessage().deserialize(line2, Placeholder.parsed("0", player.getName()), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));

                event.getBlock().setType(Material.AIR);
                timer.setRunning(false);
                timer.setTime(0);
                beaconsDataMap.remove(location);
                hasBeacon.remove(beacon.getWhoPlaced());
                return;
            }
            List<String> listToSend = plugin.messagess.getMessageConfig().getStringList("messagess.EVENT_BEACON_BREAK_NOTIFY");
            player.playSound(player, Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.5F, 1);
            beacon.setDurability(beacon.getDurability() - 1);
            for (String line : listToSend)  beacon.getWhoPlaced().sendMessage(MiniMessage.miniMessage().deserialize(line, Placeholder.parsed("0", player.getName()), Placeholder.parsed("1", String.valueOf(beacon.getDurability())), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")) ));
            event.setCancelled(true);
        }
    }


    @SuppressWarnings("null")
    @EventHandler
    public void preventPlayerHeadsDestroyment(BlockFromToEvent event) {
        if (event.getToBlock().getType() == Material.PLAYER_HEAD) event.setCancelled(true);
    }
    @SuppressWarnings("null")
    private void task(Player whoPlaced,Location location, OfflinePlayer player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!timer.isRunning()) return;
                if (location.getBlock().getType() != Material.PLAYER_HEAD) {
                    cancel();
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        List<String> listToSendBeaconBrokenBroadcast = plugin.messagess.getMessageConfig().getStringList("messagess.EVENT_BEACON_BREAK_BROADCAST");
                        for (String line : listToSendBeaconBrokenBroadcast) {
                            target.sendMessage(MiniMessage.miniMessage().deserialize(line, Placeholder.parsed("0", playerFromPCD.getName() ) , Placeholder.parsed("1", "Unknown")));
                        }
                    }
                    hasBeacon.remove(whoPlaced);
                    beaconsDataMap.remove(location);
                    timer.setRunning(false);
                    timer.setTime(0);
                    return;
                }
                if (timer.getTime() == 0) {
                    timer.setRunning(false);
                    timer.setTime(0);
                    if (beaconsDataMap.get(location) == null) return;
                    beaconsDataMap.remove(location);
                    List<String> listBeaconReviveBroadcast = plugin.messagess.getMessageConfig().getStringList("messagess.EVENT_CONSUME_BEACON_BROADCAST_REVIVE");
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        for (String line : listBeaconReviveBroadcast) {
                            target.sendMessage(MiniMessage.miniMessage().deserialize(line, Placeholder.parsed("revive", player.getName())));
                            target.playSound(target, Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 0.02F  , 1);
                        }
                    }
                    hasBeacon.remove(whoPlaced);
                    location.getWorld().setType(location,Material.AIR);
                    cancel();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"l revive "+ playerFromPCD.getName());
                }
            }
        }.runTaskTimer(LSC3.getPlugin(), 20L, 20L);
    }

    public static Map<Location, beaconsData> getBeaconsDataMap() {
        return beaconsDataMap;
    }
}
