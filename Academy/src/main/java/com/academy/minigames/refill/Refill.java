package com.academy.minigames.refill;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.academy.util.ItemBuilder;
import com.academy.util.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Refill extends Utils {
	
	Player player;
	TypesRefill typeRefill;
	long time;
	boolean start;
	
	public Refill(Player player, TypesRefill typeRefill, long time, boolean start) {
		this.player = player;
		this.typeRefill = typeRefill;
		this.time = time;
		this.start = start;
	}
	
	public void prepareInventory() { 
		if(getTypeRefill().equals(TypesRefill.LINE)) { 
			int column = new Random().nextInt(2);
			for(int i = (column == 0 ? 9 : (column == 1 ? 18 : 27)); i <= (column == 0 ? 17 : (column == 1 ? 26 : 35)); i++) { 
				getPlayer().getInventory().setItem(i, new ItemBuilder(Material.MUSHROOM_SOUP).getStack());
			}
		} else { 
			List<Integer> slots = new ArrayList<>();
			for(int i = 9; i < getPlayer().getInventory().getSize(); i++) 
				slots.add(i);
			for(int i = 0; i <= 9; i++) { 
				getPlayer().getInventory().setItem(slots.get(new Random().nextInt(slots.size())), new ItemBuilder(Material.MUSHROOM_SOUP).getStack());
			}
		}
	}
}
