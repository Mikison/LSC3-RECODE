package com.sonmiike.lsc3.Commands;

import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import com.sonmiike.lsc3.zConfigs.dataConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class check implements CommandExecutor {

    private LSC3 plugin;

    public check(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length != 1) {
            sender.sendMessage(util.mm("<click:suggest_command:/check><hover:show_text:'<aqua>/check [player]<newline><newline><gray>Check for player's information'>  <red>• <gray>/check [player]</hover></click>"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (dataConfig.getDataCfg().get("profiles." + target.getUniqueId()) == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_PLAYER_NOT_IN_DATABASE"),Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", target.getName())));
            return true;
        }
        List<String> infoList = plugin.messagess.getMessageConfig().getStringList("messagess.COMMAND_STATUS_SUCCESS_SENDER");
        for (String line : infoList) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(line,Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", target.getName()), Placeholder.parsed("iseliminated", String.valueOf(dataConfig.getDataCfg().getBoolean("profiles." + target.getUniqueId() + ".eliminated"))), Placeholder.parsed("time", (dataConfig.getDataCfg().getInt("profiles." + target.getUniqueId() + ".banTime")) > 0 ? String.valueOf(dataConfig.getDataCfg().getInt("profiles." + target.getUniqueId() + ".banTime")) : "N/A" )));
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
        }
        if (!target.isBanned()) return true;
        long timeleft = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(target.getName()).getExpiration().getTime() - new Date().getTime();
        dataConfig.getDataCfg().set("profiles." + target.getUniqueId() + ".banTime", timeleft/1000);
        dataConfig.save();
        dataConfig.reload();

        return true;
    }
}
