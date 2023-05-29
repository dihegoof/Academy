package com.academy.util;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class Serialise {
	
	static Serialise instance = new Serialise();
	
	//Slot;IDItem;Data;Quantidade;Encantamento1-LevelEncantamento1,Encantamento2-LevelEncantamento2
	//IDItem;Data;Encantamento1-LevelEncantamento1,Encantamento2-LevelEncantamento2
	
	public String serialise(ItemStack itemStack, int slot, boolean armor, Player player) {
		StringBuilder stringBuilder = new StringBuilder();
		if(armor) { 
			for(ItemStack armorContents : player.getInventory().getArmorContents()) { 
				stringBuilder.append(armorContents.getType().toString() + ";" + armorContents.getDurability());
				if(player.getInventory().getChestplate().getEnchantments().size() > 0) { 
					String listEnchants = null;
					for(Map.Entry<Enchantment, Integer> en : armorContents.getEnchantments().entrySet()) {
						listEnchants += en.getKey().getName() + "-" + en.getValue() + ",";
					}
					stringBuilder.append(";" + listEnchants.toString().substring(0, listEnchants.length() - 1));
				} else { 
					stringBuilder.append(";null");
				}
			}
		} else { 
			stringBuilder.append(slot + ";" + itemStack.getTypeId() + ";" + itemStack.getDurability() + ";" + itemStack.getAmount());
			if(itemStack.getEnchantments().size() > 0) { 
				String listEnchants = null;
				for(Map.Entry<Enchantment, Integer> en : itemStack.getEnchantments().entrySet()) {
					listEnchants += en.getKey().getName() + "-" + en.getValue() + ",";
				}
				stringBuilder.append(";" + listEnchants.toString().substring(0, listEnchants.length() - 1));
			} else { 
				stringBuilder.append(";null");
			}
		}
		return stringBuilder.toString();
	}
	
	public ItemStack deserialise(String toString) { 
		ItemBuilder itemBuilder = null;
		String[] split = toString.split(";");
		itemBuilder = new ItemBuilder().setMaterial(Material.getMaterial(Integer.valueOf(split[1]))).setDurability(Integer.valueOf(split[2])).setAmount(Integer.valueOf(split[3]));
		if(!split[4].equals("null")) { 
			String[] splitEnchant = split[4].split(",");
			for(String names : splitEnchant) {
				String[] list = names.split("-");
				itemBuilder.setEnchant(Utils.getInstance().getTranslateEnchant(list[0]), Integer.valueOf(list[1]));
			}
		}
		return itemBuilder.getStack();
	}
	
	public void deserialise(String toString, Inventory inventory) { 
		ItemBuilder itemBuilder = null;
		String[] split = toString.split(";");
		itemBuilder = new ItemBuilder().setMaterial(Material.getMaterial(Integer.valueOf(split[1]))).setDurability(Integer.valueOf(split[2])).setAmount(Integer.valueOf(split[3]));
		if(!split[4].equals("null")) { 
			String[] splitEnchant = split[4].split(",");
			for(String names : splitEnchant) {
				String[] list = names.split("-");
				itemBuilder.setEnchant(Utils.getInstance().getTranslateEnchant(list[0]), Integer.valueOf(list[1]));
			}
		}
		itemBuilder.build(inventory, Integer.valueOf(split[0]));
	}
	
	public void deserialise(String toString, Player player) { 
		ItemBuilder itemBuilder = null;
		String[] split = toString.split(";");
		if(!split[2].equals("null")) { 
			itemBuilder = new ItemBuilder().setMaterial(Material.getMaterial(Integer.valueOf(split[0]))).setDurability(Integer.valueOf(split[1]));
			String[] splitEnchant = split[4].split(",");
			for(String names : splitEnchant) {
				String[] list = names.split("-");
				itemBuilder.setEnchant(Utils.getInstance().getTranslateEnchant(list[0]), Integer.valueOf(list[1]));
			}
		}
		if(split[0].contains("helmet")) {
			player.getInventory().setHelmet(itemBuilder.getStack());
		}
		if(split[0].contains("chestplate")) { 
			player.getInventory().setHelmet(itemBuilder.getStack());
		}
		if(split[0].contains("leggings")) { 
			player.getInventory().setHelmet(itemBuilder.getStack());
		}
		if(split[0].contains("boots")) {
			player.getInventory().setHelmet(itemBuilder.getStack());
		}
	}
}
