package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.result.RegionCreateResult;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionCreateSubCommand {
    public RegionCreateSubCommand(Player player, String[] args) {
        if (!player.hasPermission("region.create")){
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        if (args.length != 2) {
            TextUtil.sendMsg(player, "&eUsage: /region create <name>");
            return;
        }

        String name = args[1];
        if (!name.matches("^[a-zA-Z0-9]+$")) {
            TextUtil.sendMsg(player, "&cInvalid region name");
            return;
        }

        Sector.getInstance().getRegionPool().addRegion(player, name, result -> {
            if (result == RegionCreateResult.USED_NAME)
                TextUtil.sendMsg(player, "&cRegion with that name already exists!");
            else if (result == RegionCreateResult.NO_CORNERS)
                TextUtil.sendMsg(player, "&cYou must select corners first! &7/region wand");
            else if (result == RegionCreateResult.SUCCESS)
                TextUtil.sendMsg(player, "&aRegion created successfully! &7("+name+")");
        });

    }
}
