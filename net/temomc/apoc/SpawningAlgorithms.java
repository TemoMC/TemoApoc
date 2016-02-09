package net.temomc.apoc;

import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpawningAlgorithms {
	
	public static boolean canSpawn(Entity entity) {
		return entity.isValid();
	}
	
	public static Stream<Location> getNearbyPlayerLocationStream(
			Location location,
			int distance) {
		return location.getWorld()
				.getNearbyEntities(location, distance, distance, distance)
				.stream()
				.filter(e -> e instanceof Player)
				.map(e -> e.getLocation());
	}
	
	public static boolean canSpawnAt(SpawnSettings settings, Location location) {
		final Location head = new Location(
				location.getWorld(),
				location.getX(),
				location.getY() + 1,
				location.getZ());
		final Location floor = new Location(
				location.getWorld(),
				location.getX(),
				location.getY() - 1,
				location.getZ());
		
		if (floor.getBlock().isEmpty()) return false;
		// TODO: Except iron bars, glass panes, fences, glass,
		// stairs, enchantment tables, walls, anvils, hoppers
		if (location.getBlock().getType().isOccluding()) return false;
		if (head.getBlock().getType().isOccluding()) return false;
		// TODO: Any skylight limitations?
		if (location.getBlock().getLightFromBlocks() > settings.lightmax) return false;

		// Get all nearby players
		return location.getWorld().getNearbyEntities(
					location,
					settings.playermindist,
					settings.playermindist,
					settings.playermindist)
				.isEmpty();
	}
	
	public static int scoreLocation(SpawnSettings settings, Location location) {
		int score = 0;
		// Calculate score from light bias
		score += Math.abs(location.getBlock().getLightFromBlocks()
				- settings.lightbias)
				/ settings.lightweight;
		// Calculate score from average local player distance & bias
		score += getNearbyPlayerLocationStream(
					location,
					settings.playerdistbias * 2)
				.map(l -> Double.valueOf(
						Math.abs(settings.playerdistbias
								- location.distance(l))))
				.mapToInt(d -> d.intValue())
				.average()
				.orElse(0)
				/ settings.playerdistweight;
		// Calculate score from depth bias
		score += Math.abs(settings.depthbias
				- location.getBlockY())
				/ settings.depthweight;
		return score;
	}
	
	public static void spawn(SpawnSettings settings, Location location) {
		Zombie z = location.getWorld().spawn(location, Zombie.class);
		z.setMaxHealth(15);
		z.setHealth(15);
		z.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, 1, false, false));
		z.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999, 2, false, false));
		z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999, 2, false, false));
		z.getEquipment().setHelmet(new ItemStack(Material.COMPASS));
	}
	
	public static int doSpawning(SpawnSettings settings, Location location) {
		final int      maxoffset  = settings.maxdist / 2;

		int[]      scores    = new int[settings.count];
		Location[] locations = new Location[settings.count];

		for(int x = location.getBlockX() - maxoffset;
				x < location.getBlockX() + maxoffset;
				x++) {
			// Y values are bound to the minimum and maximum heights
			for(int y = Math.max(location.getBlockY() - maxoffset, 1);
					y < Math.min(location.getBlockY() + maxoffset,
							location.getWorld().getMaxHeight());
					y++) {
				for(int z = location.getBlockZ() - maxoffset;
						z < location.getBlockZ() + maxoffset;
						z++) {
					final Location canidate = new Location(location.getWorld(), x, y, z);

					if (!canSpawnAt(settings, canidate)) continue;

					final int score = scoreLocation(settings, canidate);
					for (int i = 0; i < scores.length; i++) {
						if (score > scores[i]) {
							scores[i] = score;
							locations[i] = canidate;
							x += 5;
							break;
						}
					}
				}
			}
		}

		int spawned = 0;
		for (Location selected : locations) {
			if (selected == null) continue;
			spawn(settings, selected);
			spawned++;
		}

		return spawned;
	}
}
