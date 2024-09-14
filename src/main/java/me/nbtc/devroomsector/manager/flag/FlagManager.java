package me.nbtc.devroomsector.manager.flag;

import lombok.Getter;
import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.flag.flags.BlockBreakFlag;
import me.nbtc.devroomsector.manager.flag.flags.BlockPlaceFlag;
import me.nbtc.devroomsector.manager.flag.flags.EntityDamageFlag;
import me.nbtc.devroomsector.manager.flag.flags.InteractFlag;

import java.util.HashMap;

@Getter
public class FlagManager {
    private final HashMap<String, RegionFlag<?>> flags = new HashMap<>();

    public void loadDefaultFlags(){
        registerFlag(new BlockBreakFlag());
        registerFlag(new BlockPlaceFlag());
        registerFlag(new EntityDamageFlag());
        registerFlag(new InteractFlag());
    }
    public RegionFlag<?> getFlag(String flagName){
        return flags.get(flagName.toLowerCase());
    }
    public void registerFlag(RegionFlag<?> flag){
        flags.put(flag.getName().toLowerCase(), flag);
        Sector.getInstance().getServer().getPluginManager().registerEvents(flag, Sector.getInstance());
    }
}
