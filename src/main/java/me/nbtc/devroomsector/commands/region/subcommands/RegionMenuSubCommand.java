package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionMenuSubCommand {
    public RegionMenuSubCommand(Player player, String[] args){
        if (!player.hasPermission("region.menu")){
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        Sector.getInstance().getMenuManager().openRegionsMenu(player);
    }
}
