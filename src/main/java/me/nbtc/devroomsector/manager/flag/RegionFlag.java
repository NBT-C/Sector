package me.nbtc.devroomsector.manager.flag;

import lombok.Getter;
import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.pools.RegionPool;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@Getter
public abstract class RegionFlag<T extends Event> implements Listener {
    private final String name;

    public RegionFlag(String name) {
        this.name = name;
    }

    public RegionPool getPool(){
        return Sector.getInstance().getRegionPool();
    }

    @EventHandler
    public abstract void handle(T event);
}