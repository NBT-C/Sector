package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionWandSubCommand {
    public RegionWandSubCommand(Player player, String[] args) {
        if (!player.hasPermission("region.wand")){
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        if (args.length != 1) {
            TextUtil.sendMsg(player, "&eUsage: /region wand");
            return;
        }

        Sector.getInstance().getWandManager().giveWandItem(player);
        TextUtil.sendMsg(player, "&aWand given!");
    }
}
