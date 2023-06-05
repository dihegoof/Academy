package com.academy.arenas.damager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import lombok.Getter;

public class DamageManager {
	
	@Getter
	static DamageManager instance = new DamageManager();
	@Getter
	List<Damager> damagers = new ArrayList<>();
	
	public void add(Damager damager) { 
		if(!damagers.contains(damager)) 
			damagers.add(damager);
	}
	
	public void remove(Damager damager) { 
		if(damagers.contains(damager)) 
			damagers.remove(damager);
	}
	
	public Damager get(Player player) { 
		for(Damager da : damagers) { 
			if(da.getPlayer().equals(player))
				return da;
		}
		return null;
	}

}
