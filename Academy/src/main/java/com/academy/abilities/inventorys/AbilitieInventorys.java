package com.academy.abilities.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.academy.abilities.Abilitie;
import com.academy.arenas.Arena;
import com.academy.util.ItemBuilder;

import lombok.Getter;

@SuppressWarnings("deprecation")
public class AbilitieInventorys {
	
	private static List<Integer> empty = new ArrayList<>();
	private static Integer[] slots = { 18, 27, 36, 17, 26, 35, 44 };
	static {
		for(int i = 0; i <= 9; i++)
			empty.add(i);
		for(int i = 45; i <= 53; i++)
			empty.add(i);
		for(Integer in : slots)
			empty.add(in);
	}
	
	@Getter
	static AbilitieInventorys instance = new AbilitieInventorys();
	
	public void listAbilities(Player player, Arena arena, int page) {
		Inventory inventory = Bukkit.createInventory(player, 54, "Habilidades");
		ItemBuilder ib = null;
		List<Abilitie> list = arena.getAbilities();
		Abilitie abilitie = null;
		int start = 0;
		start = (page > 1 ? (28 * page) - 28 : 0);
		if(list.size() == 0) {
			ib = new ItemBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma habilidade encontrada");
			ib.build(inventory, 22);
		}
		for(int x = 0; x < inventory.getSize(); x++) {
			try {  if(list.get(start) == null) continue; } catch (Exception e) { break; }
			if(inventory.getItem(x) != null) continue;
			if(empty.contains(x)) continue;
			abilitie = list.get(start);
			ib = abilitie.getIcon();
			ib.build(inventory, x);
			start++;
		}
		if(page == 1) { 
			ib = new ItemBuilder().setMaterial(Material.ARROW).setName("§cCancelar").setDescription("§7Clique aqui cancelar a entrada na arena!");
			ib.build(inventory, 45);
		} else if(page > 1) { 
			ib = new ItemBuilder().setMaterial(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
			ib.build(inventory, 45);
		}
		if(inventory.getItem(43) != null) { 
			ib = new ItemBuilder().setMaterial(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
			ib.build(inventory, 53);
		}
		player.openInventory(inventory);
	}
}
