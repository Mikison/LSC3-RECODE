package com.sonmiike.lsc3.Commands.A_Commands;

import com.sonmiike.lsc3.Commands.A_Commands.subCommands.*;
import com.sonmiike.lsc3.Commands.subcommand;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class A_CommandManager implements TabExecutor {
    private final LSC3 plugin;
    private ArrayList<subcommand> a_SubCommands = new ArrayList<>();

    public A_CommandManager(LSC3 plugin) {
        this.plugin = plugin;
        a_SubCommands.add(new setHealth(plugin));
        a_SubCommands.add(new setMaxHealth(plugin));
        a_SubCommands.add(new addHealth(plugin));
        a_SubCommands.add(new removeHealth(plugin));
        a_SubCommands.add(new eliminate(plugin));
        a_SubCommands.add(new revive(plugin));
        a_SubCommands.add(new giveitem(plugin));
        a_SubCommands.add(new toggleRevives(plugin));
        a_SubCommands.add(new reload(plugin));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("lsc.admin")) return true; //TODO SEND ERROR FOR PLAYER WITHOUT PERMS FROM CONFIG
        if (args.length == 0) {
            sender.sendMessage(util.mm("<dark_gray>━━━━━━━━━━━━━━━ <gradient:#FF1F00:#FF06B9>LSC Commands</gradient> ━━━━━━━━━━━━━━━"));
            sender.sendMessage("\n");
            sender.sendMessage(util.mm("  <white><u>Available Commands:<reset><gray> [] = Required, <> = Optional"));
            sender.sendMessage("\n");
            for (int i = 0; i < getA_SubCommands().size(); i++) {
                sender.sendMessage(getA_SubCommands().get(i).getSyntax());
            }
            sender.sendMessage(util.mm("<click:suggest_command:/bind><hover:show_text:'<aqua>/bind [player]<newline><newline><gray>Bind a player to beacon'>  <red>• <gray>/bind [player]</hover></click>"));
            sender.sendMessage(util.mm("<click:suggest_command:/check><hover:show_text:'<aqua>/check [player]<newline><newline><gray>Check for player's information'>  <red>• <gray>/check [player]</hover></click>"));
            sender.sendMessage("\n");
            sender.sendMessage(util.mm("<yellow><b>TIP:<reset><gray> Try to <white><u><hover:show_text:'<light_purple>Hover on the commands to see the description.'>hover<reset><gray> or <white><u><hover:show_text:'<light_purple>Click on the commands to insert them in the chat.'>click<reset><gray> on the commands."));
            sender.sendMessage("\n");
            sender.sendMessage(util.mm("<dark_gray>━━━━━━━━━━━━━━━<gradient:#FF1F00:#FF06B9>LSC Commands</gradient> ━━━━━━━━━━━━━━━"));
        }
        if (args.length > 0) {
            for (int i = 0; i < getA_SubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getA_SubCommands().get(i).getName())) {
                    getA_SubCommands().get(i).perform(sender, args);
                }
            }
        }
        return true;
    }

    public ArrayList<subcommand> getA_SubCommands() {
        return a_SubCommands;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1){
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getA_SubCommands().size(); i++){
                subcommandsArguments.add(getA_SubCommands().get(i).getName());
            }

            return subcommandsArguments;
        }else if(args.length >= 2){
            for (int i = 0; i < getA_SubCommands().size(); i++){
                if (args[0].equalsIgnoreCase(getA_SubCommands().get(i).getName())){
                    return getA_SubCommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }

        return null;
    }

}
