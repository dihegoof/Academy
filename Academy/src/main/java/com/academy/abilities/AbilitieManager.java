package com.academy.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.academy.Main;
import com.academy.util.Config;

import lombok.Getter;

public class AbilitieManager {
	
	@Getter
	static AbilitieManager instance = new AbilitieManager();
	@Getter
	List<Abilitie> abilities = new ArrayList<>();
	
	public void add(Abilitie abilitie) { 
		if(!abilities.contains(abilitie)) 
			abilities.add(abilitie);
	}
	
	public void remove(Abilitie abilitie) { 
		if(abilities.contains(abilitie)) 
			abilities.remove(abilitie);
	}
	
	public Abilitie get(String key) { 
		for(Abilitie ab : abilities) { 
			if(ab.getName().equalsIgnoreCase(key))
				return ab;
		}
		return null;
	}
	
	public List<String> list() { 
		List<String> list = new ArrayList<>();
		for(Abilitie ab : abilities) { 
			list.add(ab.getName());
		}
		return list;
	}
	
	public void load() {
		//name, free, enable, price, cooldown, icon, item, dataIcon, dataItem
		int amount = 0;
		if(Config.getInstance().getAbilities().get("abilities") == null) {
			add(new Abilitie("Nenhum", true, true, 0, "0s", Arrays.asList("Nenhuma habilidade"), Material.STONE, Material.AIR, 0, 0));
			add(new Abilitie("Ninja", true, true, 5000, "8s", Arrays.asList("Ao apertar SHIFT você ", "teleporta até o ", "último jogador hitado!"), Material.EMERALD, Material.AIR, 0, 0));
			Main.debug("Gerado habilidades!");
			return;
		}
		for(String names : Config.getInstance().getAbilities().getConfigurationSection("abilities").getKeys(false)) {
			boolean free = Config.getInstance().getAbilities().getBoolean("abilities." + names + ".free");
			boolean enable = Config.getInstance().getAbilities().getBoolean("abilities." + names + ".enable");
			String cooldown = Config.getInstance().getAbilities().getString("abilities." + names + ".cooldown");
			List<String> description = Config.getInstance().getAbilities().getStringList("abilities." + names + ".description");
			double price = Config.getInstance().getAbilities().getDouble("abilities." + names + ".price");
			Material icon = Material.valueOf( Config.getInstance().getAbilities().getString("abilities." + names + ".icon"));
			int dataIcon = Config.getInstance().getAbilities().getInt("abilities." + names + ".data");
			Material item = Material.valueOf( Config.getInstance().getAbilities().getString("abilities." + names + ".item"));
			int dataItem = Config.getInstance().getAbilities().getInt("abilities." + names + ".dataitem");
			Abilitie abilitie = new Abilitie(names, free, enable, price, cooldown, description, icon, item, dataIcon, dataItem);
			add(abilitie);
			amount++;
		}
		if(amount > 0) 
			Main.debug("Carregados " + amount + " habilidade(s).");
	}

	public void save() {
		for(Abilitie ab : abilities) { 
			ab.save();
		}
	}
}
