package com.academy.arenas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.academy.Main;
import com.academy.arenas.feast.Feast;
import com.academy.gamer.Gamer;
import com.academy.kit.Kit;
import com.academy.util.Base64Encode;
import com.academy.util.Config;
import com.academy.util.ItemBuilder;

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
	List<String> abilities;
	boolean allowAbilities, allowFeast, build;
	Feast feast;
	List<Block> blocks;
	
	public Arena(String name, Material icon, int data, double x, double y, double z, float yaw, float pitch, String world, List<Gamer> gamers, Kit kit, List<String> abilities, boolean allowAbilities, boolean allowFeast, boolean build) {
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
		this.abilities = abilities;
		this.allowAbilities = allowAbilities;
		this.allowFeast = allowFeast;
		this.build = build;
		if(build)
			this.blocks = new ArrayList<>();
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
		Config.getInstance().getArenas().set("arenas." + getName() + ".abilities", getAbilities());
		Config.getInstance().getArenas().set("arenas." + getName() + ".allowabilities", isAllowAbilities());
		Config.getInstance().getArenas().set("arenas." + getName() + ".allowfeast", isAllowFeast());
		Config.getInstance().getArenas().set("arenas." + getName() + ".build", isBuild());
		if(isAllowFeast() && getFeast() != null) { 
			List<String> locations = new ArrayList<>();
			List<String> itens = new ArrayList<>();
			for(Location lo : getFeast().getChests()) { 
				locations.add(serialise(lo));
			}
			Config.getInstance().getArenas().set("arenas." + getName() + ".feast.chests", locations);
			for(ItemStack it : getFeast().getItens()) { 
				if(it != null && !it.getType().equals(Material.AIR)) { 
					itens.add(Base64Encode.getInstance().itemStackToBase64(it));
				}
			}
			Config.getInstance().getArenas().set("arenas." + getName() + ".feast.itens", itens);
			Config.getInstance().getArenas().set("arenas." + getName() + ".feast.timerestart", getFeast().getTimeRestart());
			Config.getInstance().getArenas().set("arenas." + getName() + ".feast.enable", getFeast().isEnable());
		} else if(!isAllowFeast() && getFeast() != null) { 
			Config.getInstance().getArenas().set("arenas." + getName() + ".feast", null);
		}
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
		gamer.getPlayer().getInventory().setArmorContents(null);
		gamer.setArena(this);
		gamer.teleportToArena();
		if(getKit() != null) 
			getKit().give(gamer.getPlayer());
		if(gamer.getAbilitie().getItem() != null && !gamer.getAbilitie().getItem().equals(Material.AIR)) { 
			new ItemBuilder(gamer.getAbilitie().getItem()).setDurability(gamer.getAbilitie().getDataItem()).setName("§a" + gamer.getAbilitie().getName()).build(gamer.getPlayer());
		}
	}
	
	public String serialise(Location location) { 
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch() + ";" + location.getWorld().getName());
		return stringBuilder.toString();
	}
	
	public void startClear() { 
		if(isBuild()) { 
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if(!getBlocks().isEmpty()) { 
						for(Block bl : getBlocks()) { 
							if(bl == null || bl.getType() == Material.AIR) continue;
							bl.setType(Material.AIR);
						}
						setBlocks(new ArrayList<>());
					}
				}
			}.runTaskTimer(Main.getPlugin(), 20, 300 * 20L);
		}
	}
}
