package com.academy.kit;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.academy.util.Base64Encode;
import com.academy.util.Config;
import com.academy.util.ItemBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kit {
	
	String name;
	List<String> itens, armor;
	Type type;
	Material icon;
	int data;
	
	public Kit(String name, List<String> itens, List<String> armor, Type type, Material icon, int data) {
		this.name = name;
		this.itens = itens;
		this.armor = armor;
		this.type = type;
		this.icon = icon;
		this.data = data;
	}
	
	public void save() {
		Config.getInstance().getKits().set("kits." + getName() + ".itens", getItens());
		Config.getInstance().getKits().set("kits." + getName() + ".armor", getArmor());
		Config.getInstance().getKits().set("kits." + getName() + ".type", getType().toString());
		Config.getInstance().getKits().set("kits." + getName() + ".icon", getIcon().toString());
		Config.getInstance().getKits().set("kits." + getName() + ".data", getData());
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
	
	public void completeSoup(Player player) {
		for(int i = 0; i < player.getInventory().getSize(); i++) { 
			if(player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType().equals(Material.AIR))
				player.getInventory().addItem(new ItemBuilder(Material.MUSHROOM_SOUP).getStack());
		}
	}
	
	public int slotsFree(Inventory inventory) { 
		int slotsFree = 0;
		for(ItemStack is : inventory.getContents()) {
			slotsFree += (is == null ? 1 : 0);
		}
		return slotsFree;
	}
}
