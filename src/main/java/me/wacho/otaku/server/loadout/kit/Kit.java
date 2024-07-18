package me.wacho.otaku.server.loadout.kit;

import lombok.Getter;
import me.wacho.otaku.server.loadout.ability.Ability;
import me.wacho.otaku.utils.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

@Getter
public abstract class Kit {

    private final String name;
    private final Material icon;
    private final String[] desc;

    public Kit(String name, Material icon, String[] desc) {
        this.name = name;
        this.icon = icon;
        this.desc = desc;
    }

    public abstract ItemStack[] getArmor();

    public abstract ItemStack getSword();

    protected abstract void addExtraItems(PlayerInventory inventory);

    public abstract Ability getAbility();

    public abstract PotionEffect[] getPotionEffects();

    public abstract String getPermission();

    public void equipKit(Player player) {
        player.getInventory().clear();

        for (PotionEffect effect : getPotionEffects()) {
            player.addPotionEffect(effect);
        }

        player.getInventory().setArmorContents(getArmor());

        player.getInventory().setItem(0, getSword());

        addExtraItems(player.getInventory());

        Ability ability = getAbility();
        if (ability != null) {
            player.getInventory().setItem(1, ability.getMaterial());
        }

        player.updateInventory();
        player.sendMessage(CC.translate("&eYou have chosen the &a" + getName() + "&e kit."));
    }
}
