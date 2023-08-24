package com.sonmiike.lsc3.Commands.A_Commands.subCommands;

import com.sonmiike.lsc3.Commands.subcommand;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import com.sonmiike.lsc3.zConfigs.dataConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class toggleRevives extends subcommand {

    private LSC3 plugin;

    public toggleRevives(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "togglerevives";
    }

    @Override
    public Component getDescription() {
        return null;
    }

    @Override
    public Component getSyntax() {
        return util.mm("<click:suggest_command:/lifesteal togglerevives><hover:show_text:'<aqua>/lifesteal togglerevives [true/false]<newline><newline><gray>Toggling revive beacons'>  <red>• <gray>/lifesteal togglerevives [true/false]</hover></click>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        if (args.length == 1) {
            if (dataConfig.getDataCfg().getBoolean("beaconsToggled")) {
                sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_TOGGLEREVIVES_STATUS_ON"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_TOGGLEREVIVES_STATUS_OFF"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            }
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_TOGGLEREVIVES_NO_INPUT"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(getSyntax());
            return;
        }
        if (args[1].equals("false")) {
            dataConfig.getDataCfg().set("beaconsToggled", false);
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_TOGGLEREVIVES_DISABLED"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return;
        }
        if (args[1].equals("true")) {
            dataConfig.getDataCfg().set("beaconsToggled", true);
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_TOGGLEREVIVES_ENABLED"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return;
        }
        sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_TOGGLEREVIVES_INVALID_INPUT"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        List<String> empty = new ArrayList<>();

        if (args.length == 2) {
            List<String> arguments =new ArrayList<>();
            arguments.add("true");
            arguments.add("false");
            return arguments;
        }
        return empty;
    }
}
