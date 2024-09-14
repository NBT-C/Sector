package me.nbtc.devroomsector.model;

import lombok.Data;
import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class Region {
    private final UUID randomId;
    private String name;
    private Location corner1;
    private Location corner2;
    private Map<String, UUID> whitelist;
    private Map<String, FlagState> flags;

    public Region(String name, Location corner1, Location corner2) {
        this.randomId = UUID.randomUUID();
        this.name = name;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.whitelist = new HashMap<>();
        this.flags = new HashMap<>();
    }
    public Region(UUID randomId, String name, Location corner1, Location corner2, Map<String, UUID> whitelist, Map<String, FlagState> flags){
        this.randomId = randomId;
        this.name = name;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.whitelist = whitelist;
        this.flags = flags;
    }
    public void addWhitelist(Player player){
        whitelist.put(player.getName().toLowerCase(), player.getUniqueId());
    }
    public void removeWhitelist(String name){
        whitelist.remove(name.toLowerCase());
    }
    public FlagState getRegionFlagState(String flagName){
        if (!flags.containsKey(flagName))
            return FlagState.EVERYONE;
        return flags.get(flagName);
    }
    public void setFlag(String flag, FlagState state){
        flags.put(flag, state);
    }
    public boolean isWhitelisted(Player player){
        return whitelist.containsValue(player.getUniqueId());
    }
    public boolean contains(Location location) {
        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return location.getX() >= minX && location.getX() <= maxX &&
                location.getY() >= minY && location.getY() <= maxY &&
                location.getZ() >= minZ && location.getZ() <= maxZ;
    }
}
