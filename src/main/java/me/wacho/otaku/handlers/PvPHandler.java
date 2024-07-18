package me.wacho.otaku.handlers;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketCooldown;
import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.enums.PlayerStat;
import me.wacho.otaku.server.events.CombatTagEvent;
import me.wacho.otaku.user.PlayerData;
import me.wacho.otaku.utils.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PvPHandler implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setDeathMessage(null);

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        UUID victimUUID = victim.getUniqueId();
        UUID killerUUID = killer != null ? killer.getUniqueId() : null;

        PlayerData victimData = PlayerData.getPlayerData(victimUUID);
        PlayerData killerData = killerUUID != null ? PlayerData.getPlayerData(killerUUID) : null;

        String victimName = victim.getName();
        String killerName = killer != null ? killer.getName() : "Unknown";

        int victimKills = victimData.getStat(PlayerStat.KILLS);
        int killerKills = killerData != null ? killerData.getStat(PlayerStat.KILLS) : 0;

        // Update stats
        victimData.getPlayerStats().merge(PlayerStat.DEATHS, 1, Integer::sum);
        if (killerData != null) {
            killerData.getPlayerStats().merge(PlayerStat.KILLS, 1, Integer::sum);
            killerData.getPlayerStats().merge(PlayerStat.STREAK, 1, Integer::sum);
            int currentStreak = killerData.getStat(PlayerStat.STREAK);
            if (currentStreak > killerData.getStat(PlayerStat.HIGHEST_STREAK)) {
                killerData.getPlayerStats().put(PlayerStat.HIGHEST_STREAK, currentStreak);
            }
            killerData.getPlayerStats().merge(PlayerStat.BALANCE, 100, Integer::sum);
        }

        String deathMessage;
        if (victim.getLastDamageCause() != null) {
            switch (victim.getLastDamageCause().getCause()) {
                case VOID:
                    deathMessage = CC.translate("&c" + victimName + "&7[&f" + victimKills + "&7] &efell out of the world");
                    break;
                case FALL:
                    deathMessage = CC.translate("&c" + victimName + "&7[&f" + victimKills + "&7] &efell from a high place");
                    break;
                default:
                    deathMessage = CC.translate("&c" + victimName + "&7[&f" + victimKills + "&7] &ewas slain by &c" + killerName + "&7[&f" + killerKills + "&7] &e");
                    break;
            }
        } else {
            deathMessage = CC.translate("&c" + victimName + "&7[&f" + victimKills + "&7] &ewas slain by &c" + killerName + "&7[&f" + killerKills + "&7] &e");
        }

        Bukkit.broadcastMessage(deathMessage);
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (Otaku.getInstance().getServerData().getSpawnCuboid().contains(victim.getLocation()) &&
                Otaku.getInstance().getServerData().getSpawnCuboid().contains(attacker.getLocation())) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getPluginManager().callEvent(new CombatTagEvent(victim, attacker));
        setCombatCooldown(victim);
        setCombatCooldown(attacker);
    }

    @EventHandler
    public void onCombatTag(CombatTagEvent event) {
        Player player = event.getPlayer();
        Player attacker = event.getAttacker();

        if (LunarClientAPI.getInstance().isRunningLunarClient(player)) {
            sendCombatCooldownPacket(player);
        }
        if (LunarClientAPI.getInstance().isRunningLunarClient(attacker)) {
            sendCombatCooldownPacket(attacker);
        }
    }

    private void setCombatCooldown(Player player) {
        Otaku.getInstance().getCombatManager().setCombatTime(player, 16);
        Otaku.getInstance().getCombatManager().setCombatSet(player, true);
    }

    private void sendCombatCooldownPacket(Player player) {
        LunarClientAPI.getInstance().sendPacket(player, new LCPacketCooldown("Combat Cooldown", 16000, Material.DIAMOND_SWORD.getId()));
    }
}
