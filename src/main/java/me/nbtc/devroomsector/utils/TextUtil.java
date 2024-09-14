package me.nbtc.devroomsector.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TextUtil {
    private final static String prefix = "&bRegions >> ";
    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static void sendMsg(Player player, String msg){
        player.sendMessage(format(prefix + msg));
    }
}
