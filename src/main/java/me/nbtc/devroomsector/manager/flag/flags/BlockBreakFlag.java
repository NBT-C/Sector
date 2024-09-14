package me.nbtc.devroomsector.manager.flag.flags;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import me.nbtc.devroomsector.model.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakFlag extends RegionFlag<BlockBreakEvent> {
    public BlockBreakFlag() {
        super("block_break");
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(BlockBreakEvent event) {
        // Get the player breaking the block
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();

        // Get all regions at the block’s location
        List<Region> regions = getPool().getRegionsSync(location);
        if (regions.isEmpty()) return;

        // Check each region
        for (Region region : regions) {
            // Get the flag state for the region
            FlagState state = region.getRegionFlagState(getName());

            // Decide what to do based on the flag state
            switch (state) {
                case EVERYONE:
                    // No restrictions; anyone can break the block
                    break;
                case WHITELIST:
                    // Only allow if the player is on the whitelist
                    if (!region.isWhitelisted(player)) {
                        event.setCancelled(true); // Cancel the event if not whitelisted
                    }
                    break;
                case NONE:
                    // Block breaking is not allowed
                    event.setCancelled(true); // Cancel the event
                    break;
            }
        }
    }
}
