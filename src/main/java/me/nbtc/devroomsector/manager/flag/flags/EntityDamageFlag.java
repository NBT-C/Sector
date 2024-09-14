package me.nbtc.devroomsector.manager.flag.flags;

import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import me.nbtc.devroomsector.model.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityDamageFlag extends RegionFlag<EntityDamageByEntityEvent> {
    public EntityDamageFlag() {
        super("entity_damage");
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        List<Region> victimRegions = getPool().getRegionsSync(event.getEntity().getLocation());
        List<Region> damagerRegions = getPool().getRegionsSync(player.getLocation());

        for (Region region : victimRegions) {
            FlagState state = region.getRegionFlagState(getName());

            switch (state) {
                case EVERYONE:
                    break;
                case WHITELIST:
                    if (!region.isWhitelisted(player))
                        event.setCancelled(true);
                    break;
                case NONE:
                    event.setCancelled(true);
                    break;
            }
        }
        for (Region region : damagerRegions) {
            FlagState state = region.getRegionFlagState(getName());

            switch (state) {
                case EVERYONE:
                    break;
                case WHITELIST:
                    if (!region.isWhitelisted(player))
                        event.setCancelled(true);
                    break;
                case NONE:
                    event.setCancelled(true);
                    break;
            }
        }

    }
}
