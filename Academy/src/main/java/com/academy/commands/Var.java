package com.academy.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.academy.Main;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.kit.Kit;
import com.academy.kit.KitManager;
import com.academy.kit.inventorys.KitInventorys;
import com.academy.util.Serialise;
import com.academy.util.Utils;

public class Var extends Utils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0) {
				help(label, sender);
				return true;
			}
			switch(args[0]) {
			case "kit":
				if(args.length == 3) { 
					if(args[1].equalsIgnoreCase("criar")) {
						Gamer gamer = GamerManager.getInstance().get(player.getName());
						if(gamer == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
							return true;
						}
						if(gamer.inventoryEmpty()) { 
							sendMessage(player, false, "§cSeu inventário está vazio!");
							return true;
						}
						Kit kit = KitManager.getInstance().get(args[2]);
						if(kit != null) {
							sendMessage(player, false, "§cJá existe um kit com este nome!");
							return true;
						}
						List<String> itens = new ArrayList<>();
						for(int i = 0; i < player.getInventory().getSize(); i++) {
							if(player.getInventory().getItem(i) != null) {
								itens.add(Serialise.getInstance().serialise(player.getInventory().getItem(i), i, false, player));
							}
						}
						List<String> armor = null;
						if(player.getInventory().getArmorContents() != null) {
							armor = new ArrayList<>();
							for(ItemStack it : player.getInventory().getArmorContents()) { 
								armor.add(Serialise.getInstance().serialise(it, 0, true, player));
							}
						}
						player.getInventory().clear();
						kit = new Kit(args[2], itens, armor);
						KitManager.getInstance().add(kit);
						sendMessage(player, false, "§aVocê criou o kit §7" + kit.getName() + "§a!");
					} else if(args[1].equalsIgnoreCase("deletar")) { 
						Kit kit = KitManager.getInstance().get(args[2]);
						if(kit == null) {
							sendMessage(player, false, "§cNão existe um kit com este nome!");
							return true;
						}
						kit.delete();
						KitManager.getInstance().remove(kit);
						sendMessage(player, false, "§cVocê deletou o kit §7" + args[2] + "§c!");
					}
					return true;
				} else if(args.length == 2) { 
					if(args[1].equalsIgnoreCase("lista")) {
						KitInventorys.getInstance().listKits(player, 1);
					}
					return true;
				} else { 
					help(label, sender);
				}
				break;
			default:
				sendMessage(player, false, "§cEste argumento não foi encontrado!");
				break;
			}
		} else { 
			sendMessage(sender, false, "§cVocê precisa ser um jogador para digitar este comando!");
		}
		return false;
	}
	
	void help(String label, CommandSender sender) { 
		sintaxCommand(sender, 
				"§c/" + label + " kit <criar, deletar> <nome>",
				"§c/" + label + " kit lista");
	}
	
	//var kit <criar, deletar> <nome>
}
