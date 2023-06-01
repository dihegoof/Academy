package com.academy.abilities.types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.academy.abilities.Abilitie;

public class Kangaroo extends Abilitie {
	
	public Kangaroo() {
		setPrice(50000);
		setCooldown(0);
		setIcon(new ItemStack(Material.FIREWORK));
		setDescription("Tranforme-se em um kanguru e pule", "correndo atrás de seus inimigos.");
		setItens(createItemStack("§aKangaroo", Material.FIREWORK));
	}

}
