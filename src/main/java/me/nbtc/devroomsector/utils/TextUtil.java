package me.nbtc.devroomsector.utils;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

public class TextUtil {
    public final static String prefix = "&bRegions >> ";
    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static void sendMsg(Player player, String msg){
        player.sendMessage(format(prefix + msg));
    }
    public static void sendMsg(Conversable player, String msg){
        player.sendRawMessage(format(prefix + msg));
    }
}
