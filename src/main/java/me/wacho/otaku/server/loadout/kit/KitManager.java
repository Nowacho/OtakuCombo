package me.wacho.otaku.server.loadout.kit;

import lombok.Getter;
import me.wacho.otaku.server.loadout.kit.impl.DefaultKit;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KitManager {

    private final List<Kit> kits = new ArrayList<>();

    public KitManager() {
        kits.add(new DefaultKit());
    }

    public Kit getKitByName(String name) {
        for (Kit kit : kits) {
            if (name.equals(kit.getName())) {
                return kit;
            }
        }

        return null;
    }
}