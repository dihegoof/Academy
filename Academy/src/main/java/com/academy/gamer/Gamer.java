package com.academy.gamer;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.academy.arenas.Arena;
import com.academy.arenas.ArenaManager;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class Gamer {
	
	UUID uniqueId;
	String nickName;
	Player player;
	State stateGamer;	
	boolean online, invencible;
	Arena arena;
	
	public Gamer(UUID uniqueId, String nickName, Player player, State stateGamer, boolean online, boolean invencible, Arena arena) {
		this.uniqueId = uniqueId;
		this.nickName = nickName;
		this.player = player;
		this.stateGamer = stateGamer;
		this.online = online;
		this.invencible = invencible;
		this.arena = arena;
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
	
	public void preparePlayer() {
		if(getPlayer() == null) return;
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setArmorContents(null);
		getPlayer().setHealth(20.0D);
		getPlayer().setFoodLevel(20);
		getPlayer().setAllowFlight(false);
		getPlayer().setFlying(false);
		for(PotionEffect po : getPlayer().getActivePotionEffects()) 
			getPlayer().getActivePotionEffects().remove(po);
	}
	
	public void setSpawn() { 
		setArena(ArenaManager.getInstance().get("Spawn"));
		teleportToArena();
	}
	
	public void teleportToArena() {
		getPlayer().teleport(getArena().getLocation());
	}

	public boolean outSpawn() { 
		return isOnline() && !getArena().getName().equals("Spawn");
	}
}
