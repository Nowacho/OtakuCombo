package me.wacho.otaku.handlers;

import me.wacho.otaku.Otaku;
import me.wacho.otaku.utils.text.CC;
import me.wacho.otaku.utils.cuboid.Cuboid;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CoreHandler implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) return;

        Cuboid spawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid();
        if (spawnCuboid != null && spawnCuboid.contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) return;

        Cuboid spawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid();
        if (spawnCuboid != null && spawnCuboid.contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Cuboid spawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid();
            if (spawnCuboid != null && spawnCuboid.contains(event.getEntity().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Cuboid spawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid();
            if (spawnCuboid != null && spawnCuboid.contains(event.getEntity().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().clear();
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState() && event.getWorld().getEnvironment() == World.Environment.NORMAL) {
            event.setCancelled(true);
            event.getWorld().setStorm(false);
            event.getWorld().setThundering(false);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) return;

        event.getItemDrop().remove();
    }

    @EventHandler
    public void onAchievement(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            String[] lines = event.getLines();
            for (int i = 0; i < lines.length; i++) {
                event.setLine(i, CC.translate(lines[i]));
            }
        }
    }
}
