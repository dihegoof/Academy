package com.academy.gamer;

import java.util.UUID;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class Gamer {
	
	UUID uniqueId;
	Player player;
	State stateGamer;	
	boolean online;
	
	public Gamer(UUID uniqueId, Player player, State stateGamer, boolean online) {
		this.uniqueId = uniqueId;
		this.player = player;
		this.stateGamer = stateGamer;
		this.online = online;
	}
}
