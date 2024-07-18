package me.wacho.otaku.utils.menu.buttons;

import me.wacho.otaku.utils.player.ItemBuilder;
import me.wacho.otaku.utils.menu.Button;
import lombok.AllArgsConstructor;
import me.wacho.otaku.utils.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageInfoButton extends Button {

    private final PaginatedMenu paginatedMenu;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName("&ePage Info")
                .setLore("&e" + paginatedMenu.getPage() + "&7/&a" + paginatedMenu.getPages(player))
                .toItemStack();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
