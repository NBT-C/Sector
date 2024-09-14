/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package me.nbtc.devroomsector.utils.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface Config {
    public boolean exists();

    public void delete();

    public YamlConfiguration getConfig();

    public void save();

    public File getFile();
}

