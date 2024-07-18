package me.wacho.otaku.server.menu;

import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.loadout.kit.Kit;
import me.wacho.otaku.server.menu.buttons.SelectKitButton;
import me.wacho.otaku.user.PlayerData;
import me.wacho.otaku.utils.menu.Button;
import me.wacho.otaku.utils.menu.Menu;
import me.wacho.otaku.utils.menu.buttons.CloseButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KitSelectionMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Kit Selector";
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());

        buttons.put(0, new CloseButton());

        for (Kit kit : Otaku.getInstance().getKitManager().getKits()) {
            buttons.put(buttons.size(), new SelectKitButton(kit, playerData));
        }

        return buttons;
    }
}
