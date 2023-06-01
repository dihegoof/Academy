package com.academy.gamer.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.gamer.State;

public class GamerListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) {
			gamer = new Gamer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), event.getPlayer(), State.ALIVE, false, true, null);
			GamerManager.getInstance().add(gamer);
		} else { 
			gamer.setOnline(true);
			gamer.setPlayer(event.getPlayer());
		}
		gamer.preparePlayer();
		gamer.setSpawn();
		gamer.setInvencible(true);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn()) {
			gamer.getArena().remove(gamer);
			gamer.setArena(null);
		}
		gamer.setPlayer(null);
		gamer.setOnline(false);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getEntity().getName());
		if(gamer == null) return;
		if(gamer.isInvencible()) 
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getDamager() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getEntity().getName());
		if(gamer.isInvencible()) 
			event.setCancelled(true);
	}
}
