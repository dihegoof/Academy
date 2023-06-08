package com.academy.gamer.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.academy.Main;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.gamer.State;
import com.academy.minigames.damager.DamageManager;
import com.academy.util.Utils;

public class GamerListener extends Utils implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) { 
		event.setJoinMessage(null);
		for(int i = 0; i < 120; i++)
			sendMessage(event.getPlayer(), false, "");
		sendMessage(event.getPlayer(), false, "§aSeja bem-vindo ao servidor de treino!");
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) {
			gamer = new Gamer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), event.getPlayer(), State.PLAYER, true, true);
			GamerManager.getInstance().add(gamer);
		} else { 
			gamer.setOnline(true);
			gamer.setPlayer(event.getPlayer());
		}
		gamer.preparePlayer();
		gamer.setSpawn();
		gamer.setInvencible(true);
		gamer.updateTag();
		for(Player pl : Bukkit.getOnlinePlayers()) { 
			if(GamerManager.getInstance().get(pl.getName()).isAdmin()) { 
				gamer.getPlayer().hidePlayer(pl);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) { 
		event.setQuitMessage(null);
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn()) {
			gamer.getArena().remove(gamer);
			gamer.setArena(null);
		}
		gamer.removeOtherConstructors();
		gamer.removeCooldown();
		gamer.setPlayer(null);
		gamer.setOnline(false);
		gamer.removeAbilitie();
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn()) {
			gamer.getArena().remove(gamer);
			gamer.setArena(null);
		}
		gamer.removeOtherConstructors();
		gamer.removeCooldown();
		gamer.setPlayer(null);
		gamer.setOnline(false);
		gamer.removeAbilitie();
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getEntity().getName());
		if(gamer == null) return;
		if(gamer.isInvencible()) {
			if(gamer.outSpawn()) { 
				gamer.setInvencible(event.getDamage() >= 1.0 ? false : true);
				event.setDamage(0.0D);
				sendMessage(gamer.getPlayer(), false, "§7Você perdeu a proteção do spawn!");
				return;
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) { 
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getDamager() instanceof Player)) return;
		Gamer gamer = GamerManager.getInstance().get(event.getEntity().getName());
		if(gamer.isInvencible()) {
			if(gamer.outSpawn()) { 
				gamer.setInvencible(event.getDamage() >= 1.0 ? false : true);
				event.setDamage(0.0D);
				sendMessage(gamer.getPlayer(), false, "§7Você perdeu a proteção do spawn!");
				return;
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) { 
		if(event.getAction().toString().contains("RIGHT")) { 
			if(event.getItem() != null && event.getItem().getType().toString().contains("MUSHROOM")) { 
				if(event.getPlayer().getHealth() == 20.0D && event.getPlayer().getFoodLevel() == 20) return;
				if(event.getPlayer().getHealth() < 20.0D) { 
					double life = event.getPlayer().getHealth() + 7.0D;
					if(life > 20.0D)  
						life = 20.0D;
					event.getPlayer().setHealth(life);
					event.getPlayer().getItemInHand().setType(Material.BOWL);
				} else if(event.getPlayer().getFoodLevel() < 20) { 
					event.getPlayer().setFoodLevel(20);
					event.getPlayer().getItemInHand().setType(Material.BOWL);
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Gamer gamer = GamerManager.getInstance().get(event.getEntity().getName());
		if(gamer == null) return;
		event.setDeathMessage(null);
		String[] phrases = { "morreu para", "foi morto por", "espancado por", "tomou uma surra de", "foi amassado por", "foi conhecer o céu por culpa de" };
		if(!gamer.isDamager()) { 
			if(event.getEntity().getKiller() instanceof Player) { 
				sendBroadcastMessage(true, "§7" + event.getEntity().getName() + " §e" + phrases[new Random().nextInt(phrases.length)] + " §7" + event.getEntity().getKiller().getName());
			} else {
				sendBroadcastMessage(true, "§7" + event.getEntity().getName() + " §emorreu!");
			}
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				event.getEntity().spigot().respawn();
				gamer.preparePlayer();
				gamer.setSpawn();
				gamer.setInvencible(true);
				gamer.removeCooldown();
				gamer.removeAbilitie();
				if(gamer.isDamager()) { 
					sendMessage(gamer.getPlayer(), true, "§aVocê durou §7" + compareSimpleTime(DamageManager.getInstance().get(gamer.getPlayer()).getTime(), System.currentTimeMillis()).replace(".", "") + "§a!");
					if(DamageManager.getInstance().get(gamer.getPlayer()).isHidden()) { 
						for(Player pl : Bukkit.getOnlinePlayers()) { 
							if(!GamerManager.getInstance().get(pl.getName()).isAdmin()) { 
								gamer.getPlayer().showPlayer(pl);
							}
						}
					}
				}
				gamer.removeOtherConstructors();
				gamer.setDamager(gamer.isDamager() ? false : false);
			}
		}.runTaskLater(com.academy.Main.getPlugin(), 5L);
	}
	
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event) {
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn()) {
			gamer.getArena().remove(gamer);
			gamer.setArena(null);
		}
	}
	
	String[] names = { "SOUP", "MUSH", "BOWL" };
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) { 
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.getWorld("world").playEffect(event.getItemDrop().getLocation(), Effect.SMOKE, 1);
				event.getItemDrop().remove();
			}
		}.runTaskLater(Main.getPlugin(), 20L * 5);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		for(String name : names) { 
			if(gamer.isAdmin()) return;
			if(!event.getItem().getType().toString().contains(name)) { 
				event.setCancelled(true);
			}
		}
	}
}
