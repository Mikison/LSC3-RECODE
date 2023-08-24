package com.sonmiike.lsc3.Data;


import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class beaconsData {

    private Player whoPlaced;


    private Location location;
    private int time_seconds;
    private OfflinePlayer playerToRevive;


    private int durability;
    private boolean Silent;

    public beaconsData(Player whoPlaced, Location location, int time_seconds, int durability, boolean Silent, OfflinePlayer playerToRevive) {
        this.whoPlaced = whoPlaced;
        this.location = location;
        this.time_seconds = time_seconds;
        this.durability = durability;
        this.Silent = Silent;
        this.playerToRevive = playerToRevive;
    }

    public Player getWhoPlaced() {
        return whoPlaced;
    }

    public void setWhoPlaced(Player whoPlaced) {
        this.whoPlaced = whoPlaced;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getTime_seconds() {
        return time_seconds;
    }

    public void setTime_seconds(int time_seconds) {
        this.time_seconds = time_seconds;
    }

    public OfflinePlayer getPlayerToRevive() {
        return playerToRevive;
    }

    public void setPlayerToRevive(OfflinePlayer playerToRevive) {
        this.playerToRevive = playerToRevive;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public boolean isSilent() {
        return Silent;
    }

    public void setSilent(boolean silent) {
        Silent = silent;
    }
}
