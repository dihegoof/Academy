package com.academy.kit;

import java.util.List;

import org.bukkit.entity.Player;

import com.academy.util.Base64Encode;
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
	
	public void give(Player player) { 
		for(String itens : getItens()) {
			String[] split = itens.split(";");
			player.getInventory().setItem(Integer.valueOf(split[0]), Base64Encode.getInstance().itemStackFromBase64(split[1]));
		}
		for(String armor : getArmor()) {
			String[] split = armor.split(";");
			if(split[0].equalsIgnoreCase("helmet")) { 
				player.getInventory().setHelmet(split[1].equals("null") ? null : Base64Encode.getInstance().itemStackFromBase64(split[1]));
			} else if(split[0].equalsIgnoreCase("chestplate")) { 
				player.getInventory().setChestplate(split[1].equals("null") ? null : Base64Encode.getInstance().itemStackFromBase64(split[1]));
			} else if(split[0].equalsIgnoreCase("leggings")) { 
				player.getInventory().setLeggings(split[1].equals("null") ? null : Base64Encode.getInstance().itemStackFromBase64(split[1]));
			} else if(split[0].equalsIgnoreCase("boots")) { 
				player.getInventory().setBoots(split[1].equals("null") ? null : Base64Encode.getInstance().itemStackFromBase64(split[1]));
			}
		}
	}
}
