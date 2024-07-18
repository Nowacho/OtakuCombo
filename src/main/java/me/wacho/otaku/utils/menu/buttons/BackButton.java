package me.wacho.otaku.utils.menu.buttons;

import me.wacho.otaku.utils.player.ItemBuilder;
import me.wacho.otaku.utils.menu.Button;
import me.wacho.otaku.utils.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BackButton extends Button {

    private final Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.BED)
                .setName("&c&lBack")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }
}
