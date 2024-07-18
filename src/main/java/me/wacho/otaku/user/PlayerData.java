package me.wacho.otaku.user;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import me.wacho.otaku.Otaku;
import me.wacho.otaku.server.enums.PlayerStat;
import me.wacho.otaku.server.enums.PlayerState;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlayerData {

    private static final Map<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    private final UUID uuid;
    private String playerName;

    private final Map<PlayerStat, Integer> playerStats = Collections.synchronizedMap(new HashMap<>());
    private PlayerState playerState;

    private String currentKitName;

    private PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.playerState = PlayerState.SPAWN;
        loadProfile();
    }

    private void loadProfile() {
        Document document = Otaku.getInstance().getMongoManager().getStatsCollection().find(Filters.eq("uuid", uuid.toString())).first();
        if (document != null) {
            Object statsObj = document.get("playerStats");
            if (statsObj instanceof List) {
                List<?> statsList = (List<?>) statsObj;
                statsList.stream()
                        .filter(Objects::nonNull)
                        .filter(Document.class::isInstance)
                        .map(Document.class::cast)
                        .forEach(statDoc -> playerStats.put(PlayerStat.valueOf(statDoc.getString("name")), statDoc.getInteger("value")));
            }

            playerName = document.getString("playerName");
        }
    }

    public Document serialize() {
        Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("playerName", playerName);
        document.put("playerStats", playerStats.entrySet().stream()
                .map(entry -> new Document("name", entry.getKey().name()).append("value", entry.getValue()))
                .collect(Collectors.toList()));
        return document;
    }


    public int getStat(PlayerStat stat) {
        return playerStats.getOrDefault(stat, 0);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, PlayerData::new);
    }
}
