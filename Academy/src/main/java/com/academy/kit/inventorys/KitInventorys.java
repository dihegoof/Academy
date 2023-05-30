package com.academy.kit.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.academy.kit.Kit;
import com.academy.kit.KitManager;
import com.academy.util.ItemBuilder;

import lombok.Getter;

@SuppressWarnings("deprecation")
public class KitInventorys {

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
	static KitInventorys instance = new KitInventorys();
	
	public void listKits(Player player, int page) {
		Inventory inventory = Bukkit.createInventory(player, 54, "Kits");
		ItemBuilder ib = null;
		List<Kit> list = KitManager.getInstance().getKits();
		Kit kit = null;
		int start = 0;
		start = (page > 1 ? (28 * page) - 28 : 0);
		if(list.size() == 0) {
			ib = new ItemBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum kit encontrado");
			ib.build(inventory, 22);
		}
		for(int x = 0; x < inventory.getSize(); x++) {
			try {  if(list.get(start) == null) continue; } catch (Exception e) { break; }
			if(inventory.getItem(x) != null) continue;
			if(empty.contains(x)) continue;
			kit = list.get(start);
			ib = new ItemBuilder().setMaterial(Material.PAPER).setName("§aKit §7" + kit.getName()).setDescription("§7Clique aqui para ver.");
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
