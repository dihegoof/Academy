package com.academy.arenas;

import java.util.List;

import org.bukkit.Location;

import com.academy.gamer.Gamer;
import com.academy.kit.Kit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Arena {
	
	String name;
	Location location;
	List<Gamer> gamers;
	Kit kit;
	
	public Arena(String name, Location location, List<Gamer> gamers, Kit kit) {
		this.name = name;
		this.location = location;
		this.gamers = gamers;
		this.kit = kit;
	}
}
