package com.academy.arenas;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.academy.gamer.Gamer;
import com.academy.kit.Kit;
import com.academy.util.Config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Arena {
	
	String name;
	Material icon;
	int data;
	double x, y, z;
	float yaw, pitch;
	String world;
	List<Gamer> gamers;
	Kit kit;
	
	public Arena(String name, Material icon, int data, double x, double y, double z, float yaw, float pitch, String world, List<Gamer> gamers, Kit kit) {
		this.name = name;
		this.icon = icon;
		this.data = data;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.world = world;
		this.gamers = gamers;
		this.kit = kit;
	}
	
	public void save() {
		Config.getInstance().getArenas().set("arenas." + getName() + ".icon", getIcon().toString());
		Config.getInstance().getArenas().set("arenas." + getName() + ".data", getData());
		Config.getInstance().getArenas().set("arenas." + getName() + ".loc.x", getX());
		Config.getInstance().getArenas().set("arenas." + getName() + ".loc.y", getY());
		Config.getInstance().getArenas().set("arenas." + getName() + ".loc.z", getZ());
		Config.getInstance().getArenas().set("arenas." + getName() + ".loc.yaw", getYaw());
		Config.getInstance().getArenas().set("arenas." + getName() + ".loc.pitch", getPitch());
		Config.getInstance().getArenas().set("arenas." + getName() + ".loc.world", getWorld());
		Config.getInstance().getArenas().set("arenas." + getName() + ".kit", getKit() == null ? "none" : getKit().getName());
		Config.getInstance().save(Config.getInstance().getArenas(), "arenas");
	}
	
	public void delete() { 
		Config.getInstance().getArenas().set("arenas." + getName(), null);
		Config.getInstance().save(Config.getInstance().getArenas(), "arenas");
	}
	
	public Location getLocation() {
		Location location = new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ());
		location.setYaw(getYaw());
		location.setPitch(getPitch());
		return location;
	}
	
	public void add(Gamer gamer) { 
		List<Gamer> list = getGamers();
		if(!list.contains(gamer)) {
			list.add(gamer);
			setGamers(list);
		}
	}
	
	public void remove(Gamer gamer) {
		List<Gamer> list = getGamers();
		if(list.contains(gamer)) {
			list.remove(gamer);
			setGamers(list);
		}
	}
	
	public void prepareGamer(Gamer gamer) {
		gamer.getPlayer().closeInventory();
		gamer.getPlayer().getInventory().clear();
		gamer.setArena(this);
		gamer.teleportToArena();
		if(getKit() != null) 
			getKit().give(gamer.getPlayer());
	}
}
