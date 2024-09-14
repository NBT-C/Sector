package me.nbtc.devroomsector.menus.flags;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlagMenuComponent implements PageComponent {
    private final String flag;
    private FlagState state;
    private final String regionName;
    public FlagMenuComponent(String regionName, String flag, FlagState state){
        this.flag = flag;
        this.state = state;
        this.regionName = regionName;
    }

    @Override
    public ItemStack toItem() {
        List<String> lore = new ArrayList<>();
        lore.add(TextUtil.format("&8➜ " + (state == FlagState.EVERYONE ? "&aEVERYONE" : "&cEVERYONE")));
        lore.add(TextUtil.format("&8➜ " + (state == FlagState.WHITELIST ? "&aWHITELIST" : "&cWHITELIST")));
        lore.add(TextUtil.format("&8➜ " + (state == FlagState.NONE ? "&aNONE" : "&cNONE")));
        lore.add("");
        lore.add(TextUtil.format("&7Interact to switch state"));

        return ItemBuilder.legacy(Material.PAPER)
                .setDisplay("&e" + flag)
                .setLore(lore)
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);

        FlagState state;
        switch (this.state) {
            case EVERYONE:
                state = FlagState.WHITELIST;
                break;
            case WHITELIST:
                state = FlagState.NONE;
                break;
            case NONE:
                state = FlagState.EVERYONE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.state);
        }


        Region region = Sector.getInstance().getRegionPool().getRegion(regionName);
        region.setFlag(flag, state);

        this.state = state;

        Sector.getInstance().getMenuManager().handleRegionMenuFlags((Player) event.getWhoClicked(), region);
    }

}
