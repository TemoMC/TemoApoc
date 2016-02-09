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
