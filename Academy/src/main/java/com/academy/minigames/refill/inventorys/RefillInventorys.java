package com.academy.minigames.refill.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.academy.minigames.refill.Refill;
import com.academy.minigames.refill.TypesRefill;
import com.academy.util.ItemBuilder;

import lombok.Getter;

public class RefillInventorys {
	
	@Getter
	static RefillInventorys instance = new RefillInventorys();

	public void create(Player player, Refill refill) { 
		Inventory inventory = Bukkit.createInventory(player, 27, "Refill");
		ItemBuilder ib = null;
		ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cCancelar").setDescription("§7Clique aqui para cancelar!");
		ib.build(inventory, 11);
		ib = new ItemBuilder(Material.MUSHROOM_SOUP).setName("§aTipo §7" + (refill.getTypeRefill().equals(TypesRefill.LINE) ? "Linha" : "Bagunçado")).setDescription("§7Clique aqui para alterar o tipo do inventário!");
		ib.build(inventory, 13);
		ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(5).setName("§aConfirmar").setDescription("§7Clique aqui para confirmar o desafio!");
		ib.build(inventory, 15);
		player.openInventory(inventory);
	}
}
