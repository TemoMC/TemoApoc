/**
 * TemoMC Apocolypse Plugin (TemoApoc)
 * Copyright (C) 2016 TemoDevil (devil@temomc.net)
 * Released under the terms of the GNU General Public License
 *   as published by the Free Software Foundation; either version
 *   3 of the license, or (at your option) any later version.
 */
package net.temomc.apoc.task;

import org.bukkit.entity.Entity;

import net.temomc.apoc.SpawnSettings;
import net.temomc.apoc.SpawningAlgorithms;

public final class TaskSpawn implements Runnable {
	
	private final SpawnSettings  settings;
	private final Entity         spawner;
	
	public TaskSpawn(SpawnSettings settings, Entity spawner) {
		this.settings = settings;
		this.spawner  = spawner;
	}

	@Override
	public void run() {
		if (SpawningAlgorithms.canSpawn(spawner))
			SpawningAlgorithms.doSpawning(settings, spawner.getLocation());
	}
}
