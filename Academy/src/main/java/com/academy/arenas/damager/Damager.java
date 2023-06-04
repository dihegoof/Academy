package com.academy.arenas.damager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Damager {
	
	Player player;
	double damage;
	BukkitTask bukkitTask;

}
