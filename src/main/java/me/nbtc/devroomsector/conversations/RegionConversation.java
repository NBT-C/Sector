package me.nbtc.devroomsector.conversations;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.MenuManager;
import me.nbtc.devroomsector.model.EditType;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.model.RegionEdit;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RegionConversation {
    private final MenuManager menuManager = Sector.getInstance().getMenuManager();
    private String message;

    private final ConversationFactory conversationFactory = new ConversationFactory(Sector.getInstance())
            .withFirstPrompt(new RegionPrompt())
            .withEscapeSequence("cancel")
            .withLocalEcho(false)
            .thatExcludesNonPlayersWithMessage("Only players can use this!");

    public void startConversation(Player player, String context) {
        this.message = context;
        conversationFactory.buildConversation(player).begin();
    }

    private class RegionPrompt extends StringPrompt {
        @Override
        public String getPromptText(ConversationContext context) {
            return TextUtil.format(TextUtil.prefix + message);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            Conversable player = context.getForWhom();
            UUID playerId = ((Player)player).getUniqueId();

            if (!menuManager.getRegionEditor().containsKey(playerId)) {
                return END_OF_CONVERSATION;
            }

            RegionEdit regionEditor = menuManager.getRegionEditor().get(playerId);
            Region region = Sector.getInstance().getRegionPool().getRegion(regionEditor.getName());
            EditType type = regionEditor.getType();

            if (input.equalsIgnoreCase("cancel")) {
                menuManager.getRegionEditor().remove(playerId);
                return END_OF_CONVERSATION;
            }

            switch (type) {
                case NAME: {
                    if (!input.matches("^[a-zA-Z0-9]+$") || input.contains(" ")) {
                        TextUtil.sendMsg(player, "&cInvalid region name");
                        return new RegionPrompt();
                    }
                    if (Sector.getInstance().getRegionPool().getRegions().containsKey(input.toLowerCase())) {
                        TextUtil.sendMsg(player, "&cRegion with that name already exists!");
                        return new RegionPrompt();
                    }

                    String oldName = region.getName();
                    region.setName(input);

                    Sector.getInstance().getRegionPool().getRegions().put(input.toLowerCase(), region);
                    Sector.getInstance().getRegionPool().getRegions().remove(oldName.toLowerCase());

                    TextUtil.sendMsg(player, "&aRegion name updated to " + input + "!");
                    menuManager.getRegionEditor().remove(playerId);
                    return END_OF_CONVERSATION;
                }
                case ADD_WHITELIST: {
                    Player target = Bukkit.getPlayer(input);
                    if (target == null || !target.isOnline()) {
                        TextUtil.sendMsg(player, "&cPlayer not found or not online");
                        return new RegionPrompt();
                    }
                    if (region.getWhitelist().containsValue(target.getUniqueId())) {
                        TextUtil.sendMsg(player, "&cPlayer already whitelisted");
                        return new RegionPrompt();
                    }

                    region.addWhitelist(target);
                    TextUtil.sendMsg(player, "&aAdded player &7" + target.getName() + " &ato region &7" + region.getName());
                    menuManager.getRegionEditor().remove(playerId);
                    return END_OF_CONVERSATION;
                }
                case REMOVE_WHITELIST: {
                    input = input.toLowerCase();
                    if (!region.getWhitelist().containsKey(input)) {
                        TextUtil.sendMsg(player, "&cPlayer not whitelisted");
                        return new RegionPrompt();
                    }

                    Player target = Bukkit.getPlayer(input);
                    String playerName = input;
                    if (target != null && target.isOnline())
                        playerName = target.getName();

                    region.removeWhitelist(playerName);
                    TextUtil.sendMsg(player, "&aRemoved player &7" + playerName + " &afrom region &7" + region.getName());
                    menuManager.getRegionEditor().remove(playerId);
                    return END_OF_CONVERSATION;
                }
                default:
                    return END_OF_CONVERSATION;
            }
        }
    }
}
