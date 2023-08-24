package com.sonmiike.lsc3.Commands;

import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class bind implements CommandExecutor {

    private LSC3 plugin;

    public bind(LSC3 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return true;
        if (args.length != 1) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_BIND_INVALID_SYNTAX"), Placeholder.parsed("prefix",plugin.messagess.getMessageConfig().getString("messagess.prefix"))));

            return true;
        }
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return true;
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(new NamespacedKey(LSC3.getPlugin(), "beacon"), PersistentDataType.STRING)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_BIND_NO_BEACON"), Placeholder.parsed("prefix",plugin.messagess.getMessageConfig().getString("messagess.prefix"))));
            return true;
        }
        OfflinePlayer playerToRevive = Bukkit.getOfflinePlayer(args[0]);
        String getLore = data.get(new NamespacedKey(LSC3.getPlugin(), "beacon"), PersistentDataType.STRING);
        ConfigurationSection section = LSC3.getPlugin().items.getItemConfig().getConfigurationSection("items.beacons." + getLore + ".item");
        List<String> lore = section.getStringList("lore");
        player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.messagess.getMessageConfig().getString("messagess.COMMAND_BIND_SUCCESS"), Placeholder.parsed("prefix",plugin.messagess.getMessageConfig().getString("messagess.prefix")), Placeholder.parsed("target", playerToRevive.getName())));
        player.playSound(player, Sound.BLOCK_ANVIL_USE, SoundCategory.AMBIENT, 0.3F, 1);
        data.set(new NamespacedKey(LSC3.getPlugin(), "whoRevive"), PersistentDataType.STRING, playerToRevive.getUniqueId().toString());
        meta.lore(util.transferStringtoComponent(lore, playerToRevive));
        player.getInventory().getItemInMainHand().setItemMeta(meta);
        return true;
    }
}
