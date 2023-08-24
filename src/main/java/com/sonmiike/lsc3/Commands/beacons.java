package com.sonmiike.lsc3.Commands;

import com.sonmiike.lsc3.Data.beaconsData;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Tasks.Timer;
import com.sonmiike.lsc3.Utils.util;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import com.sonmiike.lsc3.Listeners.beaconListener;

import java.util.Map;

public class beacons implements CommandExecutor {

    private LSC3 plugin;

    public beacons(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Timer timer = LSC3.getPlugin().getTimer();
        Map<Location, beaconsData> currentBeacons =  beaconListener.getBeaconsDataMap();
        sender.sendMessage(util.mm("<dark_gray>━━━━━━━━━━━━━━━ <gradient:#FF1F00:#FF06B9>Online Beacons</gradient> ━━━━━━━━━━━━━━━"));
        sender.sendMessage("\n");
        int i = 1;
        for (beaconsData beacon : currentBeacons.values()) {
            if (!beacon.isSilent()) {
                sender.sendMessage(util.mm("<green>" + i +".<gray><newline>    Who: <#ff1c55>" + beacon.getPlayerToRevive().getName() + "<newline><gray>    Durability:<#ff1c55>  " + beacon.getDurability() + "<newline><gray>    Time Left:<#ff1c55>  " + timer.getTime() + "<newline><gray>    Placed by: <#ff1c55> " + beacon.getWhoPlaced().getName()));
            }
        }
        sender.sendMessage("\n");
        sender.sendMessage(util.mm("<dark_gray>━━━━━━━━━━━━━━━ <gradient:#FF1F00:#FF06B9>Online Beacons</gradient> ━━━━━━━━━━━━━━━"));
        return true;
    }
}
