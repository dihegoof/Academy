package com.academy.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.academy.util.ItemBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@SuppressWarnings("deprecation")
public class Abilitie {
	
	static ItemBuilder ib = new ItemBuilder();
	
	String name;
	String[] description;
	boolean free, enable;
	int cooldown, price;
	ItemStack icon;
	ItemStack[] itens;
	
	public Abilitie() {
		this.name = getClass().getName();
		this.enable = true;
		this.itens = null;
	}
	
	public void setItens(ItemStack... itens) {
		this.itens = itens;
	}
	
	public void setDescription(String... description)  {
		this.description = description;
		ItemMeta itemMeta = this.icon.getItemMeta();

		List<String> lore = new ArrayList<String>();

		for (String lines : this.description) {
			lore.add("§7" + lines);
		}

		itemMeta.setDisplayName("§7" + this.name);
		itemMeta.setLore(lore);

		this.icon.setItemMeta(itemMeta);
	}
	
	public static ItemStack createItemStack(String name, Material material) {
		return ib.setMaterial(material).setName(name).getStack();
	}

	public static ItemStack createItemStack(String name, Material material, int amount) {
		return ib.setMaterial(material).setName(name).setAmount(amount).getStack();
	}

	public static ItemStack createItemStack(String name, String[] desc, Material material, int amount) {
		return ib.setMaterial(material).setName(name).setAmount(amount).setDescription(desc).getStack();
	}

	public static ItemStack createItemStack(String name, Material material, int amount, Enchantment enchant, int level) {
		return ib.setMaterial(material).setName(name).setAmount(amount).setEnchant(enchant, level).getStack();
	}
	
	
}
