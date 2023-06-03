package com.academy.arenas.feast;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.academy.arenas.Arena;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feast {
	
	Arena arena;
	List<Location> chests;
	List<ItemStack> itens;
	int timeRestart, timeCount;
	boolean spawned, enable;
	
	public Feast(Arena arena, List<Location> chests, List<ItemStack> itens, int timeRestart, int timeCount, boolean spawned, boolean enable) {
		this.arena = arena;
		this.chests = chests;
		this.itens = itens;
		this.timeRestart = timeRestart;
		this.timeCount = timeCount;
		this.spawned = spawned;
		this.enable = enable;
	}
	
	public void start() { 
		
	}
}
