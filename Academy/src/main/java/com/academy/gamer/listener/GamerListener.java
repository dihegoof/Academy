package com.academy.gamer.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.gamer.State;

public class GamerListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getUniqueId().toString());
		if(gamer == null) {
			gamer = new Gamer(event.getPlayer().getUniqueId(), event.getPlayer(), State.ALIVE, false, null);
			GamerManager.getInstance().add(gamer);
		} else { 
			gamer.setOnline(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getUniqueId().toString());
		if(gamer == null) return;
		gamer.setPlayer(null);
		gamer.setOnline(false);
		if(gamer.getArena() != null) {
			gamer.getArena().remove(gamer);
			gamer.setArena(null);
		}
	}
}
