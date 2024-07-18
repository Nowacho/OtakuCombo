package me.wacho.otaku.server.loadout.ability.impl;

import me.wacho.otaku.server.loadout.ability.Ability;
import me.wacho.otaku.server.loadout.ability.AbilityCallable;
import me.wacho.otaku.utils.player.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthAbility extends Ability {

    public StrengthAbility() {
        super("Strength", new ItemBuilder(Material.NETHER_STAR).setName("&cStrength").toItemStack());
    }

    @Override
    public AbilityCallable getAbilityCallable() {
        return player -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 30, 1));
    }

    @Override
    public int getCooldown() {
        return 60;
    }
}
