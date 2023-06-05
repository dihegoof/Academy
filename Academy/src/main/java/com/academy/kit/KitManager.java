package com.academy.kit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.academy.Main;
import com.academy.util.Config;

import lombok.Getter;

public class KitManager {
	
	@Getter
	static KitManager instance = new KitManager();
	@Getter
	List<Kit> kits = new ArrayList<>();
	
	public void add(Kit kit) { 
		if(!kits.contains(kit)) 
			kits.add(kit);
	}
	
	public void remove(Kit kit) { 
		if(kits.contains(kit)) 
			kits.remove(kit);
	}
	
	public Kit get(String key) { 
		for(Kit ki : kits) { 
			if(ki.getName().equalsIgnoreCase(key))
				return ki;
		}
		return null;
	}
	
	public List<Kit> get(Type type) { 
		List<Kit> list = new ArrayList<>();
		for(Kit ki : kits) { 
			if(ki.getType().equals(type))
				list.add(ki);
		}
		return list;
	}
	
	public void load() { 
		int amount = 0;
		if(Config.getInstance().getKits().get("kits") == null)
			return;
		for(String names : Config.getInstance().getKits().getConfigurationSection("kits").getKeys(false)) {
			List<String> itens = Config.getInstance().getKits().getStringList("kits." + names + ".itens");
			List<String> armor = Config.getInstance().getKits().getStringList("kits." + names + ".armor");
			Type type = Type.valueOf(Config.getInstance().getKits().getString("kits." + names + ".type"));
			Material icon = Material.valueOf(Config.getInstance().getKits().getString("kits." + names + ".icon"));
			int data = Config.getInstance().getKits().getInt("kits." + names + ".data");
			Kit kit = new Kit(names, itens, armor, type, icon, data);
			add(kit);
			amount++;
		}
		if(amount > 0) 
			Main.debug("Carregados " + amount + " kit(s).");
	}
	
	public void save() {
		for(Kit ki : kits) { 
			ki.save();
		}
	}
}
