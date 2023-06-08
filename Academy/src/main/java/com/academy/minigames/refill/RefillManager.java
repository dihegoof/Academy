package com.academy.minigames.refill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import lombok.Getter;

public class RefillManager {
	
	@Getter
	static RefillManager instance = new RefillManager();
	@Getter
	List<Refill> refills = new ArrayList<>();
	
	public void add(Refill refill) { 
		if(!refills.contains(refill)) 
			refills.add(refill);
	}
	
	public void remove(Refill refill) { 
		if(refills.contains(refill)) 
			refills.remove(refill);
	}
	
	public Refill get(Player player) { 
		for(Refill re : refills) { 
			if(re.getPlayer().equals(player))
				return re;
		}
		return null;
	}
}
