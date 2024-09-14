package me.nbtc.devroomsector.menus.flags;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.Page;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlagsAutoPage extends Page {
    @Override
    public int getPageButtonsCount(PageView pageView, Player opener) {
            return 18;
    }

    @Override
    public ItemStack nextPageItem(Player player) {
        return ItemBuilder.legacy(Material.ARROW).setDisplay("&eNext Page").build();
    }

    @Override
    public ItemStack previousPageItem(Player player) {
        return ItemBuilder.legacy(Material.ARROW).setDisplay("&ePrevious Page").build();
    }

    @Override
    public String getName() {
        return "Regions";
    }

    @Override
    public MenuTitle getTitle(DataRegistry extraData, Player opener) {
        return MenuTitles.createLegacy("Region Flags");
    }

    @Override
    public Capacity getCapacity(DataRegistry extraData, Player opener) {
        return Capacity.ofRows(3);
    }

    @Override
    public Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
        var builder = Content.builder(capacity);
        return builder.build();
    }
}
