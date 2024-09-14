package me.nbtc.devroomsector.commands.region.subcommands;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

public class RegionHelpSubCommand {
    public RegionHelpSubCommand(Player player, String[] args) {
        if (!player.hasPermission("region.add")) {
            TextUtil.sendMsg(player, "&cNo permissions!");
            return;
        }

        if (args.length != 1) {
            TextUtil.sendMsg(player, "&eUsage: /region help");
            return;
        }


        TextUtil.sendMsg(player,
                        "&aRegion Help\n" +
                        "&e/region&7 - Opens the regions menu\n" +
                        "&e/region create <name>&7 - Creates a region at the selected location\n" +
                        "&e/region wand&7 - Gives the user a stick with a custom name to select locations to create a region\n" +
                        "&e/region add <name> <username>&7 - Whitelist a user to a region\n" +
                        "&e/region remove <name> <username>&7 - Removes a user from the region whitelist\n" +
                        "&e/region whitelist <name>&7 - Lists the users in the region whitelist\n" +
                        "&e/region <name>&7 - Opens the region menu\n" +
                        "&e/region flag <name> <flag> <state>&7 - Edit a region flag\n"
                );
    }
}
