package me.nbtc.devroomsector.manager.flag.flags;

import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import me.nbtc.devroomsector.model.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceFlag extends RegionFlag<BlockPlaceEvent> {
    public BlockPlaceFlag() {
        super("block_place");
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();

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
