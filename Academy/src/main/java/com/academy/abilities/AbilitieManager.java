package com.academy.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.academy.util.ItemBuilder;

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
	
	public void load() {
		//name,description,free,enable,cooldown,price,icon,itens
		add(new Abilitie("Ninja", true, true, 5000, "8s", new ItemBuilder(Material.EMERALD).setName("§aNinja").setDescription("Ao apertar SHIFT você", "teleporta até as costas do último", "jogador hitado"), null));
	}
	
}
