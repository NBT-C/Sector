package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionFlagSubCommand {
    public RegionFlagSubCommand(Player player, String[] args) {
        if (!player.hasPermission("region.flag")) {
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        if (args.length != 4) {
            TextUtil.sendMsg(player, "&eUsage: /region flag <region> <flag> <state>");
            return;
        }

        String regionString = args[1];

        Region region = Sector.getInstance().getRegionPool().getRegion(regionString);
        if (region == null){
            TextUtil.sendMsg(player, "&cRegion not found");
            return;
        }

        String flagString = args[2];
        RegionFlag<?> flag = Sector.getInstance().getFlagManager().getFlag(flagString);

        if (flag == null){
            TextUtil.sendMsg(player, "&cRegion flag not found.");
            return;
        }

        String stateString = args[3];
        FlagState state = FlagState.fromString(stateString);

        if (state == null){
            TextUtil.sendMsg(player, "&cIncorrect state! (EVERYONE, WHITELIST, NONE)");
            return;
        }

        if (region.getRegionFlagState(flag.getName()) == state){
            TextUtil.sendMsg(player, "&cFlag &a"+flag.getName()+" &cis already set to &a" + state.name() + "!");
            return;
        }

        if (state != FlagState.EVERYONE)
            region.setFlag(flag.getName(), state);
        else
            region.getFlags().remove(flag.getName());

        TextUtil.sendMsg(player, "&7("+region.getName()+") &eFlag &a" + flag.getName() + " &eset to &a" + state.name() + "&e successfully!");
    }
}
