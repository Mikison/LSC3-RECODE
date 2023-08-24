package com.sonmiike.lsc3.Commands.A_Commands.subCommands;

import com.sonmiike.lsc3.Commands.subcommand;
import com.sonmiike.lsc3.Listeners.ItemsManager.ItemManager;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class giveitem extends subcommand {

    private  LSC3 plugin;

    public giveitem(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "giveitem";
    }

    @Override
    public Component getDescription() {
        return null;
    }

    @Override
    public Component getSyntax() {
        return util.mm("<click:suggest_command:/lifesteal giveitem><hover:show_text:'<aqua>/lifesteal giveitem [player] [item_name] [amount] <-s><newline><newline><gray>Giving custom item to player'>  <red>• <gray>/lifesteal giveitem [player] [item_name] [amount] <-s></hover></click>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 4 || args.length > 5) {
            sender.sendMessage(getSyntax());
            return;
        }

        if (args.length == 4) {
            if (args[1].equals("*")) {
                int count = 0;
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (!giveItem(sender, args, target)) return;
                    count++;
                    target.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_GIVEITEM_SUCCESS_RECEIVER"), Placeholder.parsed("prefix",plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("amount", args[3]), Placeholder.component("item_name", ItemManager.getItem(args[2]).displayName()), Placeholder.parsed("sender", sender.getName())));
                }
                sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_GIVEITEM_SUCCESS_SENDER_ALL"), Placeholder.parsed("amount", args[3]), Placeholder.component("item_name", ItemManager.getItem(args[2]).displayName()), Placeholder.parsed("count", String.valueOf(count)), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
                if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (!giveItem(sender, args, target)) return;
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_GIVEITEM_SUCCESS_SENDER"), Placeholder.parsed("amount", args[3]), Placeholder.component("item_name", ItemManager.getItem(args[2]).displayName()), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", target.getName())));
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            target.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_GIVEITEM_SUCCESS_RECEIVER"), Placeholder.parsed("prefix",plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("amount", args[3]), Placeholder.component("item_name", ItemManager.getItem(args[2]).displayName()), Placeholder.parsed("sender", sender.getName())));
            return;
        }

        if (!args[4].equals("-s")) {
            sender.sendMessage(getSyntax());
            return;
        }
        if (args[1].equals("*")) {
            int count = 0;
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (!giveItem(sender, args, target)) return;
                count++;
            }
            sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_GIVEITEM_SUCCESS_SENDER_SILENT_ALL"), Placeholder.parsed("amount", args[3]), Placeholder.component("item_name", ItemManager.getItem(args[2]).displayName()), Placeholder.parsed("count", String.valueOf(count)), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.3F, 1);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (!giveItem(sender, args, target)) return;
        sender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_GIVEITEM_SUCCESS_SENDER_SILENT"), Placeholder.parsed("amount", args[3]), Placeholder.component("item_name", ItemManager.getItem(args[2]).displayName()), Placeholder.parsed("prefix", plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", target.getName())));
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
            arguments1.add("heart_tier1");
            arguments1.add("heart_tier2");
            arguments1.add("heart_tier3");
            arguments1.add("beacon_tier1");
            arguments1.add("beacon_tier2");
            arguments1.add("beacon_tier3");
            arguments1.add("beacon_ghost");
            return arguments1;
        }
        if (args.length == 4) {
            List<String> arguments2 = new ArrayList<>();
            arguments2.add("amount");
            return arguments2;
        }
        if (args.length == 5) {
            List<String> arguments3 = new ArrayList<>();
            arguments3.add("-s");
            return arguments3;
        }
        return empty;
    }




    private boolean giveItem(CommandSender sender, String[] args, Player player) {
        if (!util.isInt(args[3])) {
            sender.sendMessage(util.mm(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_INVALID_VALUE").replace("<prefix>",plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return false;
        }



        if (ItemManager.getItem(args[2].toLowerCase()) == null) {
            sender.sendMessage(util.mm(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_GIVEITEM_WRONGITEM").replace("<prefix>",plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return false;
        }

        for (int i = 0; i < Integer.parseInt(args[3]); i++) {
            player.getInventory().addItem(ItemManager.getItem(args[2].toLowerCase()));
        }
        return true;
    }
}
