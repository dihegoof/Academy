package com.academy.arenas.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.academy.arenas.Arena;
import com.academy.arenas.ArenaManager;
import com.academy.util.ItemBuilder;

import lombok.Getter;

@SuppressWarnings("deprecation")
public class ArenaInventorys {
	
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
	static ArenaInventorys instance = new ArenaInventorys();
	
	public void listArenas(Player player, int page) {
		Inventory inventory = Bukkit.createInventory(player, 54, "Arenas");
		ItemBuilder ib = null;
		List<Arena> list = ArenaManager.getInstance().getArenas();
		Arena arena = null;
		int start = 0;
		start = (page > 1 ? (28 * page) - 28 : 0);
		if(list.size() == 0) {
			ib = new ItemBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma arena encontrada");
			ib.build(inventory, 22);
		}
		for(int x = 0; x < inventory.getSize(); x++) {
			try {  if(list.get(start) == null) continue; } catch (Exception e) { break; }
			if(inventory.getItem(x) != null) continue;
			if(empty.contains(x)) continue;
			arena = list.get(start);
			ib = new ItemBuilder().setMaterial(arena.getIcon()).setDurability(arena.getData()).setName("§aArena " + arena.getName()).setDescription("§fJogadores §7" + arena.getGamers().size());
			ib.build(inventory, x);
			start++;
		}
		if(page > 1) { 
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
