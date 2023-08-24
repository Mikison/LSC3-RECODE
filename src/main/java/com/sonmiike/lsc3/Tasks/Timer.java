package com.sonmiike.lsc3.Tasks;

import com.sonmiike.lsc3.LSC3;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {

    private boolean running;
    private int time;

    public Timer(boolean running, int time) {
        this.running = running;
        this.time = time;
        run();
    }


    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isRunning()) return;
                setTime(getTime() - 1);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"say " + getTime());
            }
        }.runTaskTimer(LSC3.getPlugin(), 20L, 20L);
    }
}
