package me.wacho.otaku.utils.text;

import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class CC {

    public static ItemStack nameItem(ItemStack item, String name, short durability, int amount, List<String> lores) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        meta.setLore(CC.translateFromArray(lores));
        item.setItemMeta(meta);
        item.setAmount(amount);
        item.setDurability(durability);
        return item;
    }

    public static ItemStack nameItem(Material item, String name, short durability, int amount, List<String> lores) {
        return nameItem(new ItemStack(item), name, durability, amount, lores);
    }

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> translateFromArray(List<String> text) {
        return text.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static void log(String in) {
        Bukkit.getConsoleSender().sendMessage(translate(in));
    }

    public static String capitalize(String str) {
        return WordUtils.capitalize(str);
    }
}
