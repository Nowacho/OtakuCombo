package me.wacho.otaku.server.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Data;
import me.wacho.otaku.Otaku;
import me.wacho.otaku.utils.other.LocationUtils;
import me.wacho.otaku.utils.cuboid.Cuboid;
import me.wacho.otaku.utils.mongo.DocumentSerializer;
import org.bson.Document;
import org.bukkit.Location;

@Data
public class ServerData implements DocumentSerializer {

    private static final String SERVER_IDENTIFIER = "server_info";
    private String spawn, firstCI, secondCI;

    private final MongoCollection<Document> serverInformation;

    public ServerData() {
        this.serverInformation = Otaku.getInstance().getMongoManager().getServerCollection();
        loadServer();
    }

    private void loadServer() {
        Document document = serverInformation.find(Filters.eq("identifier", SERVER_IDENTIFIER)).first();
        if (document != null) {
            spawn = document.getString("spawn");
            firstCI = document.getString("firstCI");
            secondCI = document.getString("secondCI");
        } else {
            spawn = firstCI = secondCI = null;
        }
    }

    @Override
    public Document serialize() {
        Document document = new Document("identifier", SERVER_IDENTIFIER);
        putStringToDocument(document, "spawn", spawn);
        putStringToDocument(document, "firstCI", firstCI);
        putStringToDocument(document, "secondCI", secondCI);
        return document;
    }

    private void putStringToDocument(Document document, String key, String value) {
        if (value != null) {
            document.put(key, value);
        }
    }

    public void saveServer() {
        serverInformation.replaceOne(Filters.eq("identifier", SERVER_IDENTIFIER), serialize(), new UpdateOptions().upsert(true));
    }

    public Cuboid getSpawnCuboid() {
        Location first = LocationUtils.getLocation(firstCI);
        Location second = LocationUtils.getLocation(secondCI);
        return new Cuboid(first, second);
    }

    public Location getSpawnLocation() {
        return LocationUtils.getLocation(spawn);
    }
}
