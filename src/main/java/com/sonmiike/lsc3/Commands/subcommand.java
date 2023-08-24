package com.sonmiike.lsc3.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class subcommand {


    public abstract String getName();
    public abstract Component getDescription();
    public abstract Component  getSyntax();

    public abstract void perform(CommandSender sender, String args[]);
    public abstract List<String> getSubcommandArguments(CommandSender sender, String args[]);
}
