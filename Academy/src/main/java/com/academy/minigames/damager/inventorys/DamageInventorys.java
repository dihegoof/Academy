package com.academy.minigames.damager.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.academy.kit.Kit;
import com.academy.kit.KitManager;
import com.academy.kit.Type;
import com.academy.minigames.damager.Damager;
import com.academy.util.ItemBuilder;

import lombok.Getter;

public class DamageInventorys {
	
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
	static DamageInventorys instance = new DamageInventorys();
	
	public void create(Player player, Damager damager) { 
		Inventory inventory = Bukkit.createInventory(player, 27, "Personalização");
		ItemBuilder ib = null;
		ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cCancelar").setDescription("§7Clique aqui para cancelar!");
		ib.build(inventory, 10);
		ib = new ItemBuilder(Material.DIAMOND_SWORD).setName("§aDano").setDescription("§7" + damager.getDamage() + " §7corações", "", "§7Clique aqui para alterar os corações por segundo!");
		ib.build(inventory, 12);
		if(damager.getKit() == null) { 
			ib = new ItemBuilder(Material.CHEST).setName("§aSelecione o kit").setDescription("§7Clique aqui para alterar o kit do desafio!");
			ib.build(inventory, 13);
		} else { 
			ib = new ItemBuilder(damager.getKit().getIcon()).setDurability(damager.getKit().getData()).setName("§a" + damager.getKit().getName());
			ib.build(inventory, 13);
		}
		ib = new ItemBuilder(Material.POTION).setDurability(16462).setName("§aTreinar invisível?").setDescription((damager.isHidden() ? "§aSim" : "§cNão"), "", "§7Clique aqui para alterar a visibilidade dos outros jogadores!");
		ib.build(inventory, 14);
		ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(5).setName("§aConfirmar").setDescription("§7Clique aqui para confirmar a personalização do desafio!");
		ib.build(inventory, 16);
		player.openInventory(inventory);
	}
	
	public void listKits(Player player, int page) {
		Inventory inventory = Bukkit.createInventory(player, 54, "Kits para Resistência");
		ItemBuilder ib = null;
		List<Kit> list = KitManager.getInstance().get(Type.RESISTANCE);
		Kit kit = null;
		int start = 0;
		start = (page > 1 ? (28 * page) - 28 : 0);
		if(list.size() == 0) {
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum kit encontrado");
			ib.build(inventory, 22);
		}
		for(int x = 0; x < inventory.getSize(); x++) {
			try {  if(list.get(start) == null) continue; } catch (Exception e) { break; }
			if(inventory.getItem(x) != null) continue;
			if(empty.contains(x)) continue;
			kit = list.get(start);
			ib = new ItemBuilder(kit.getIcon()).setDurability(kit.getData()).setName("§aKit §7" + kit.getName()).setDescription("§7Clique aqui para selecionar");
			ib.build(inventory, x);
			start++;
		}
		if(page == 1) { 
			ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
			ib.build(inventory, 45);
		} else if(page > 1) { 
			ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
			ib.build(inventory, 45);
		}
		if(inventory.getItem(43) != null) { 
			ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
			ib.build(inventory, 53);
		}
		player.openInventory(inventory);
	}
}
