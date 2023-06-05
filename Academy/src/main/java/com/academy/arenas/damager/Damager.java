package com.academy.arenas.damager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.academy.Main;
import com.academy.kit.Kit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Damager {
	
	Player player;
	double damage;
	BukkitTask bukkitTask;
	Kit kit;
	long time;
	boolean hidden, start;
	
	public Damager(Player player, double damage, BukkitTask bukkitTask, Kit kit, long time, boolean hidden, boolean start) {
		this.player = player;
		this.damage = damage;
		this.bukkitTask = bukkitTask;
		this.kit = kit;
		this.time = time;
		this.hidden = hidden;
		this.start = start;
	}

	public void start() {
		bukkitTask = new BukkitRunnable() {
			
			@Override
			public void run() {
				getPlayer().damage(getDamage() * 2);
			}
		}.runTaskTimer(Main.getPlugin(), 20L, 20L);
	}
	
	public void cancel() { 
		if(getBukkitTask() != null) 
			getBukkitTask().cancel();
	}
}
