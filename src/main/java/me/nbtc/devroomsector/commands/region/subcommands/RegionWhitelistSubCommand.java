package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionWhitelistSubCommand {
    public RegionWhitelistSubCommand(Player player, String[] args) {
        if (!player.hasPermission("region.add")) {
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        if (args.length != 2) {
            TextUtil.sendMsg(player, "&eUsage: /region whitelist <region>");
            return;
        }

        String regionString = args[1];
        Region region = Sector.getInstance().getRegionPool().getRegion(regionString);
        if (region == null){
            TextUtil.sendMsg(player, "&cRegion not found");
            return;
        }

        StringBuilder message = new StringBuilder("&7" + region.getName() + " &ewhitelisted users:");
        if (region.getWhitelist().isEmpty()){
            message.append("\n").append("&cThere's no whitelisted users!");
        } else {
            for (String whitelisted : region.getWhitelist().keySet()) {
                message.append("\n").append("&8- &e").append(whitelisted);
            }
        }
        TextUtil.sendMsg(player, message.toString());
    }
}
