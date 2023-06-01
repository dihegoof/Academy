package com.academy.gamer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.academy.abilities.Abilitie;
import com.academy.arenas.Arena;
import com.academy.arenas.ArenaManager;
import com.academy.util.TimeCount;
import com.academy.util.Utils;

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
	Abilitie abilitie;
	
	static HashMap<Player, Long> timeWaiting;
	
	public Gamer(UUID uniqueId, String nickName, Player player, State stateGamer, boolean online, boolean invencible) {
		this.uniqueId = uniqueId;
		this.nickName = nickName;
		this.player = player;
		this.stateGamer = stateGamer;
		this.online = online;
		this.invencible = invencible;
		this.arena = null;
		this.abilitie = null;
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
	
	public boolean hasArena(String name) {
		return getArena().getName().equals(name);
	}
	
	public boolean hasAbilitie(String name) { 
		return getAbilitie().getName().equals(name);
	}
	
	public void addCooldown(String time) { 
		if(!timeWaiting.containsKey(getPlayer()))
			timeWaiting = new HashMap<>();
		
		timeWaiting.put(getPlayer(), TimeCount.getInstance().getTime(time));
	}
	
	public void removeCooldown() { 
		if(timeWaiting.containsKey(getPlayer()))
			timeWaiting.remove(getPlayer());
	}
	
	public boolean inCooldown() { 
		return timeWaiting.containsKey(getPlayer()) && timeWaiting.get(getPlayer()) > System.currentTimeMillis();
	}
	
	public String getCooldown() { 
		if(timeWaiting.containsKey(getPlayer())) { 
			return Utils.getInstance().compareSimpleTime(timeWaiting.get(getPlayer()));
		}
		return "0s";
	}
}
