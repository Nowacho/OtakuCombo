package me.wacho.otaku.server.menu.buttons;

import lombok.AllArgsConstructor;
import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.loadout.kit.Kit;
import me.wacho.otaku.user.PlayerData;
import me.wacho.otaku.utils.player.ItemBuilder;
import me.wacho.otaku.utils.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class SelectKitButton extends Button {

    private Kit kit;
    private PlayerData playerData;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>(Arrays.asList(kit.getDesc()));

        if (!player.hasPermission(kit.getPermission())) {
            lore.add("");
            lore.add("&cYou do not have permission to use this kit.");
            lore.add("&cBuy it at &estore.otaku.combo&c.");
        } else {
            lore.add("");
            lore.add("&eClick to apply " + kit.getName() + " Kit.");
        }

        String itemName = player.hasPermission(kit.getPermission()) ? "&a" + kit.getName() : "&c" + kit.getName();
        return new ItemBuilder(kit.getIcon())
                .setName(itemName)
                .setLore(lore)
                .toItemStack();
    }


    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (!player.hasPermission(kit.getPermission())) return;

        player.closeInventory();
        playerData.setCurrentKitName(kit.getName());
        Otaku.getInstance().getKitManager().getKitByName(kit.getName()).equipKit(player);
    }
}
