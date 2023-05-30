package com.academy.kit;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kit {
	
	String name;
	List<String> itens, armor;
	
	public Kit(String name, List<String> itens, List<String> armor) {
		this.name = name;
		this.itens = itens;
		this.armor = armor;
	}
}
