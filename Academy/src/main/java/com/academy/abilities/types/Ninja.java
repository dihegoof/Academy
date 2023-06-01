package com.academy.abilities.types;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.academy.abilities.Abilitie;
import com.academy.abilities.AbilitieManager;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.util.Utils;

public class Ninja extends Utils implements Listener {
	
	static Abilitie abilitie = AbilitieManager.getInstance().get("Ninja");
	
	static HashMap<Player, Player> hit = new HashMap<>();
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getDamager() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getDamager().getName());
		if(gamer == null) return;
		if(gamer.hasAbilitie(abilitie.getName()) && isHitable(gamer, GamerManager.getInstance().get(event.getEntity().getName()))) {
			hit.put(gamer.getPlayer(), (Player)event.getEntity());
		}
	}
	
	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.hasAbilitie(abilitie.getName())) { 
			if(gamer.inCooldown()) { 
				sendMessage(null, false, "§cVocê deve aguardar " + gamer.getCooldown() + "!");
				return;
			}
			if(!hit.containsKey(gamer.getPlayer())) {
				sendMessage(gamer.getPlayer(), false, "§cVocê não hitou ninguém até o momento!");
				return;
			}
			if(!isHitable(gamer, GamerManager.getInstance().get(hit.get(gamer.getPlayer()).getName()))) { 
				sendMessage(gamer.getPlayer(), false, "§cO último jogador hitado não está na mesma arena que você");
				return;
			}
			if(gamer.getPlayer().getLocation().distance(hit.get(gamer.getPlayer()).getLocation()) > 30) {
				sendMessage(gamer.getPlayer(), false, "§cO último jogador hitado está acima de 30 blocos de distancia!");
				return;
			}
			gamer.getPlayer().teleport(hit.get(gamer.getPlayer()));
			gamer.addCooldown(abilitie.getCooldown());
			sendMessage(gamer.getPlayer(), false, "§aTeleportado!");
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) { 
		if(hit.containsKey(event.getPlayer()))
			hit.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) { 
		if(hit.containsKey(event.getPlayer()))
			hit.remove(event.getPlayer());
	}
	
	boolean isHitable(Gamer gamer, Gamer gamerTwo) { 
		return gamer.hasArena(gamerTwo.getArena().getName());
	}
}
