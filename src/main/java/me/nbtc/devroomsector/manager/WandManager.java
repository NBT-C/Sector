package me.nbtc.devroomsector.manager;

import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import lombok.Data;
import me.nbtc.devroomsector.model.Wand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class WandManager {
    private final Map<UUID, Wand> wands = new HashMap<>();
    private final ItemStack wandItem = ItemBuilder.legacy(Material.BLAZE_ROD)
            .setDisplay("&eWand")
            .setLore("&7Right-Click for pos-1", "&7Left-Click for pos-2").build();

    public Wand getWand(Player player) {
        UUID playerId = player.getUniqueId();
        return wands.computeIfAbsent(playerId, id -> new Wand(null, null));
    }

    public void giveWandItem(Player player){
        player.getInventory().addItem(wandItem);
    }
}
