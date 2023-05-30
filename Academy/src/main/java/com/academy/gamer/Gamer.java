package com.academy.gamer;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class Gamer {
	
	UUID uniqueId;
	Player player;
	State stateGamer;	
	boolean online;
	
	public Gamer(UUID uniqueId, Player player, State stateGamer, boolean online) {
		this.uniqueId = uniqueId;
		this.player = player;
		this.stateGamer = stateGamer;
		this.online = online;
	}
	
	public boolean inventoryEmpty() { 
		for(ItemStack is : getPlayer().getInventory().getContents()) { 
			if(is != null) { 
				return false;
			}
		}
		return true;
	}
	
	public int slotsFree() { 
		int slotsFree = 0;
		for(ItemStack is : getPlayer().getInventory().getContents()) {
			slotsFree += (is == null ? 1 : 0);
		}
		return slotsFree;
	}
}
