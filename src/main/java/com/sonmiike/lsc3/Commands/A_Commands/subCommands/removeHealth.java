package com.sonmiike.lsc3.Commands.A_Commands.subCommands;

import com.sonmiike.lsc3.Commands.subcommand;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class removeHealth extends subcommand {

    private LSC3 plugin;

    public removeHealth(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Component getDescription() {
        return null;
    }

    @Override
    public Component getSyntax() {
        return util.mm("<click:suggest_command:/lifesteal remove><hover:show_text:'<aqua>/lifesteal remove [player] [amount] <-s><newline><newline><gray>Removing specified amount health from player'>  <red>• <gray>/lifesteal remove [player] [amount] <-s></hover></click>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        if (args.length < 3 || args.length > 4) {
            sender.sendMessage(getSyntax());
            return;
        }
        if (args.length == 3) {
            if (args[1].equals("*")) {
                int count = 0;
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (!commandMethod(sender, target, args)) return;
                    count++;
                    target.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_REMOVE_SUCCESS_RECEIVER"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("sender", sender.getName()), Placeholder.parsed("amount", String.valueOf(args[2]))));
                }
                sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_REMOVE_SUCCESS_SENDER_ALL"), Placeholder.parsed("amount", String.valueOf(args[2])), Placeholder.parsed("count" , String.valueOf(count)), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
                if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (!commandMethod(sender, target, args)) return;
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_REMOVE_SUCCESS_SENDER"), Placeholder.parsed("amount", String.valueOf(args[2])), Placeholder.parsed("target", target.getName()), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            target.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_REMOVE_SUCCESS_RECEIVER"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("sender", sender.getName()), Placeholder.parsed("amount", String.valueOf(args[2]))));
            return;
        }
        if (!args[3].equals("-s")) {
            sender.sendMessage(getSyntax());
            return;
        }
        if (args[1].equals("*")) {
            int count = 0;
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (!commandMethod(sender, target, args)) return;
                count++;
            }
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_REMOVE_SUCCESS_SENDER_SILENT_ALL"), Placeholder.parsed("amount", String.valueOf(args[2])), Placeholder.parsed("count" , String.valueOf(count)), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (!commandMethod(sender, target, args)) return;
        sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_REMOVE_SUCCESS_SENDER_SILENT"), Placeholder.parsed("amount", String.valueOf(args[2])), Placeholder.parsed("target", target.getName()), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
        if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);

    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        List<String> empty = new ArrayList<>();

        if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            playerNames.addAll(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            playerNames.add("*");
            return playerNames;
        }
        if (args.length == 3) {
            List<String> arguments1 = new ArrayList<>();
            arguments1.add("amount");
            return arguments1;
        }
        if (args.length == 4) {
            List<String> arguments2 = new ArrayList<>();
            arguments2.add("-s");
            return arguments2;
        }
        return empty;
    }



    private boolean commandMethod(CommandSender sender, Player target, String[] args) {
        if (target == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_INVALID_PLAYER"), Placeholder.parsed("target", args[1]), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return false;
        }

        if (!util.isInt(args[2])) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_INVALID_VALUE"), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return false;
        }

        double PlayerHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (PlayerHealth / 2 - Double.parseDouble(args[2]) < 1 * 2) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_REMOVE_EXCEEDING_VALUE"), Placeholder.parsed("target", target.getName()), Placeholder.parsed("amount", String.valueOf(PlayerHealth / 2)),  Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return false;
        }
        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(PlayerHealth - Double.parseDouble(args[2]) * 2);
        return true;
    }
}
