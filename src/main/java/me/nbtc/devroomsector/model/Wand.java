package me.nbtc.devroomsector.model;

import lombok.Data;
import org.bukkit.Location;


@Data
public class Wand {
    Location corner1;
    Location corner2;
    public Wand(Location corner1, Location corner2){
        this.corner1 = corner1;
        this.corner2 = corner2;
    }
    public boolean isReady() {
        return corner1 != null && corner2 != null;
    }
}
