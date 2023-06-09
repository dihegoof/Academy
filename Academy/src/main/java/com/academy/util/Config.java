package com.academy.util;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.academy.Main;

import lombok.Getter;

public class Config {
	
	@Getter
	static Config instance = new Config();
	static FileConfiguration kits, arenas, abilities;
	
	public void create() {
		File kit = new File(Main.getPlugin().getDataFolder(), "kits.yml");
		if(!kit.exists()) {
			try {
				kit.createNewFile();
				Main.debug("Arquivo " + kit.getName() + " criado!");
			} catch (Exception e) {
				Main.debug("Erro ao criar o arquivo " + kit.getName() + "!");
			}
		}
		kits = YamlConfiguration.loadConfiguration(kit);
		File arena = new File(Main.getPlugin().getDataFolder(), "arenas.yml");
		if(!arena.exists()) {
			try {
				arena.createNewFile();
				Main.debug("Arquivo " + arena.getName() + " criado!");
			} catch (Exception e) {
				Main.debug("Erro ao criar o arquivo " + arena.getName() + "!");
			}
		}
		arenas = YamlConfiguration.loadConfiguration(arena);
		File abilitie = new File(Main.getPlugin().getDataFolder(), "abilities.yml");
		if(!abilitie.exists()) {
			try {
				abilitie.createNewFile();
				Main.debug("Arquivo " + abilitie.getName() + " criado!");
			} catch (Exception e) {
				Main.debug("Erro ao criar o arquivo " + abilitie.getName() + "!");
			}
		}
		abilities = YamlConfiguration.loadConfiguration(abilitie);
	}
	
	public void save(FileConfiguration fileConfig, String name) {
		File file = new File(Main.getPlugin().getDataFolder(), name + ".yml");
		try {
			fileConfig.save(file);
			Main.debug("Arquivo " + name + ".yml salvo!");
		} catch (Exception e) {
			Main.debug("Erro ao salvar o arquivo " + file.getName() + "!");
		}
	}
	
	public FileConfiguration getKits() {
		return kits;
	}
	
	public FileConfiguration getArenas() {
		return arenas;
	}
	
	public FileConfiguration getAbilities() {
		return abilities;
	}
}
