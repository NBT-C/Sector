package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionAddSubCommand {
    public RegionAddSubCommand(Player player, String[] args) {
        if (!player.hasPermission("region.add")) {
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        if (args.length != 3) {
            TextUtil.sendMsg(player, "&eUsage: /region add <region> <player>");
            return;
        }

        String regionString = args[1];
        Player target = Sector.getInstance().getServer().getPlayer(args[2]);

        Region region = Sector.getInstance().getRegionPool().getRegion(regionString);
        if (region == null){
            TextUtil.sendMsg(player, "&cRegion not found");
            return;
        }

        if (target == null || !target.isOnline()){
            TextUtil.sendMsg(player, "&cPlayer not found or not online");
            return;
        }
        if (region.getWhitelist().containsValue(target.getUniqueId())){
            TextUtil.sendMsg(player, "&cPlayer already whitelisted");
            return;
        }

        region.addWhitelist(player);
        TextUtil.sendMsg(player, "&aAdded player &7" + target.getName() + " &ato region &7" + region.getName());
    }
}
