package me.wacho.otaku.handlers;

import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.loadout.ability.Ability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AbilityHandler implements Listener {

    private final Ability strengthAbility;

    public AbilityHandler() {
        this.strengthAbility = Otaku.getInstance().getAbilityManager().getAbilityByName("Strength");
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getItem() != null && event.getItem().isSimilar(strengthAbility.getMaterial())) {
            strengthAbility.getAbilityCallable().executeAs(player);
        }
    }
}
