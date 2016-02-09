package net.temomc.apoc;

import org.bukkit.plugin.java.JavaPlugin;

import net.temomc.apoc.listen.ApocListener;
import net.temomc.apoc.task.TaskManager;

public final class TemoApoc extends JavaPlugin {
	
	// TODO: Configurability of below options
	
	private final long SPAWN_TASK_DELAY    = 20;  // One second
	private final long SPAWN_TASK_INTERVAL = 200; // Ten seconds
	
	private final TaskManager  taskManager = new TaskManager(this);
	private final ApocListener listener    = new ApocListener(taskManager);
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(listener, this);
		this.getServer().getOnlinePlayers().forEach(p -> taskManager.addSpawnTask(p));
	}
	
	@Override
	public void onDisable() {
		taskManager.stopTasks();
	}
	
	public long getSpawnTaskDelay() {
		return SPAWN_TASK_DELAY;
	}
	
	public long getSpawnTaskInterval() {
		return SPAWN_TASK_INTERVAL;
	}
}
