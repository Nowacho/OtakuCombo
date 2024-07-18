package me.wacho.otaku.utils.menu.pagination;

import me.wacho.otaku.utils.player.ItemBuilder;
import me.wacho.otaku.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageButton extends Button {

    private final int mod;
    private final PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.CARPET);

        if (mod > 0) {
            itemBuilder.setDurability((short) 13);
        }
        else {
            itemBuilder.setDurability((short) 14);
        }

        if (this.hasNext(player)) {
            itemBuilder.setName(this.mod > 0 ? "&aNext page" : "&cPrevious page");
        }
        else {
            itemBuilder.setName("&7" + (this.mod > 0 ? "Last page" : "First page"));
        }

        return itemBuilder.toItemStack();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        if (hasNext(player)) {
            this.menu.modPage(player, this.mod);
            Button.playNeutral(player);
        } else {
            Button.playFail(player);
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }
}
