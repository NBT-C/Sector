package me.nbtc.devroomsector;

import com.google.gson.Gson;
import io.github.mqzen.menus.Lotus;
import lombok.Getter;
import me.nbtc.devroomsector.commands.region.RegionCommand;
import me.nbtc.devroomsector.conversations.RegionConversation;
import me.nbtc.devroomsector.listeners.MainListeners;
import me.nbtc.devroomsector.manager.MenuManager;
import me.nbtc.devroomsector.manager.WandManager;
import me.nbtc.devroomsector.manager.flag.FlagManager;
import me.nbtc.devroomsector.manager.mysql.MySQLManager;
import me.nbtc.devroomsector.pools.RegionPool;
import me.nbtc.devroomsector.utils.config.SimpleConfig;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Sector extends JavaPlugin {
    private static @Getter Sector instance;
    private RegionPool regionPool;
    private WandManager wandManager;

    private Lotus lotus;
    private MenuManager menuManager;
    private FlagManager flagManager;
    private SimpleConfig settings;
    private MySQLManager mySQLManager;
    private final Gson gson = new Gson();

    private RegionConversation chatConversation;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        settings = new SimpleConfig("settings.yml");

        initialize();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        regionPool.saveToDatabase();
    }
    private void initialize(){
        wandManager = new WandManager();
        regionPool = new RegionPool();
        lotus = new Lotus(this, EventPriority.NORMAL);
        menuManager = new MenuManager();
        flagManager = new FlagManager();

        flagManager.loadDefaultFlags();
        mySQLManager = new MySQLManager();
        mySQLManager.connect();
        regionPool.loadFromDatabase();
        
        enrollCommands();
        enrollListeners();

        chatConversation = new RegionConversation();
    }
    private void enrollCommands() {
        getCommand("region").setExecutor(new RegionCommand());
        getCommand("region").setTabCompleter(new RegionCommand());
    }
    private void enrollListeners(){
        getServer().getPluginManager().registerEvents(new MainListeners(), this);
    }
}
