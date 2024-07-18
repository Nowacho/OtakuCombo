package me.wacho.otaku.server.loadout.kit.impl;

import me.wacho.otaku.server.loadout.ability.Ability;
import me.wacho.otaku.server.loadout.kit.Kit;
import me.wacho.otaku.utils.player.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class DefaultKit extends Kit {

    public DefaultKit() {
        super("Default", Material.DIAMOND_CHESTPLATE, null);
    }

    @Override
    public ItemStack getSword() {
        return new ItemBuilder(Material.DIAMOND_SWORD)
                .addEnchant(Enchantment.DAMAGE_ALL, 1)
                .addEnchant(Enchantment.DURABILITY, 3)
                .toItemStack();
    }

    @Override
    protected void addExtraItems(PlayerInventory inventory) {
        inventory.setItem(1, new ItemBuilder(Material.ENDER_PEARL).setAmount(3).toItemStack());

        ItemStack speedPotion = new ItemStack(Material.POTION, 32, (short) 8226);
        inventory.setItem(7, speedPotion);

        inventory.setItem(8, new ItemBuilder(Material.GOLDEN_APPLE).setDurability((short) 1).setAmount(64).toItemStack());
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[]{
                new ItemBuilder(Material.DIAMOND_BOOTS)
                        .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
                        .addEnchant(Enchantment.DURABILITY, 5)
                        .toItemStack(),
                new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
                        .addEnchant(Enchantment.DURABILITY, 5)
                        .toItemStack(),
                new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
                        .addEnchant(Enchantment.DURABILITY, 5)
                        .toItemStack(),
                new ItemBuilder(Material.DIAMOND_HELMET)
                        .addEnchant(Enchantment.PROTECTION_PROJECTILE, 10)
                        .addEnchant(Enchantment.DURABILITY, 5)
                        .toItemStack()
        };
    }

    @Override
    public Ability getAbility() {
        return null;
    }

    @Override
    public PotionEffect[] getPotionEffects() {
        return new PotionEffect[]{};
    }

    @Override
    public String getPermission() {
        return null;
    }
}
