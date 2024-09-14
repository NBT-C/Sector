package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionRemoveSubCommand {
    public RegionRemoveSubCommand(Player player, String[] args) {
        if (!player.hasPermission("region.remove")) {
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        if (args.length != 3) {
            TextUtil.sendMsg(player, "&eUsage: /region remove <region> <player>");
            return;
        }

        String regionString = args[1];
        String playerToRemoveString = args[2].toLowerCase();

        Region region = Sector.getInstance().getRegionPool().getRegion(regionString);
        if (region == null){
            TextUtil.sendMsg(player, "&cRegion not found");
            return;
        }

        if (!region.getWhitelist().containsKey(playerToRemoveString)){
            TextUtil.sendMsg(player, "&cPlayer not whitelisted");
            return;
        }

        Player playerToRemove = Sector.getInstance().getServer().getPlayer(playerToRemoveString);
        String playerName = playerToRemove == null ? playerToRemoveString : playerToRemove.getName();

        region.removeWhitelist(playerName);
        TextUtil.sendMsg(player, "&aRemoved player &7" + playerName + " &afrom region &7" + region.getName());
    }
}
