package com.academy.arenas.feast;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.academy.Main;
import com.academy.arenas.Arena;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.util.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feast extends Utils {
	
	Arena arena;
	List<Location> chests;
	List<ItemStack> itens;
	int timeRestart, timeCount;
	boolean spawned, enable;
	
	public Feast(Arena arena, List<Location> chests, List<ItemStack> itens, int timeRestart, int timeCount, boolean spawned, boolean enable) {
		this.arena = arena;
		this.chests = chests;
		this.itens = itens;
		this.timeRestart = timeRestart;
		this.timeCount = timeCount;
		this.spawned = spawned;
		this.enable = enable;
	}
	
	public void start() { 
		if(isEnable()) { 
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if(!isSpawned()) { 
						if(timeCount % 30 == 0 && timeCount != 0) {
							for(Gamer ga : GamerManager.getInstance().getGamers()) { 
								if(ga.hasArena(getArena().getName())) { 
									sendMessage(ga.getPlayer(), false, "§eO feast será abastecido com itens valiosos em §7" + formatTime(timeCount) + "§a!");
								}
							}
						} else if(timeCount == 0) { 
							for(Gamer ga : GamerManager.getInstance().getGamers()) { 
								if(ga.hasArena(getArena().getName())) { 
									sendMessage(ga.getPlayer(), true, "§eO feast foi abastecido, aproveite!");
								}
							}
							spawnChests();
						} else if(timeCount == -15) { 
							setSpawned(true);
						}
						timeCount--;
					} else { 
						timeCount = timeRestart;
						setSpawned(false);
						removeChests();
					}
				}
			}.runTaskTimer(Main.getPlugin(), 20L, 20L);
		}
	}
	
	public void removeChests() { 
		if(getChests().isEmpty()) return;
		for(Location lo : getChests()) { 
			Block block = lo.getBlock();
			if(block == null) continue;
			Chest chest = (Chest) block.getState();
			for(ItemStack it : chest.getInventory()) { 
				it.setType(Material.AIR);
			}
			block.setType(Material.AIR);
		}
	}
	
	public void spawnChests() { 
		if(getChests().isEmpty()) return;
		for(Location lo : getChests()) { 
			if(lo.getBlock() == null || lo.getBlock().getType().equals(Material.AIR)) { 
				lo.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) lo.getBlock().getState();
				fillChests(chest);
			}
		}
	}
	
	public void fillChests(Chest chest) { 
		for(int i = 0; i < getItens().size(); i++) {
			if(new Random().nextInt(100) < new Random().nextInt(50)) {
				chest.getInventory().setItem(new Random().nextInt(27), getItens().get(i));
			}
		}
		chest.update();
	}
}
