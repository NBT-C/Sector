/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package me.nbtc.devroomsector.utils.config;

import me.nbtc.devroomsector.Sector;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SimpleConfig
implements Config {
    public String path;
    private final File file;
    private YamlConfiguration cfg;

    public SimpleConfig(String name) {
        this.path = Sector.getInstance().getDataFolder().getPath();
        new File(path).mkdir();
        this.file = new File(path, name);
        if (!this.file.exists()) {
            try {
                Sector.getInstance().saveResource(name, true);
                this.cfg = YamlConfiguration.loadConfiguration((File)this.file);
            } catch (Exception exception) {
                // empty catch block
            }
        }
        this.cfg = YamlConfiguration.loadConfiguration((File)this.file);
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

    @Override
    public void delete() {
        this.file.delete();
        this.cfg = null;
    }

    @Override
    public YamlConfiguration getConfig() {
        if (this.cfg == null) {
            this.cfg = YamlConfiguration.loadConfiguration((File)this.file);
        }
        return this.cfg;
    }

    @Override
    public void save() {
        try {
            this.cfg.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File getFile() {
        return this.file;
    }
}

