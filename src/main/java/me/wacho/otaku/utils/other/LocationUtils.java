package me.wacho.otaku.utils.other;

import org.bukkit.*;

public class LocationUtils {

    public static String getString(Location loc) {
        StringBuilder builder = new StringBuilder();
        if (loc == null) {
            return "Location Not Found";
        }
        builder.append(loc.getX()).append("|");
        builder.append(loc.getY()).append("|");
        builder.append(loc.getZ()).append("|");
        builder.append(loc.getWorld().getName()).append("|");
        builder.append(loc.getYaw()).append("|");
        builder.append(loc.getPitch());
        return builder.toString();
    }

    public static Location getLocation(String s) {
        if (s == null || s.equals("Location Not Found") || s.equals("")) {
            return null;
        }
        String[] data = s.split("\\|");
        double x = Double.parseDouble(data[0]);
        double y = Double.parseDouble(data[1]);
        double z = Double.parseDouble(data[2]);
        World world = Bukkit.getWorld(data[3]);
        Float yaw = Float.parseFloat(data[4]);
        Float pitch = Float.parseFloat(data[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
}
