package com.academy.abilities.inventorys.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.academy.abilities.Abilitie;
import com.academy.abilities.AbilitieManager;
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
		if(event.getInventory().getTitle().startsWith("Habilidades para ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			Arena arena = ArenaManager.getInstance().get(event.getInventory().getTitle().replace("Habilidades para ", ""));
			if(arena == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				AbilitieInventorys.getInstance().listAbilities(player, arena, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				AbilitieInventorys.getInstance().listAbilities(player, arena, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§cCancelar")) {
				ArenaInventorys.getInstance().listArenas(player, 1);
			} else { 
				Gamer gamer = GamerManager.getInstance().get(player.getName());
				if(gamer == null) return;
				Abilitie abilitie = AbilitieManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
				if(abilitie == null) return;
				player.closeInventory();
				gamer.setAbilitie(abilitie);
				arena.prepareGamer(gamer);
				arena.add(gamer);
				sendMessage(player, false, "§aVocê selecionou a habilidade §7" + abilitie.getName() + " §ana arena §7" + arena.getName() + "§a!");
			}
			return;
		}
	}
}
