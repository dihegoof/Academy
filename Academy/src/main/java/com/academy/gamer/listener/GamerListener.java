package com.academy.gamer.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.gamer.State;
import com.academy.util.Utils;

public class GamerListener extends Utils implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) { 
		event.setJoinMessage(null);
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) {
			gamer = new Gamer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), event.getPlayer(), State.ALIVE, true, true);
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
		event.setQuitMessage(null);
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn()) {
			gamer.getArena().remove(gamer);
			gamer.setArena(null);
		}
		gamer.removeCooldown();
		gamer.setPlayer(null);
		gamer.setOnline(false);
		gamer.removeAbilitie();
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn()) {
			gamer.getArena().remove(gamer);
			gamer.setArena(null);
		}
		gamer.removeCooldown();
		gamer.setPlayer(null);
		gamer.setOnline(false);
		gamer.removeAbilitie();
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getEntity().getName());
		if(gamer == null) return;
		if(gamer.isInvencible()) {
			if(gamer.outSpawn()) { 
				gamer.setInvencible(event.getDamage() >= 1.0 ? false : true);
				event.setDamage(0.0D);
				sendMessage(gamer.getPlayer(), false, "§7Você perdeu a proteção do spawn!");
				return;
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getDamager() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getEntity().getName());
		if(gamer.isInvencible()) {
			if(gamer.outSpawn()) { 
				gamer.setInvencible(event.getDamage() >= 1.0 ? false : true);
				event.setDamage(0.0D);
				sendMessage(gamer.getPlayer(), false, "§7Você perdeu a proteção do spawn!");
				return;
			}
			event.setCancelled(true);
		}
	}
}
