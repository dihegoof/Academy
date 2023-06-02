package com.academy.arenas.inventorys.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.academy.Main;
import com.academy.abilities.inventorys.AbilitieInventorys;
import com.academy.arenas.Arena;
import com.academy.arenas.ArenaManager;
import com.academy.arenas.inventorys.ArenaInventorys;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.util.Utils;

public class InventoryListener extends Utils implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) { 
		if(event.getClickedInventory() == null) return;
		Player player = (Player) event.getWhoClicked();
		if(event.getInventory().getTitle().equals("Arenas")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				ArenaInventorys.getInstance().listArenas(player, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				ArenaInventorys.getInstance().listArenas(player, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§aSpawn")) {
				Gamer gamer = GamerManager.getInstance().get(player.getName());
				if(gamer == null) { 
					Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
					return;
				}
				player.closeInventory();
				gamer.setSpawn();
				gamer.preparePlayer();
			} else { 
				Gamer gamer = GamerManager.getInstance().get(player.getName());
				if(gamer == null) { 
					Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
					return;
				}
				Arena arena = ArenaManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aArena ", ""));
				if(arena == null) { 
					sendMessage(player, false, "§cEsta arena não foi encontrada!");
					player.closeInventory();
					return;
				}
				if(arena.isAllowAbilities()) { 
					AbilitieInventorys.getInstance().listAbilities(player, arena, 1);
				} else { 
					player.closeInventory();
					arena.prepareGamer(gamer);
					arena.add(gamer);
					sendMessage(player, false, "§aVocê entrou na arena §7" + arena.getName() + "§a!");
				}
			}
			return;
		}
	}
}
