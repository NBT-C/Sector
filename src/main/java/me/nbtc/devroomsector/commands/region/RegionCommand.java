package me.nbtc.devroomsector.commands.region;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.commands.region.subcommands.*;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RegionCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 0){
            new RegionMenuSubCommand(player, args);
            return false;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand){
            case "create":
                new RegionCreateSubCommand(player, args);
                break;
            case "wand":
                new RegionWandSubCommand(player, args);
                break;
            case "add":
                new RegionAddSubCommand(player, args);
                break;
            case "remove":
                new RegionRemoveSubCommand(player, args);
                break;
            case "help":
                new RegionHelpSubCommand(player, args);
                break;
            case "whitelist":
                new RegionWhitelistSubCommand(player, args);
                break;
            case "flag":
                new RegionFlagSubCommand(player, args);
                break;
            default:
                if (args.length > 1 ) break;
                Region region = Sector.getInstance().getRegionPool().getRegion(args[0]);
                if (region == null) {
                    TextUtil.sendMsg(player, "&cRegion not found!");
                    return false;
                }
                Sector.getInstance().getMenuManager().openRegionPage(player, region);
                break;

        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> commands = List.of("help", "create", "wand", "add", "remove", "whitelist", "flag");
            return filterSuggestions(commands, args[0]);
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "create":
                    return List.of("<name>");
                case "add":
                case "remove":
                case "flag":
                case "whitelist":
                    List<String> regionNames = new ArrayList<>();
                    Sector.getInstance().getRegionPool().getRegions().forEach((name, region) -> regionNames.add(name));
                    return filterSuggestions(regionNames, args[1]);
            }
        }

        if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "add":
                    List<String> players = new ArrayList<>();
                    Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
                    return filterSuggestions(players, args[2]);
                case "remove":
                    Region region = Sector.getInstance().getRegionPool().getRegion(args[1]);
                    if (region == null) return List.of();
                    return filterSuggestions(new ArrayList<>(region.getWhitelist().keySet()), args[2]);
                case "flag":
                    return filterSuggestions(new ArrayList<>(Sector.getInstance().getFlagManager().getFlags().keySet()), args[2]);
            }
        }

        if (args.length == 4 && "flag".equalsIgnoreCase(args[0])) {
            return filterSuggestions(List.of("everyone", "whitelist", "none"), args[3]);
        }

        return List.of();
    }

    private List<String> filterSuggestions(List<String> options, String input) {
        return options.stream()
                .filter(option -> option.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }


}
