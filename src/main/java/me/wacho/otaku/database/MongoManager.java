package me.wacho.otaku.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.wacho.otaku.Otaku;
import org.bson.Document;

@Getter
public class MongoManager {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> statsCollection, serverCollection;

    public void initialize() {
        String host = Otaku.getInstance().getConfigFile().getString("DATABASE.HOST");
        int port = Otaku.getInstance().getConfigFile().getInt("DATABASE.PORT");
        String dbName = "OtakuCombo";

        MongoClientURI uri;
        if (Otaku.getInstance().getConfigFile().getBoolean("DATABASE.AUTHENTICATION.ENABLED")) {
            String username = Otaku.getInstance().getConfigFile().getString("DATABASE.AUTHENTICATION.USER");
            String password = Otaku.getInstance().getConfigFile().getString("DATABASE.AUTHENTICATION.PASSWORD");

            uri = new MongoClientURI(String.format("mongodb://%s:%s@%s:%d/%s",
                    username, password, host, port, dbName));
        } else {
            uri = new MongoClientURI(String.format("mongodb://%s:%d/%s", host, port, dbName));
        }

        client = new MongoClient(uri);
        database = client.getDatabase(dbName);
        statsCollection = database.getCollection("StatsCollection", Document.class);
        serverCollection = database.getCollection("ServerCollection", Document.class);
    }
}
