package com.academy.minigames.refill.inventorys.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.academy.Main;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.minigames.refill.Refill;
import com.academy.minigames.refill.RefillManager;
import com.academy.minigames.refill.TypesRefill;
import com.academy.minigames.refill.inventorys.RefillInventorys;
import com.academy.util.Utils;

public class InventoryListener extends Utils implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) { 
		if(event.getClickedInventory() == null) return;
		Player player = (Player) event.getWhoClicked();
		Gamer gamer = GamerManager.getInstance().get(player.getName());
		if(gamer == null) return;
		if(event.getInventory().getTitle().equals("Refill")) {
			Refill refill = RefillManager.getInstance().get(player);
			if(refill == null) return;
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§aConfirmar")) {
				player.getInventory().clear();
				player.closeInventory();
				refill.setStart(true);
				refill.setTime(System.currentTimeMillis());
				refill.prepareInventory();
				sendMessage(player, false, "§aFaça seu refil!");
			} else if(event.getCurrentItem().getType().equals(Material.MUSHROOM_SOUP)) {
				refill.setTypeRefill(refill.getTypeRefill().equals(TypesRefill.LINE) ? TypesRefill.MIXED : TypesRefill.LINE);
				RefillInventorys.getInstance().create(player, refill);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§cCancelar")) {
				RefillManager.getInstance().remove(refill);
				player.closeInventory();
			}
			return;
		} else if(event.getInventory().equals(player.getInventory())) { 
			if(event.getCurrentItem().getType().equals(Material.MUSHROOM_SOUP)) {
				Main.debug("sOPA");
			}
			return;
		}
	}
}
