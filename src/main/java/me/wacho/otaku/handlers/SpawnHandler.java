package me.wacho.otaku.handlers;

import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.enums.PlayerState;
import me.wacho.otaku.user.PlayerData;
import me.wacho.otaku.utils.text.CC;
import me.wacho.otaku.utils.cuboid.Cuboid;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnHandler implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) return;

        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        Cuboid spawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid();

        if (spawnCuboid == null) return;

        if (playerData.getPlayerState() == PlayerState.SPAWN && !spawnCuboid.contains(to)) {
            playerData.setPlayerState(PlayerState.PLAYING);

            player.sendMessage(CC.translate("&eLeaving: &aSpawn&e, Entering: &cWarzone"));
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        onPlayerMove(event);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;

        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        Cuboid spawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid();

        if (playerData.getPlayerState() == PlayerState.PLAYING && spawnCuboid.contains(player.getLocation())) {
            Location to = event.getTo();
            Location from = event.getFrom();

            if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
                return;
            }

            Location nearestLocation = LocationSpawnCuboid(player.getLocation().getBlock());
            if (nearestLocation != null) {
                player.teleport(nearestLocation.add(0.5, 0, 0.5));
            }
        }
    }

    private Location LocationSpawnCuboid(Block block) {
        int[] offsets = {0, 1, -1};
        Cuboid spawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid();

        for (int xOffset : offsets) {
            for (int zOffset : offsets) {
                Location location = block.getLocation().clone().add(xOffset, 0, zOffset);
                if (!spawnCuboid.contains(location)) {
                    return location;
                }
            }
        }
        return null;
    }
}

