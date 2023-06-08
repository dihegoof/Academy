package com.academy.gamer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.academy.abilities.Abilitie;
import com.academy.abilities.AbilitieManager;
import com.academy.arenas.Arena;
import com.academy.arenas.ArenaManager;
import com.academy.minigames.damager.DamageManager;
import com.academy.minigames.damager.Damager;
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
	boolean online, invencible, damager;
	Arena arena;
	Abilitie abilitie;
	
	HashMap<String, Long> timeWaiting;
	
	public Gamer(UUID uniqueId, String nickName, Player player, State stateGamer, boolean online, boolean invencible) {
		this.uniqueId = uniqueId;
		this.nickName = nickName;
		this.player = player;
		this.stateGamer = stateGamer;
		this.online = online;
		this.invencible = invencible;
		this.damager = false;
		this.arena = null;
		this.abilitie = AbilitieManager.getInstance().get("Nenhum");
		timeWaiting = new HashMap<>();
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
	
	public void updateTag() { 
		getPlayer().setPlayerListName("§7" + getNickName());
	}
	
	public void prepareChallenge(Damager damager) {
		getPlayer().closeInventory();
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setArmorContents(null);
		if(damager.getKit() != null) 
			damager.getKit().give(getPlayer());
		damager.getKit().completeSoup(getPlayer());
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
	
	public void removeAbilitie() { 
		setAbilitie(AbilitieManager.getInstance().get("Nenhum"));
	}
	
	public void addCooldown(String time) { 
		timeWaiting.put(getNickName(), TimeCount.getInstance().getTime(time));
	}
	
	public void removeCooldown() { 
		if(timeWaiting.containsKey(getNickName()))
			timeWaiting.remove(getNickName());
	}
	
	public boolean inCooldown() { 
		return timeWaiting.containsKey(getNickName()) 
				&& timeWaiting.get(getNickName()) > System.currentTimeMillis();
	}
	
	public String getCooldown() { 
		if(timeWaiting.containsKey(getNickName())) { 
			return Utils.getInstance().compareSimpleTime(timeWaiting.get(getNickName()));
		}
		return "0s";
	}
	
	public boolean isPlayer() { 
		return getStateGamer().equals(State.PLAYER);
	}
	
	public boolean isAdmin() { 
		return getStateGamer().equals(State.ADMIN);
	}
	
	public boolean isSpec() { 
		return getStateGamer().equals(State.SPEC);
	}
	
	public void removeOtherConstructors() { 
		if(DamageManager.getInstance().get(getPlayer()) != null) {
			DamageManager.getInstance().get(getPlayer()).cancel();
			DamageManager.getInstance().remove(DamageManager.getInstance().get(getPlayer()));
		}
	}
}
