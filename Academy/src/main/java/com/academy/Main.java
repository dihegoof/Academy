package com.academy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.academy.abilities.AbilitieManager;
import com.academy.arenas.ArenaManager;
import com.academy.kit.KitManager;
import com.academy.util.Config;
import com.academy.util.ItemBuilder;

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
	static String[] listeners = { 
			"gamer.listener", 
			"kit.inventorys.listener", 
			"arenas.inventorys.listener", 
			"abilities.inventorys.listener", 
			"abilities.types", 
			"arenas.listener",
			"arenas.damager.inventorys.listener"};
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
		createSoups();
	}

	@Override
	public void onDisable() {
		KitManager.getInstance().save();
		ArenaManager.getInstance().save();
		AbilitieManager.getInstance().save();
		debug("Plugin desabilitado!");
		plugin = null;
	}
	
	public static void debug(String... messages) { 
		if(!debug) return;
		for(String message : messages) { 
			Bukkit.getConsoleSender().sendMessage(message);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createSoups() {
		ItemBuilder soup = new ItemBuilder(Material.MUSHROOM_SOUP);
		ShapelessRecipe cocoa = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe cactus = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe pumpkin = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe melon = new ShapelessRecipe(soup.getStack());

		cocoa.addIngredient(Material.INK_SACK, 3);
		cocoa.addIngredient(Material.BOWL);
		
		cactus.addIngredient(Material.CACTUS);
		cactus.addIngredient(Material.BOWL);
		
		pumpkin.addIngredient(Material.PUMPKIN_SEEDS);
		pumpkin.addIngredient(Material.BOWL);
		
		melon.addIngredient(Material.MELON_SEEDS);
		melon.addIngredient(Material.BOWL);

		Bukkit.addRecipe(cocoa);
		Bukkit.addRecipe(cactus);
		Bukkit.addRecipe(pumpkin);
		Bukkit.addRecipe(melon);
	}

}
