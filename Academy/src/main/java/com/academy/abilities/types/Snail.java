package com.academy.abilities.types;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;

public class Snail implements Listener {
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getDamager() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getDamager().getName());
		if(gamer == null) return;
		if(gamer.hasAbilitie("Snail") && isHitable(gamer, GamerManager.getInstance().get(event.getEntity().getName()))) {
			if(new Random().nextInt(33) < 33) { 
				((Player)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3));
			}
		}
	}
	
	boolean isHitable(Gamer gamer, Gamer gamerTwo) { 
		return gamer.hasArena(gamerTwo.getArena().getName());
	}
}
