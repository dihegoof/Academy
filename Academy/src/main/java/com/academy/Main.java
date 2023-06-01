package com.academy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.academy.abilities.AbilitieManager;
import com.academy.arenas.ArenaManager;
import com.academy.kit.KitManager;
import com.academy.util.Config;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Dihego
 */

public class Main extends JavaPlugin {
	
	@Getter 
	@Setter
	static Plugin plugin;
	static boolean debug = true;
	static String[] listeners = { "gamer.listener", "kit.inventorys.listener", "arenas.inventorys.listener" };
	static String[] commands = { "commands" };
	
	@Override
	public void onEnable() {
		plugin = this;
		debug("Plugin habilitado!");
		for(String loaderListener : listeners) { 
			ClassGetter.getInstance().events("com.academy." + loaderListener);
		}
		for(String loaderCommands : commands) { 
			ClassGetter.getInstance().commands("com.academy." + loaderCommands);
		}
		Config.getInstance().create();
		KitManager.getInstance().load();
		ArenaManager.getInstance().load();
		AbilitieManager.getInstance().load();
	}

	@Override
	public void onDisable() {
		KitManager.getInstance().save();
		ArenaManager.getInstance().save();
		debug("Plugin desabilitado!");
		plugin = null;
	}
	
	public static void debug(String... messages) { 
		if(!debug) return;
		for(String message : messages) { 
			Bukkit.getConsoleSender().sendMessage(message);
		}
	}
}
