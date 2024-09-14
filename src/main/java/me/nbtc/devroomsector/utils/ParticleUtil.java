package me.nbtc.devroomsector.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleUtil {
    public static void spawnRedstoneBoxParticles(Location corner1, Location corner2) {
        World world = corner1.getWorld();

        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.RED, 1.0f);

        spawnEdgeParticles(world, minX, minY, minZ, maxX, minY, minZ, dustOptions);
        spawnEdgeParticles(world, minX, minY, minZ, minX, maxY, minZ, dustOptions);
        spawnEdgeParticles(world, maxX, minY, minZ, maxX, maxY, minZ, dustOptions);
        spawnEdgeParticles(world, minX, maxY, minZ, maxX, maxY, minZ, dustOptions);

        spawnEdgeParticles(world, minX, minY, maxZ, maxX, minY, maxZ, dustOptions);
        spawnEdgeParticles(world, minX, minY, maxZ, minX, maxY, maxZ, dustOptions);
        spawnEdgeParticles(world, maxX, minY, maxZ, maxX, maxY, maxZ, dustOptions);
        spawnEdgeParticles(world, minX, maxY, maxZ, maxX, maxY, maxZ, dustOptions);

        spawnEdgeParticles(world, minX, minY, minZ, minX, minY, maxZ, dustOptions);
        spawnEdgeParticles(world, maxX, minY, minZ, maxX, minY, maxZ, dustOptions);
        spawnEdgeParticles(world, minX, maxY, minZ, minX, maxY, maxZ, dustOptions);
        spawnEdgeParticles(world, maxX, maxY, minZ, maxX, maxY, maxZ, dustOptions);
    }

    private static void spawnEdgeParticles(World world, double x1, double y1, double z1, double x2, double y2, double z2, Particle.DustOptions dustOptions) {
        int steps = 20;
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double x = x1 + (x2 - x1) * t;
            double y = y1 + (y2 - y1) * t;
            double z = z1 + (z2 - z1) * t;
            world.spawnParticle(Particle.REDSTONE, x, y, z, 0, dustOptions);
        }
    }
}
