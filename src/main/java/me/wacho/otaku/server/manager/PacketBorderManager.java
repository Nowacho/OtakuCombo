package me.wacho.otaku.server.manager;

import me.wacho.otaku.Otaku;
import me.wacho.otaku.utils.cuboid.Cuboid;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PacketBorderManager extends Thread {

    private static final int REGION_DISTANCE = 8;
    private static final int REGION_DISTANCE_SQUARED = REGION_DISTANCE * REGION_DISTANCE;
    private static final int BLOCK_CHANGE_DURATION = 4000; // 4 seconds

    private static final Map<UUID, Map<Location, Long>> sentBlockChanges = new ConcurrentHashMap<>();

    /**
     * The main run method of the thread, which continually checks players and updates their block changes.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (Player player : Otaku.getInstance().getServer().getOnlinePlayers()) {
                    checkPlayer(player);
                }
                Thread.sleep(20L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks a player's status and updates block changes accordingly.
     *
     * @param player the player to check
     */
    private void checkPlayer(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            clearPlayer(player);
            return;
        }

        boolean isInSpawnCuboid = Otaku.getInstance().getServerData().getSpawnCuboid().contains(player);

        if (isInSpawnCuboid) {
            clearPlayer(player);
            return;
        }

        sentBlockChanges.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());

        Iterator<Map.Entry<Location, Long>> bordersIterator = sentBlockChanges.get(player.getUniqueId()).entrySet().iterator();

        while (bordersIterator.hasNext()) {
            Map.Entry<Location, Long> border = bordersIterator.next();
            if (System.currentTimeMillis() >= border.getValue()) {
                Location loc = border.getKey();
                if (loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
                    Block block = loc.getBlock();
                    player.sendBlockChange(loc, block.getType(), block.getData());
                    bordersIterator.remove();
                }
            }
        }
        sendClaimToPlayer(player);
    }

    /**
     * Sends claim blocks to the player within a defined region distance.
     *
     * @param player the player to send claim blocks to
     */
    private void sendClaimToPlayer(Player player) {
        Cuboid cuboid = Otaku.getInstance().getServerData().getSpawnCuboid();
        Location playerLocation = player.getLocation();

        for (Block block : getBorderBlocks(cuboid)) {
            Location blockLocation = block.getLocation();
            double distanceSquared = playerLocation.distanceSquared(new Location(player.getWorld(), blockLocation.getX(), playerLocation.getY(), blockLocation.getZ()));
            if (distanceSquared <= REGION_DISTANCE_SQUARED) {
                for (int i = -4; i <= 4; i++) {
                    Location check = blockLocation.clone().add(0, i, 0);
                    if (isValidBlockChangeLocation(player, check, cuboid)) {
                        player.sendBlockChange(check, Material.STAINED_GLASS, (byte) 14);
                        sentBlockChanges.get(player.getUniqueId()).put(check, System.currentTimeMillis() + BLOCK_CHANGE_DURATION);
                    }
                }
            }
        }
    }

    /**
     * Gets the border blocks of a cuboid region.
     *
     * @param cuboid the cuboid region
     * @return a list of border blocks
     */
    private List<Block> getBorderBlocks(Cuboid cuboid) {
        List<Block> borderBlocks = new ArrayList<>();
        for (int x = cuboid.getLowerX(); x <= cuboid.getUpperX(); x++) {
            for (int z = cuboid.getLowerZ(); z <= cuboid.getUpperZ(); z++) {
                if (x == cuboid.getLowerX() || x == cuboid.getUpperX() || z == cuboid.getLowerZ() || z == cuboid.getUpperZ()) {
                    for (int y = cuboid.getLowerY(); y <= cuboid.getUpperY(); y++) {
                        borderBlocks.add(new Location(cuboid.getWorld(), x, y, z).getBlock());
                    }
                }
            }
        }
        return borderBlocks;
    }

    /**
     * Checks if a block change location is valid.
     *
     * @param player the player
     * @param location the location to check
     * @param cuboid the cuboid region
     * @return true if the location is valid for a block change, false otherwise
     */
    private boolean isValidBlockChangeLocation(Player player, Location location, Cuboid cuboid) {
        return location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4) &&
                location.getBlock().getType().isTransparent() &&
                cuboid.contains(location.getBlock()) &&
                location.distanceSquared(player.getLocation()) <= REGION_DISTANCE_SQUARED;
    }

    /**
     * Clears all block changes for a player.
     *
     * @param player the player whose block changes should be cleared
     */
    private static void clearPlayer(Player player) {
        Map<Location, Long> changes = sentBlockChanges.remove(player.getUniqueId());
        if (changes != null) {
            for (Location changedLoc : changes.keySet()) {
                if (changedLoc.getWorld().isChunkLoaded(changedLoc.getBlockX() >> 4, changedLoc.getBlockZ() >> 4)) {
                    Block block = changedLoc.getBlock();
                    player.sendBlockChange(changedLoc, block.getType(), block.getData());
                }
            }
        }
    }
}
