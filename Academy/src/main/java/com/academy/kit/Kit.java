package com.academy.kit;

import java.util.List;

import com.academy.util.Config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kit {
	
	String name;
	List<String> itens, armor;
	
	public Kit(String name, List<String> itens, List<String> armor) {
		this.name = name;
		this.itens = itens;
		this.armor = armor;
	}
	
	public void save() {
		Config.getInstance().getKits().set("kits." + getName() + ".itens", getItens());
		Config.getInstance().getKits().set("kits." + getName() + ".armor", getArmor());
		Config.getInstance().save(Config.getInstance().getKits(), "kits");
	}
	
	public void delete() { 
		Config.getInstance().getKits().set("kits." + getName(), null);
		Config.getInstance().save(Config.getInstance().getKits(), "kits");
	}
}
