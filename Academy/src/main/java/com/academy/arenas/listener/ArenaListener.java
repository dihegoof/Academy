package com.academy.arenas.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.academy.arenas.feast.FeastManager;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.util.Utils;

public class ArenaListener extends Utils implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(FeastManager.getInstance().getSelectPos().containsKey(event.getPlayer())) { 
			List<Location> chests = FeastManager.getInstance().getSelectPos().get(event.getPlayer());
			if(event.getClickedBlock() != null) { 
				Block block = event.getClickedBlock();
				if(block.getType().equals(Material.CHEST)) {
					if(!chests.contains(block.getLocation())) { 
						chests.add(block.getLocation());
						sendMessage(event.getPlayer(), false, "§aBaú adicionado com sucesso!");
					} else { 
						sendMessage(event.getPlayer(), false, "§cEste baú já foi adicionado!");
					}
				} else { 
					sendMessage(event.getPlayer(), false, "§cO bloco interagido deve ser um baú!");
				}
			} else { 
				sendMessage(event.getPlayer(), false, "§cVocê deve interagir com algum bloco!");
			}
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) { 
		if(FeastManager.getInstance().getSelectPos().containsKey(event.getPlayer())) 
			FeastManager.getInstance().getSelectPos().remove(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) { 
		if(FeastManager.getInstance().getSelectPos().containsKey(event.getPlayer())) 
			FeastManager.getInstance().getSelectPos().remove(event.getPlayer());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn() && gamer.getArena().isBuild()) { 
			event.setCancelled(false);
			List<Block> blocks = gamer.getArena().getBlocks();
			blocks.add(event.getBlockPlaced());
			gamer.getArena().setBlocks(blocks);
			return;
		}
		event.setCancelled(gamer.isAdmin() ? false : true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { 
		Gamer gamer = GamerManager.getInstance().get(event.getPlayer().getName());
		if(gamer == null) return;
		if(gamer.outSpawn() && gamer.getArena().isBuild() && gamer.getArena().getBlocks().contains(event.getBlock())) { 
			event.setCancelled(false);
			return;
		}
		event.setCancelled(gamer.isAdmin() ? false : true);
	}
	
}
