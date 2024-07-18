package me.wacho.otaku.utils.menu;

import com.google.common.collect.Maps;
import me.wacho.otaku.utils.text.CC;
import me.wacho.otaku.utils.player.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter @Setter
public abstract class Menu {

    @Getter private static Map<String, Menu> openedMenus = Maps.newHashMap();
    private Map<Integer, Button> buttons = Maps.newHashMap();
    private boolean updateAfterClick = false;
    private boolean closedByMenu = false;
    private boolean placeholder = false;
    private Button placeholderButton = Button.placeholder(Material.STAINED_GLASS_PANE, 7, " ");

    private ItemStack createItemStack(Player player, Button button) {
        return new ItemBuilder(button.getButtonItem(player)).toItemStack();
    }

    public void openMenu(final Player player) {
        this.buttons = this.getButtons(player);

        Menu previousMenu = openedMenus.get(player.getName());
        Inventory inventory = null;
        String title = CC.translate(this.getTitle(player));
        int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
        boolean update = false;

        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        if (player.getOpenInventory() != null) {
            if (previousMenu == null) {
                player.closeInventory();
            }
            else {
                int previousSize = player.getOpenInventory().getTopInventory().getSize();

                if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
                    inventory = player.getOpenInventory().getTopInventory();
                    update = true;
                }
                else {
                    previousMenu.setClosedByMenu(true);
                    player.closeInventory();
                }
            }
        }

        if (inventory == null) {
            inventory = Bukkit.createInventory(player, size, title);
        }

        inventory.setContents(new ItemStack[inventory.getSize()]);

        openedMenus.put(player.getName(), this);

        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
        }

        if (this.isPlaceholder()) {
            Button fillButton = this.getPlaceholderButton() == null ? placeholderButton : this.getPlaceholderButton();

            for (int i = 0; i < size; i++) {
                if (this.buttons.get(i) == null) {
                    this.buttons.put(i, fillButton);
                    inventory.setItem(i, fillButton.getButtonItem(player));
                }
            }
        }

        if (update) {
            player.updateInventory();
        }
        else {
            player.openInventory(inventory);
        }

        this.setClosedByMenu(false);
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public int getSlot(int x, int y) {
        return ((9 * y) + x);
    }

    public int getSize() {
        return -1;
    }

    public Button getPlaceholderButton() {
        return null;
    }

    public abstract String getTitle(Player player);

    public abstract Map<Integer, Button> getButtons(Player player);
}