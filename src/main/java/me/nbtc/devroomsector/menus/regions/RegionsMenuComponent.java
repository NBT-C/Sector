package me.nbtc.devroomsector.menus.regions;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.Region;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class RegionsMenuComponent implements PageComponent {
    private final Region region;
    public RegionsMenuComponent(Region region){
        this.region = region;
    }

    @Override
    public ItemStack toItem() {
        return ItemBuilder.legacy(Material.PAPER)
                .setDisplay("&e" + region.getName())
                .setLore(
                        "&7Pos-1: " + region.getCorner1().getX() + ", " + region.getCorner1().getY() + ", " + region.getCorner1().getZ(),
                        "&7Pos-2: " + region.getCorner2().getX() + ", " + region.getCorner2().getY() + ", " + region.getCorner2().getZ(),
                        "&eInteract to view"
                )
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        Sector.getInstance().getMenuManager().openRegionPage(player, region);
    }

}
