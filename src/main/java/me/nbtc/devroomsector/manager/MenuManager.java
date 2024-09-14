package me.nbtc.devroomsector.manager;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import lombok.Getter;
import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.manager.flag.RegionFlag;
import me.nbtc.devroomsector.menus.flags.FlagMenuComponent;
import me.nbtc.devroomsector.menus.flags.FlagsAutoPage;
import me.nbtc.devroomsector.menus.region.RegionPage;
import me.nbtc.devroomsector.menus.regions.RegionsAutoPage;
import me.nbtc.devroomsector.menus.regions.RegionsMenuComponent;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.model.EditType;
import me.nbtc.devroomsector.model.RegionEdit;
import me.nbtc.devroomsector.model.Wand;
import me.nbtc.devroomsector.utils.TextUtil;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class MenuManager {
    private final HashMap<UUID, RegionEdit> regionEditor = new HashMap<>();

    public void openRegionsMenu(Player player){
        List<PageComponent> components = new ArrayList<>();

        for (Region region : Sector.getInstance().getRegionPool().getRegions().values()){
            components.add(new RegionsMenuComponent(region));
        }

        Pagination pagination = Pagination.auto(Sector.getInstance().getLotus())
                .creator(new RegionsAutoPage())
                .componentProvider(() ->components)
                .build();
        try {
            pagination.open(player);
        } catch (InvalidPageException ex) {
            player.sendMessage("There is no components or pages to display !!");
        }
    }

    public void openRegionPage(Player player, Region region){
        Sector.getInstance().getLotus().openMenu(player, new RegionPage(region));
    }
    public void handleRegion(Player player, Region region, EditType editType){
        regionEditor.put(player.getUniqueId(), new RegionEdit(region.getName(), editType));

        String value = "new region name";
        if (editType != EditType.NAME)
            value = "player name";

        player.closeInventory();
        TextUtil.sendMsg(player, "&aPlease write the "+value+" in chat, &awrite &cCancel&a to cancel.");
    }
    public void handleRegionLocationRedefine(Player player, Region region){
        Wand playerWand = Sector.getInstance().getWandManager().getWand(player);
        if (!playerWand.isReady()){
            TextUtil.sendMsg(player, "&cYou must select corners first! &7/region wand");
            return;
        }
        region.setCorner1(playerWand.getCorner1());
        region.setCorner2(playerWand.getCorner2());
        TextUtil.sendMsg(player, "&7("+region.getName()+") &aRegion positions updated!");
    }

    public void handleRegionMenuFlags(Player player, Region region){
        List<PageComponent> components = new ArrayList<>();

        Set<String> allFlags = Sector.getInstance().getFlagManager().getFlags().keySet();

        for (String flag : allFlags){
            FlagState state = FlagState.EVERYONE;
            if (region.getFlags().containsKey(flag))
                state = region.getRegionFlagState(flag);

            components.add(new FlagMenuComponent(region.getName().toLowerCase(), flag.toLowerCase(), state));
        }

        Pagination pagination = Pagination.auto(Sector.getInstance().getLotus())
                .creator(new FlagsAutoPage())
                .componentProvider(() -> components)
                .build();
        try {
            pagination.open(player);
        } catch (InvalidPageException ex) {
            player.sendMessage("There is no components or pages to display !!");
        }
    }
}
