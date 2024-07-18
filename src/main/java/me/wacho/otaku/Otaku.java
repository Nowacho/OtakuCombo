package me.wacho.otaku;

import co.aikar.commands.PaperCommandManager;
import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import me.wacho.otaku.board.scoreboard.Adapter;
import me.wacho.otaku.commands.EssentialCommands;
import me.wacho.otaku.database.MongoManager;
import me.wacho.otaku.handlers.*;
import me.wacho.otaku.server.data.ServerData;
import me.wacho.otaku.server.loadout.ability.AbilityManager;
import me.wacho.otaku.server.loadout.kit.KitManager;
import me.wacho.otaku.server.manager.CombatManager;
import me.wacho.otaku.server.manager.PacketBorderManager;
import me.wacho.otaku.utils.other.FileConfig;
import me.wacho.otaku.utils.menu.ButtonListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

import static me.wacho.otaku.utils.text.CC.log;
import static me.wacho.otaku.utils.text.CC.translate;

@Getter
public final class Otaku extends JavaPlugin {

    @Getter
    private static Otaku instance;

    private FileConfig configFile;
    private KitManager kitManager;
    private AbilityManager abilityManager;
    private ServerData serverData;
    private CombatManager combatManager;
    private MongoManager mongoManager;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        log(" ");
        log("&7&m-----------------------------------------------");
        log("&fPlugin: &cOtakuCombo");
        log("&fVersion: &c" + getDescription().getVersion());
        log("&fAuthor: &c" + getDescription().getAuthors().get(0));
        log("&7&m-----------------------------------------------");
        log(" ");

        // Initialize managers synchronously
        setupConfig();
        setupManager();
        setupListener();
        setupCommands();
    }

    private void setupConfig() {
        try {
            this.configFile = new FileConfig(this, "settings.yml");
        } catch (RuntimeException e) {
            Bukkit.getConsoleSender().sendMessage(translate("&cFailed to load settings.yml: " + e.getMessage()));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void setupListener() {
        List<Listener> listeners = Arrays.asList(
                new PlayerHandler(),
                new CoreHandler(),
                new PvPHandler(),
                new AbilityHandler(),
                new SpawnHandler(),
                new ButtonListener(this)
        );

        listeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        if (serverData != null) {
            serverData.saveServer();
        }

        if (mongoManager != null && mongoManager.getClient() != null) {
            mongoManager.getClient().close();
        }
    }

    private void setupCommands() {
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new EssentialCommands());
    }

    private void setupManager() {
        kitManager = new KitManager();
        abilityManager = new AbilityManager();
        combatManager = new CombatManager();
        (new PacketBorderManager()).start();

        Assemble assemble = new Assemble(this, new Adapter());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);

        // Initialize MongoManager asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                initializeMongoManager();
                initializeServerData(); // Initialize ServerData after MongoManager
            } catch (Exception ex) {
                getLogger().severe("Failed to initialize MongoManager: " + ex.getMessage());
                Bukkit.getPluginManager().disablePlugin(this);
            }
        });
    }

    private void initializeMongoManager() {
        this.mongoManager = new MongoManager();
        this.mongoManager.initialize();
    }

    private void initializeServerData() {
        this.serverData = new ServerData();
    }
}
