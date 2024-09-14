<h1 align="center" id="title">Sector</h1>

<p align="center" id="description">A Minecraft Regions plugin developed in just 2 days for submission to Dev-Room's freelancer Discord.</p>

<h2>💖 Join Us on Discord</h2>
<p align="center"><a href="https://discord.gg/YrBmF6CAMZ">https://discord.gg/YrBmF6CAMZ</a></p>

<h2>© Custom Flag Creation</h2>

<p>This example demonstrates how to create a custom region flag for handling block break events:</p>

```java
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

            // Handle based on the flag state
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
```
<p align="center">✌ Don't forget to register the flag in the Main class:</p>

    Sector.getInstance().getFlagManager().registerFlag(new BlockBreakFlag());
