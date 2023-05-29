package com.academy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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
	static boolean debug = false;
	static String[] listeners = { "gamer.listener" };
	
	@Override
	public void onEnable() {
		plugin = this;
		debug("Plugin habilitado!");
		for(String loaderListener : listeners) { 
			ClassGetter.getInstance().events("com.academy." + loaderListener);
		}
	}

	@Override
	public void onDisable() {
		debug("Plugin desabilitado!");
		plugin = null;
	}
	
	public void debug(String... messages) { 
		if(!debug) return;
		for(String message : messages) { 
			Bukkit.getConsoleSender().sendMessage(message);
		}
	}
}
