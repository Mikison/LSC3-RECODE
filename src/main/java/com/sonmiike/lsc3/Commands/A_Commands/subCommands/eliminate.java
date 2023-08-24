package com.sonmiike.lsc3.Commands.A_Commands.subCommands;

import com.sonmiike.lsc3.Commands.subcommand;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import com.sonmiike.lsc3.zConfigs.dataConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class eliminate extends subcommand {
    private LSC3 plugin;

    public eliminate(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "eliminate";
    }

    @Override
    public Component getDescription() {
        return null;
    }

    @Override
    public Component getSyntax() {
        return util.mm("<click:suggest_command:/lifesteal eliminate><hover:show_text:'<aqua>/lifesteal eliminate [player] <-s><newline><newline><gray>Eliminating specified player'>  <red>• <gray>/lifesteal eliminate [player] <-s></hover></click>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2 || args.length > 3) {
            sender.sendMessage(getSyntax());
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        if (dataConfig.getDataCfg().get("profiles." + player.getUniqueId() + ".eliminated") == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_PLAYER_NOT_IN_DATABASE"), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", player.getName())));
            return;
        }

        if (dataConfig.getDataCfg().getBoolean("profiles." + player.getUniqueId() + ".eliminated")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_ELIMINATE_ALREADY_ELIMINATED"), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", player.getName())));
            return;
        }
        Date date = new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 24);
        player.banPlayer("Zostales wyeliminowany!", date, "CONSOLE", true);
        dataConfig.getDataCfg().set("profiles." + player.getUniqueId() + ".eliminated", true);
        long timeleft = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(player.getName()).getExpiration().getTime() - new Date().getTime();
        dataConfig.getDataCfg().set("profiles." + player.getUniqueId() + ".banTime", timeleft/1000);
        dataConfig.save();
        if (args.length == 3 && args[2].equals("-s")) {
            if (sender instanceof Player send) send.playSound(send, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_ELIMINATE_SUCCESS_SENDER_SILENT"), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", player.getName())));
            return;
        }
        if (sender instanceof Player send) send.playSound(send, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
        sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_ELIMINATE_SUCCESS_SENDER"), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", player.getName())));
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.EVENT_BAN_BROADCAST"), Placeholder.parsed("prefix" ,plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", player.getName())));
            players.playSound(players, Sound.ENTITY_GHAST_SCREAM, 0.5F, 1);
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        List<String> empty = new ArrayList<>();

        if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            playerNames.addAll(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            return playerNames;
        }
        if (args.length == 3) {
            List<String> arguments2 = new ArrayList<>();
            arguments2.add("-s");
            return arguments2;
        }
        return empty;
    }
}
