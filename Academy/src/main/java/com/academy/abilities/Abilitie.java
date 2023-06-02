package com.academy.abilities;

import java.util.List;

import org.bukkit.Material;

import com.academy.util.Config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Abilitie {
	
	String name;
	boolean free, enable;
	double price;
	String cooldown;
	List<String> description;
	Material icon, item;
	int dataIcon, dataItem;
	
	public Abilitie(String name, boolean free, boolean enable, double price, String cooldown, List<String> description, Material icon, Material item, int dataIcon, int dataItem) {
		this.name = name;
		this.free = free;
		this.enable = enable;
		this.price = price;
		this.cooldown = cooldown;
		this.description = description;
		this.icon = icon;
		this.item = item;
		this.dataIcon = dataIcon;
		this.dataItem = dataItem;
	}
	
	public void save() {
		Config.getInstance().getAbilities().set("abilities." + getName() + ".free", isFree());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".enable", isEnable());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".cooldown", getCooldown());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".description", getDescription());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".price", getPrice());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".icon", getIcon().toString());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".dataicon", getDataIcon());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".item", getItem().toString());
		Config.getInstance().getAbilities().set("abilities." + getName() + ".dataitem", getDataItem());
		Config.getInstance().save(Config.getInstance().getAbilities(), "abilities");
	}
}
