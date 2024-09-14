package me.nbtc.devroomsector.listeners;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.MenuManager;
import me.nbtc.devroomsector.model.EditType;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChatListener implements Listener {
    final MenuManager menuManager = Sector.getInstance().getMenuManager();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (!menuManager.getRegionEditor().containsKey(playerId)) return;

        String regionName = menuManager.getRegionEditor().get(playerId).getName();
        Region region = Sector.getInstance().getRegionPool().getRegion(regionName);
        EditType type = menuManager.getRegionEditor().get(playerId).getType();

        String message = event.getMessage();
        event.setCancelled(true);

        if (message.equalsIgnoreCase("cancel")){
            TextUtil.sendMsg(player, "&aOperation cancelled successfully!");
            menuManager.getRegionEditor().remove(playerId);
            return;
        }

        switch (type){
            case NAME: {
                if (!message.matches("^[a-zA-Z0-9]+$") || message.contains(" ")) {
                    TextUtil.sendMsg(player, "&cInvalid region name");
                    return;
                }
                if (Sector.getInstance().getRegionPool().getRegions().containsKey(message.toLowerCase())){
                    TextUtil.sendMsg(player, "&cRegion with that name already exists!");
                    return;
                }

                String oldName = region.getName();
                region.setName(message);

                Sector.getInstance().getRegionPool().getRegions().put(message.toLowerCase(), region);
                Sector.getInstance().getRegionPool().getRegions().remove(oldName.toLowerCase());

                TextUtil.sendMsg(player, "&aRegion name updated to " + message + "!");
                menuManager.getRegionEditor().remove(playerId);
                break;
            }
            case ADD_WHITELIST: {
                Player target = Bukkit.getPlayer(message);
                if (target == null || !target.isOnline()){
                    TextUtil.sendMsg(player, "&cPlayer not found or not online");
                    return;
                }
                if (region.getWhitelist().containsValue(target.getUniqueId())){
                    TextUtil.sendMsg(player, "&cPlayer already whitelisted");
                    return;
                }

                region.addWhitelist(target);
                TextUtil.sendMsg(player, "&aAdded player &7" + target.getName() + " &ato region &7" + region.getName());
                menuManager.getRegionEditor().remove(playerId);
                break;
            }
            case REMOVE_WHITELIST: {
                message = message.toLowerCase();
                if (!region.getWhitelist().containsKey(message)){
                    TextUtil.sendMsg(player, "&cPlayer not whitelisted");
                    return;
                }

                Player target = Bukkit.getPlayer(message);
                String playerName = message;
                if (target != null && target.isOnline())
                    playerName = target.getName();
                TextUtil.sendMsg(player, "&aRemoved player &7" + playerName + " &afrom region &7" + region.getName());
                region.removeWhitelist(playerName);
                menuManager.getRegionEditor().remove(playerId);
                break;
            }
        }
    }
}
