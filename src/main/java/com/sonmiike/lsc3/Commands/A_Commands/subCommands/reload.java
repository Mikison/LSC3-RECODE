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

public class reload extends subcommand {

    private LSC3 plugin;

    public reload(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public Component getDescription() {
        return null;
    }

    @Override
    public Component getSyntax() {
        return util.mm("<click:suggest_command:/lifesteal reload><hover:show_text:'<aqua>/lifesteal reload <newline><newline><gray>Reloading plugin configs and saving them'>  <red>• <gray>/lifesteal reload</hover></click>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(getSyntax());
            return;
        }

        dataConfig.reload();
        plugin.messagess.reloadConfig();
        plugin.items.reloadConfig();
        List<String> reloadMessage = plugin.messagess.getMessageConfig().getStringList("messagess.COMMAND_RELOAD");
        for (String line : reloadMessage) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(line, Placeholder.parsed("prefix",plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
        }
        if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5F, 1);
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        List<String> empty = new ArrayList<>();
        return empty;
    }
}
