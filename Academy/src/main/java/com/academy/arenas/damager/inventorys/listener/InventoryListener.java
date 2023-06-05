package com.academy.arenas.damager.inventorys.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.academy.arenas.damager.DamageManager;
import com.academy.arenas.damager.Damager;
import com.academy.arenas.damager.inventorys.DamageInventorys;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.kit.KitManager;
import com.academy.util.Utils;

public class InventoryListener extends Utils implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) { 
		if(event.getClickedInventory() == null) return;
		Player player = (Player) event.getWhoClicked();
		Gamer gamer = GamerManager.getInstance().get(player.getName());
		if(gamer == null) return;
		if(event.getInventory().getTitle().equals("Personalização")) {
			Damager damager = DamageManager.getInstance().get(player);
			if(damager == null) return;
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§aDano")) {
				damager.setDamage(damager.getDamage() <= 7 ? (damager.getDamage() + 1.0D) : 2.0D);
				DamageInventorys.getInstance().create(player, damager);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§aSelecione o kit")) {
				DamageInventorys.getInstance().listKits(player, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§aTreinar invisível?")) {
				damager.setHidden(damager.isHidden() ? false : true);
				player.closeInventory();
				DamageInventorys.getInstance().create(player, damager);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§aConfirmar")) {
				if(damager.getKit() == null) {
					sendMessage(player, false, "§cVocê precisa selecionar um kit!");
					return;
				}
				gamer.setDamager(true);
				gamer.prepareChallenge(damager);
				damager.setStart(true);
				damager.setTime(System.currentTimeMillis());
				damager.start();
				player.closeInventory();
				sendMessage(player, false, "§aVocê iniciou seu desafio de resistência, boa sorte!");
				if(damager.isHidden()) { 
					for(Player pl : Bukkit.getOnlinePlayers()) { 
						player.hidePlayer(pl);
					}
				}
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§aCancelar")) {
				DamageManager.getInstance().remove(damager);
				player.closeInventory();
			} else {
				DamageInventorys.getInstance().listKits(player, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equals("Kits para Resistência")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			Damager damager = DamageManager.getInstance().get(player);
			if(damager == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				DamageInventorys.getInstance().listKits(player, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) {
				int pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				DamageInventorys.getInstance().listKits(player, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§cVoltar")) {
				DamageInventorys.getInstance().create(player, damager);
			} else { 
				damager.setKit(KitManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aKit §7", "")));
				DamageInventorys.getInstance().create(player, damager);
			}
			return;
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) { 
		
	}
}
