package me.wacho.otaku.handlers;

import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.loadout.kit.Kit;
import me.wacho.otaku.user.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());

        // Check if the player has a current kit
        if (playerData.getCurrentKitName() == null) {
            Kit defaultKit = Otaku.getInstance().getKitManager().getKitByName("Default");

            // Give the player the DefaultKit
            if (defaultKit != null) {
                defaultKit.equipKit(player);
                playerData.setCurrentKitName("Default");
            }
        }
    }
}
