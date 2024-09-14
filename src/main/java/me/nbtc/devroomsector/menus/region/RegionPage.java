package me.nbtc.devroomsector.menus.region;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import lombok.var;
import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.model.EditType;
import me.nbtc.devroomsector.model.Region;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegionPage implements Menu {
    final Region region;
    public RegionPage(Region region){
        this.region = region;
    }
    @Override
    public String getName() {
        return "Region";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry extraData, Player opener) {
        return MenuTitles.createLegacy("Region: " + region.getName());
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry extraData, Player opener) {
        return Capacity.ofRows(5);
    }

    @Override
    public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
        var builder = Content.builder(capacity);

        Button nameButton = Button.clickable(
                ItemBuilder.legacy(Material.ACACIA_SIGN).setDisplay("&eRename").setLore("&7Current Name:&7 " + region.getName()).build(),
                ButtonClickAction.plain((menuView, event)-> { event.setCancelled(true);
                    Sector.getInstance().getMenuManager().handleRegion(opener, region, EditType.NAME);
                })
        );
        Button whitelistAddButton = Button.clickable(
                ItemBuilder.legacy(Material.PAPER).setDisplay("&eAdd Whitelist").setLore("&7Adds a player to region whitelist").build(),
                ButtonClickAction.plain((menuView, event)-> { event.setCancelled(true);
                    Sector.getInstance().getMenuManager().handleRegion(opener, region, EditType.ADD_WHITELIST);
                })
        );
        Button whitelistRemoveButton = Button.clickable(
                ItemBuilder.legacy(Material.MAP).setDisplay("&cRemove Whitelist").setLore("&7Removes a player from region whitelist").build(),
                ButtonClickAction.plain((menuView, event)-> { event.setCancelled(true);
                    Sector.getInstance().getMenuManager().handleRegion(opener, region, EditType.REMOVE_WHITELIST);
                })
        );
        Button redefineLocationButton = Button.clickable(
                ItemBuilder.legacy(Material.BELL).setDisplay("&aRedefine Location").setLore("&7Sets the new corners to your current wand").build(),
                ButtonClickAction.plain((menuView, event)-> { event.setCancelled(true);
                    Sector.getInstance().getMenuManager().handleRegionLocationRedefine(opener, region);
                })
        );

        Button editFlagsButton = Button.clickable(
                ItemBuilder.legacy(Material.CLOCK).setDisplay("&9Edit Flags").setLore("&7Click to open flags menu").build(),
                ButtonClickAction.plain((menuView, event)-> { event.setCancelled(true);
                    Sector.getInstance().getMenuManager().handleRegionMenuFlags(opener, region);
                })
        );

        builder.setButton(13, nameButton);
        builder.setButton(20, whitelistAddButton);
        builder.setButton(24, whitelistRemoveButton);
        builder.setButton(28, redefineLocationButton);
        builder.setButton(34, editFlagsButton);

        return builder.build();
    }
}
