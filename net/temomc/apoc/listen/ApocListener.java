/**
 * TemoMC Apocolypse Plugin (TemoApoc)
 * Copyright (C) 2016 TemoDevil (devil@temomc.net)
 * Released under the terms of the GNU General Public License
 *   as published by the Free Software Foundation; either version
 *   3 of the license, or (at your option) any later version.
 */
package net.temomc.apoc.listen;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.temomc.apoc.task.TaskManager;

public final class ApocListener implements Listener {

	private final TaskManager manager;
	
	public ApocListener(TaskManager manager) {
		this.manager = manager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		manager.addSpawnTask(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		manager.removeSpawnTask(event.getPlayer());
	}
}
