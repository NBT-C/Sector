package me.nbtc.devroomsector.model;

import lombok.Data;

@Data
public class RegionEdit {
    final String name;
    final EditType type;

    public RegionEdit(String name, EditType type) {
        this.name = name;
        this.type = type;
    }
}
