package com.academy.arenas.feast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.academy.arenas.Arena;

import lombok.Getter;

public class FeastManager {
	
	@Getter
	static FeastManager instance = new FeastManager();
	@Getter
	List<Feast> feast = new ArrayList<>();
	@Getter
	HashMap<Player, List<Location>> selectPos = new HashMap<>();
	
	public void add(Feast feast) { 
		if(!this.feast.contains(feast)) 
			this.feast.add(feast);
	}
	
	public void remove(Feast feast) { 
		if(this.feast.contains(feast)) 
			this.feast.remove(feast);
	}
	
	public Feast get(Arena arena) { 
		for(Feast fe : feast) { 
			if(fe.getArena().equals(arena))
				return fe;
		}
		return null;
	}

}
