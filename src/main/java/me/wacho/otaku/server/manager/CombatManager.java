package me.wacho.otaku.server.manager;

import lombok.Getter;
import me.wacho.otaku.utils.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class CombatManager extends BukkitRunnable {

    private final Set<Player> combatSet = new HashSet<>();
    private final Map<Player, AtomicInteger> timeMap = new HashMap<>();
    private int count = 0;

    public void setCombatSet(Player player, boolean inCombat) {
        if (inCombat) {
            combatSet.add(player);
            timeMap.put(player, new AtomicInteger(16));
        } else {
            combatSet.remove(player);
            timeMap.remove(player);
        }
    }

    public void setCombatTime(Player player, int time) {
        timeMap.computeIfPresent(player, (p, atomicInteger) -> new AtomicInteger(time));
    }

    public boolean isCombat(Player player) {
        return combatSet.contains(player);
    }

    public int getCombatTime(Player player) {
        return timeMap.getOrDefault(player, new AtomicInteger(0)).get();
    }

    @Override
    public void run() {
        count++;

        for (Player player : Bukkit.getOnlinePlayers()) {
            AtomicInteger combatTime = timeMap.get(player);
            if (combatTime != null && combatTime.get() > 0) {
                combatTime.decrementAndGet();
                if (combatTime.get() == 0) {
                    player.sendMessage(CC.translate("&aYou are no longer in combat"));
                    combatSet.remove(player);
                    timeMap.remove(player);
                }
            }
        }

        if (count == 160) {
            count = 0;
        }
    }
}
