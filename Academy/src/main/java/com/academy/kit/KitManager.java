package com.academy.kit;

import java.util.ArrayList;
import java.util.List;

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
	
	public void load() { 
		int amount = 0;
		if(Config.getInstance().getKits().get("kits") == null)
			return;
		for(String names : Config.getInstance().getKits().getConfigurationSection("kits").getKeys(false)) {
			List<String> itens = Config.getInstance().getKits().getStringList("kits." + names + ".itens");
			List<String> armor = Config.getInstance().getKits().getStringList("kits." + names + ".armor");
			Kit kit = new Kit(names, itens, armor);
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
