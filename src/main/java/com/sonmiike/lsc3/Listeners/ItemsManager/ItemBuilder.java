package com.sonmiike.lsc3.Listeners.ItemsManager;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.sonmiike.lsc3.LSC3;
import com.sonmiike.lsc3.Utils.util;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ItemBuilder {
    private static final String TEXTURE_URL = "http://textures.minecraft.net/texture/";
    private final Map<String, PlayerProfile> textureCache = new HashMap<>();



    private ItemStack item;
    private ItemMeta meta;

    private ItemBuilder() {
    }

    public ItemBuilder(Material material) {
        this(material, 1);

    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, 1);
        this.meta = this.item.getItemMeta();
    }

    public static ItemBuilder modifyItem(ItemStack item) {
        ItemBuilder builder = new ItemBuilder();
        builder.item = item;
        builder.meta = item.getItemMeta();

        return builder;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }
    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantmentAndLevel) {
        for (Map.Entry entry : enchantmentAndLevel.entrySet()) {
            this.meta.addEnchant((Enchantment)entry.getKey(),(Integer) entry.getValue(),true);
        }
        return this;
    }
    public ItemBuilder name(String name) {
        this.meta.displayName(util.mm(name));
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> itemLore = getLore();
        List<Component> newList = new ArrayList<>();
        for (String line : lore) {
            line = line.replace("{PLAYER}", this.meta.getPersistentDataContainer().has(new NamespacedKey(LSC3.getPlugin(), "bindedPlayer")) ? this.meta.getPersistentDataContainer().get(new NamespacedKey(LSC3.getPlugin(), "bindedPlayer"), PersistentDataType.STRING).toString() : "<gray>Not bound to any player. (Use <white>/bind<gray>)" );
            newList.add(util.mm(line));
        }

        meta.lore(newList);

        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);

        return this;
    }

    public ItemBuilder skullTexture(String texture) {
        if (this.item.getType() == Material.PLAYER_HEAD) {
            PlayerProfile cachedProfile = getProfileForTexture(TEXTURE_URL + texture);
            ((SkullMeta) this.meta).setPlayerProfile(cachedProfile);
        }
        return this;
    }

    public <T, Z> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        this.meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemStack build() {
        ItemStack finalItem = this.item;
        finalItem.setItemMeta(this.meta);

        return finalItem;
    }

    private List<String> getLore() {
        return meta.hasLore() ? meta.getLore() : new ArrayList<>();
    }

    private PlayerProfile getProfileForTexture(String texture) {
        return textureCache.computeIfAbsent(texture, key -> {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL(texture));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            profile.setTextures(textures);
            return profile;
        });
    }
}
