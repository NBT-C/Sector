package me.nbtc.devroomsector.pools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import me.cobeine.sqlava.connection.database.query.PreparedQuery;
import me.cobeine.sqlava.connection.database.query.Query;
import me.nbtc.devroomsector.Sector;
import me.nbtc.devroomsector.manager.WandManager;
import me.nbtc.devroomsector.manager.flag.FlagState;
import me.nbtc.devroomsector.model.Region;
import me.nbtc.devroomsector.model.Wand;
import me.nbtc.devroomsector.result.RegionCreateResult;
import me.nbtc.devroomsector.utils.ParticleUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegionPool {
    private final @Getter HashMap<String, Region> regions = new HashMap<>();
    private final WandManager wandManager = Sector.getInstance().getWandManager();

    public void addRegion(Player creator, String name, Consumer<RegionCreateResult> resultConsumer) {
        RegionCreateResult result;

        if (regions.containsKey(name.toLowerCase())) {
            result = RegionCreateResult.USED_NAME;
        } else if (!wandManager.getWand(creator).isReady()) {
            result = RegionCreateResult.NO_CORNERS;
        } else {
            Wand wand = wandManager.getWand(creator);
            Region region = new Region(name, wand.getCorner1(), wand.getCorner2());
            regions.put(name.toLowerCase(), region);
            result = RegionCreateResult.SUCCESS;

            new BukkitRunnable() {
                int ticks = 0;

                @Override
                public void run() {
                    if (ticks >= 10) {
                        cancel();
                        return;
                    }
                    ParticleUtil.spawnRedstoneBoxParticles(wand.getCorner1(), wand.getCorner2());
                    ticks++;
                }
            }.runTaskTimer(Sector.getInstance(), 0L, 5L);
        }
        resultConsumer.accept(result);
    }

    public void removeRegion(Region region) {
        regions.remove(region.getName());
    }

    public Region getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    public void isPlayerInAnyRegionAsync(Player player, Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Sector.getInstance(), () -> {
            boolean isInRegion = regions.values().stream().anyMatch(region -> region.contains(player.getLocation()));
            callback.onResult(isInRegion);
        });
    }

    public void getPlayerRegionsAsync(Player player, RegionCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Sector.getInstance(), () -> {
            List<Region> playerRegions = regions.values().stream()
                    .filter(region -> region.contains(player.getLocation()))
                    .collect(Collectors.toList());
            callback.onResult(playerRegions);

        });
    }

    public void getRegionsAsync(Location location, RegionCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Sector.getInstance(), () -> {
            List<Region> playerRegions = regions.values().stream()
                    .filter(region -> region.contains(location))
                    .collect(Collectors.toList());
            callback.onResult(playerRegions);
        });
    }

    public List<Region> getRegionsSync(Location location) {
        return regions.values().stream()
                .filter(region -> region.contains(location))
                .collect(Collectors.toList());
    }

    public void saveToDatabase() {
        String upsertQuery = "INSERT INTO regions (ID, NAME, CORNER_1, CORNER_2, WHITELIST, FLAGS) VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "NAME = VALUES(NAME), "
                + "CORNER_1 = VALUES(CORNER_1), "
                + "CORNER_2 = VALUES(CORNER_2), "
                + "WHITELIST = VALUES(WHITELIST), "
                + "FLAGS = VALUES(FLAGS)";

        try (PreparedStatement preparedStatement = Sector.getInstance().getMySQLManager().getConnector().getConnection().getConnection().prepareStatement(upsertQuery)) {
            for (Region region : regions.values()) {
                preparedStatement.setString(1, region.getRandomId().toString());
                preparedStatement.setString(2, region.getName());
                preparedStatement.setString(3, region.getCorner1().getWorld().getName() + ", " + region.getCorner1().getX() + ", " + region.getCorner1().getY() + ", " + region.getCorner1().getZ());
                preparedStatement.setString(4, region.getCorner2().getWorld().getName() + ", " + region.getCorner2().getX() + ", " + region.getCorner2().getY() + ", " + region.getCorner2().getZ());
                preparedStatement.setString(5, Sector.getInstance().getGson().toJson(region.getWhitelist()));
                preparedStatement.setString(6, Sector.getInstance().getGson().toJson(region.getFlags()));

                preparedStatement.addBatch();
            }

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing database operations", e);
        }
    }

    public void loadFromDatabase() {
        String selectQuery = "SELECT ID, NAME, CORNER_1, CORNER_2, WHITELIST, FLAGS FROM regions";

        try (PreparedStatement preparedStatement = Sector.getInstance().getMySQLManager().getConnector().getConnection().getConnection().prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            regions.clear();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("ID"));
                String name = resultSet.getString("NAME");
                String corner1Str = resultSet.getString("CORNER_1");
                String corner2Str = resultSet.getString("CORNER_2");
                String whitelistJson = resultSet.getString("WHITELIST");
                String flagsJson = resultSet.getString("FLAGS");

                String[] corner1Parts = corner1Str.split(", ");
                Location corner1 = new Location(
                        Sector.getInstance().getServer().getWorld(corner1Parts[0]),
                        Double.parseDouble(corner1Parts[1]),
                        Double.parseDouble(corner1Parts[2]),
                        Double.parseDouble(corner1Parts[3])
                );

                String[] corner2Parts = corner2Str.split(", ");
                Location corner2 = new Location(
                        Sector.getInstance().getServer().getWorld(corner1Parts[0]),
                        Double.parseDouble(corner2Parts[1]),
                        Double.parseDouble(corner2Parts[2]),
                        Double.parseDouble(corner2Parts[3])
                );

                Map<String, UUID> whitelist = Sector.getInstance().getGson().fromJson(whitelistJson, new TypeToken<Map<String, UUID>>(){}.getType());
                Map<String, FlagState> flags = Sector.getInstance().getGson().fromJson(flagsJson, new TypeToken<Map<String, FlagState>>(){}.getType());

                Region region = new Region(uuid, name, corner1, corner2, whitelist, flags);
                regions.put(name, region);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading data from database", e);
        }
    }


    public interface Callback {
        void onResult(boolean isInRegion);
    }

    public interface RegionCallback {
        void onResult(List<Region> regions);
    }
}
