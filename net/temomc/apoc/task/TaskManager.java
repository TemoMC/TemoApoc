/**
 * TemoMC Apocolypse Plugin (TemoApoc)
 * Copyright (C) 2016 TemoDevil (devil@temomc.net)
 * Released under the terms of the GNU General Public License
 *   as published by the Free Software Foundation; either version
 *   3 of the license, or (at your option) any later version.
 */
package net.temomc.apoc.task;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Entity;
import net.temomc.apoc.SpawnSettings;
import net.temomc.apoc.TemoApoc;

public final class TaskManager {
	
	private final TemoApoc plugin;
	private final HashMap<UUID, Integer> spawnTasks = new HashMap<>();

	public TaskManager(TemoApoc plugin) {
		this.plugin = plugin;
	}

	public void stopTasks() {
		for (Integer taskId : spawnTasks.values()) {
			plugin.getServer().getScheduler().cancelTask(taskId.intValue());
		}
		spawnTasks.clear();
	}

	public void addSpawnTask(Entity entity) {
		int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
				plugin,
				new TaskSpawn(new SpawnSettings(), entity),
				plugin.getSpawnTaskDelay(),
				plugin.getSpawnTaskInterval()
				);
		spawnTasks.put(entity.getUniqueId(), Integer.valueOf(taskId));
	}
	
	public boolean removeSpawnTask(Entity entity) {
		Integer taskId = spawnTasks.remove(entity.getUniqueId());
		if (taskId == null) return false;
		plugin.getServer().getScheduler().cancelTask(taskId.intValue());
		return true;
	}
}
