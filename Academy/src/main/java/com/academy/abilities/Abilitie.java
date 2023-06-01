package com.academy.abilities;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.academy.util.ItemBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@SuppressWarnings("deprecation")
public class Abilitie {
	
	String name;
	boolean free, enable;
	int price;
	String cooldown;
	ItemBuilder icon;
	ItemStack[] itens;
	
	public Abilitie(String name, boolean free, boolean enable, int price, String cooldown, ItemBuilder icon, ItemStack[] itens) {
		this.name = name;
		this.free = free;
		this.enable = enable;
		this.cooldown = cooldown;
		this.price = price;
		this.icon = icon;
		this.itens = itens;
	}
	
	public void setItens(ItemStack... itens) {
		this.itens = itens;
	}
	
	static ItemBuilder ib = new ItemBuilder();
	
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
