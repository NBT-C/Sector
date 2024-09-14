package me.nbtc.devroomsector.listeners;

import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.Wand;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MainListeners implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!player.hasPermission("region.wand")) return;

        Block interactedBlock = event.getClickedBlock();
        ItemStack itemStack = event.getItem();

        if ((itemStack == null || itemStack.getType() != Material.BLAZE_ROD || itemStack.getItemMeta() == null) ||
                !itemStack.getItemMeta().getDisplayName().contains("Wand")){
            return;
        }

        if (interactedBlock == null || interactedBlock.getType() == Material.AIR){
            return;
        }

        Action action = event.getAction();
        Wand playerWand = Sector.getInstance().getWandManager().getWand(player);

        event.setCancelled(true);
        if (action == Action.RIGHT_CLICK_BLOCK){
            playerWand.setCorner1(interactedBlock.getLocation().add(0.5, 0.5, 0.5));
            TextUtil.sendMsg(player, "&aCorner 1 has been set!");
        } else if (action == Action.LEFT_CLICK_BLOCK){
            playerWand.setCorner2(interactedBlock.getLocation().add(0.5, 0.5, 0.5));
            TextUtil.sendMsg(player, "&aCorner 2 has been set!");
        }
    }
}
