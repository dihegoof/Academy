package com.academy.gamer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

public class GamerManager {
	
	@Getter
	static GamerManager instance = new GamerManager();
	static List<Gamer> gamers = new ArrayList<>();
	
	public void add(Gamer gamer) { 
		if(!gamers.contains(gamer)) 
			gamers.add(gamer);
	}
	
	public void remove(Gamer gamer) { 
		if(gamers.contains(gamer)) 
			gamers.remove(gamer);
	}
	
	public Gamer get(String key) { 
		for(Gamer ga : gamers) { 
			if(ga.getPlayer().getName().equals(key) || ga.getUniqueId().equals(UUID.fromString(key)))
				return ga;
		}
		return null;
	}
}
