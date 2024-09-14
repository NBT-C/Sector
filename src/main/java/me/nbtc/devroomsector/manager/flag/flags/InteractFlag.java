package me.nbtc.devroomsector.manager.flag.flags;

import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import me.nbtc.devroomsector.model.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class InteractFlag extends RegionFlag<PlayerInteractEvent> {
    public InteractFlag() {
        super("interact");
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null)
            return;

        Location location = event.getClickedBlock().getLocation();

        List<Region> regions = getPool().getRegionsSync(location);
        if (regions.isEmpty()) return;

        for (Region region : regions) {
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
