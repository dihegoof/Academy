package com.academy.arenas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.academy.Main;
import com.academy.kit.Kit;
import com.academy.kit.KitManager;
import com.academy.util.Config;

import lombok.Getter;

public class ArenaManager {
	
	@Getter
	static ArenaManager instance = new ArenaManager();
	@Getter
	List<Arena> arenas = new ArrayList<>();
	
	public void add(Arena arena) { 
		if(!arenas.contains(arena)) 
			arenas.add(arena);
	}
	
	public void remove(Arena arena) { 
		if(arenas.contains(arena)) 
			arenas.remove(arena);
	}
	
	public Arena get(String key) { 
		for(Arena ar : arenas) { 
			if(ar.getName().equalsIgnoreCase(key))
				return ar;
		}
		return null;
	}
	
	public void load() {
		int amount = 0;
		if(Config.getInstance().getArenas().get("arenas") == null) {
			add(new Arena("Spawn", Material.BED, 0, 0.0D, 90.0D, 0.0D, 0.0F, 0.0F, "world", new ArrayList<>(), null, null, false, false));
			Main.debug("Gerado spawn!");
			return;
		}
		for(String names : Config.getInstance().getArenas().getConfigurationSection("arenas").getKeys(false)) {
			Material icon = Material.valueOf(Config.getInstance().getArenas().getString("arenas." + names + ".icon"));
			int data = Config.getInstance().getArenas().getInt("arenas." + names + ".data");
			double x = Config.getInstance().getArenas().getDouble("arenas." + names + ".loc.x");
			double y = Config.getInstance().getArenas().getDouble("arenas." + names + ".loc.y");
			double z = Config.getInstance().getArenas().getDouble("arenas." + names + ".loc.z");
			float yaw = Config.getInstance().getArenas().getFloat("arenas." + names + ".loc.yaw");
			float pitch = Config.getInstance().getArenas().getFloat("arenas." + names + ".loc.pitch");
			String world = Config.getInstance().getArenas().getString("arenas." + names + ".loc.world");
			Kit kit = KitManager.getInstance().get(Config.getInstance().getArenas().getString("arenas." + names + ".kit"));
			List<String> abilities = Config.getInstance().getArenas().getStringList("arenas." + names + ".abilities");
			boolean allowAbilities = Config.getInstance().getArenas().getBoolean("arenas." + names + ".allowabilities");
			boolean allowFeast = Config.getInstance().getArenas().getBoolean("arenas." + names + ".allowfeast");
			Arena arena = new Arena(names, icon, data, x, y, z, yaw, pitch, world, new ArrayList<>(), (kit == null ? null : kit), abilities, allowAbilities, allowFeast);
			add(arena);
			amount++;
		}
		if(amount > 0) 
			Main.debug("Carregados " + amount + " arena(s).");
	}
	
	public void save() {
		for(Arena ar : arenas) { 
			ar.save();
		}
	}
}
