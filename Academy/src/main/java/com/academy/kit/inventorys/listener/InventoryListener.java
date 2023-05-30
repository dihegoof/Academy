package com.academy.kit.inventorys.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.academy.kit.inventorys.KitInventorys;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) { 
		if(event.getClickedInventory() == null) return;
		Player player = (Player) event.getWhoClicked();
		if(event.getInventory().getTitle().equals("Kits")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				KitInventorys.getInstance().listKits(player, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				KitInventorys.getInstance().listKits(player, pageNow);
			}
			return;
		}
	}
}
