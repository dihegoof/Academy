package com.academy.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.academy.Main;
import com.academy.abilities.Abilitie;
import com.academy.abilities.AbilitieManager;
import com.academy.arenas.Arena;
import com.academy.arenas.ArenaManager;
import com.academy.arenas.inventorys.ArenaInventorys;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.kit.Kit;
import com.academy.kit.KitManager;
import com.academy.kit.inventorys.KitInventorys;
import com.academy.util.Base64Encode;
import com.academy.util.ItemName;
import com.academy.util.Utils;

public class Var extends Utils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0) {
				help(label, "none", sender);
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
								itens.add(i + ";" + Base64Encode.getInstance().itemStackToBase64(player.getInventory().getItem(i)));
							}
						}
						List<String> armor = null;
						if(player.getInventory().getArmorContents() != null) {
							armor = new ArrayList<>();
							armor.add("helmet;" + (player.getInventory().getHelmet() != null ? Base64Encode.getInstance().itemStackToBase64(player.getInventory().getHelmet()) : "null"));
							armor.add("chestplate;" + (player.getInventory().getChestplate() != null ? Base64Encode.getInstance().itemStackToBase64(player.getInventory().getChestplate()) : "null"));
							armor.add("leggings;" + (player.getInventory().getLeggings() != null ? Base64Encode.getInstance().itemStackToBase64(player.getInventory().getLeggings()) : "null"));
							armor.add("boots;" + (player.getInventory().getBoots() != null ? Base64Encode.getInstance().itemStackToBase64(player.getInventory().getBoots()) : "null"));
						}
						player.getInventory().clear();
						player.getInventory().setArmorContents(null);
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
				} else if(args.length == 4) { 
					if(args[1].equalsIgnoreCase("setar")) {
						Gamer gamerTarget = GamerManager.getInstance().get(args[2]);
						if(gamerTarget == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
							return true;
						}
						if(gamerTarget.isOnline() || gamerTarget.getPlayer() == null) {
							sendMessage(player, false, "§cEste jogador não foi encontrado!");
							return true;
						}
						Kit kit = KitManager.getInstance().get(args[3]);
						if(kit == null) {
							sendMessage(player, false, "§cNão existe um kit com este nome!");
							return true;
						}
						gamerTarget.getPlayer().getInventory().clear();
						kit.give(gamerTarget.getPlayer());
						sendMessage(gamerTarget.getPlayer(), false, "§aFoi setado à você o kit §7" + kit.getName() + "§a!");
						sendMessage(player, false, "§aVocê setou o kit §7" + kit.getName() + " §apara §7" + gamerTarget.getPlayer().getName() + "§a!");
					}
					return true;
				} else { 
					help(label, "kit", sender);
				}
				break;
			case "arena":
				if(args.length == 3) {
					if(args[1].equalsIgnoreCase("criar")) { 
						Arena arena = ArenaManager.getInstance().get(args[2]);
						if(arena != null)  {
							sendMessage(player, false, "§cJá existe uma arena com este nome!");
							return true;
						}
						arena = new Arena(args[2], Material.STONE, 0, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), player.getLocation().getWorld().getName(), new ArrayList<>(), null, new ArrayList<>());
						ArenaManager.getInstance().add(arena);
						sendMessage(player, false, "§aVocê criou a arena §7" + arena.getName() + "§a!");
					} else if(args[1].equalsIgnoreCase("deletar")) { 
						Arena arena = ArenaManager.getInstance().get(args[2]);
						if(arena == null)  {
							sendMessage(player, false, "§cNão existe uma arena com este nome!");
							return true;
						}
						arena.delete();
						ArenaManager.getInstance().remove(arena);
						sendMessage(player, false, "§cVocê deletou a arena §7" + arena.getName() + "§c!");
					} else if(args[2].equalsIgnoreCase("redefinirkit")) { 
						Arena arena = ArenaManager.getInstance().get(args[1]);
						if(arena == null)  {
							sendMessage(player, false, "§cNão existe uma arena com este nome!");
							return true;
						}
						arena.setKit(null);
						sendMessage(player, false, "§aVocê redefiniu o kit para a arena §7" + arena.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("ir")) { 
						Gamer gamer = GamerManager.getInstance().get(player.getName());
						if(gamer == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
							return true;
						}
						Arena arena = ArenaManager.getInstance().get(args[1]);
						if(arena == null) {
							sendMessage(player, false, "§cNão existe uma arena com este nome!");
							return true;
						}
						if(gamer.outSpawn()) {
							sendMessage(player, false, "§cVocê precisa estar no spawn para ir até uma arena!");
							return true;
						}
						if(gamer.getArena().getName().equals(arena.getName())) { 
							sendMessage(player, false, "§cVocê já está nesta arena!");
							return true;
						}
						arena.prepareGamer(gamer);
						arena.add(gamer);
						sendMessage(player, false, "§aVocê entrou na arena §7" + arena.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("redefinirloc")) { 
						Arena arena = ArenaManager.getInstance().get(args[1]);
						if(arena == null) {
							sendMessage(player, false, "§cNão existe uma arena com este nome!");
							return true;
						}
						arena.setX(player.getLocation().getX());
						arena.setY(player.getLocation().getY());
						arena.setZ(player.getLocation().getZ());
						arena.setYaw(player.getLocation().getYaw());
						arena.setPitch(player.getLocation().getPitch());
						arena.setWorld(player.getLocation().getWorld().getName());
						sendMessage(player, false, "§aVocê redefiniu a localização da arena §7" + arena.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("icone")) { 
						Arena arena = ArenaManager.getInstance().get(args[1]);
						if(arena == null) {
							sendMessage(player, false, "§cNão existe uma arena com este nome!");
							return true;
						}
						if(player.getItemInHand() != null || player.getItemInHand().getType() == Material.AIR) {
							sendMessage(player, false, "§cVocê precisa estar com algum item em mãos!");
							return true;
						}
						arena.setIcon(player.getItemInHand().getType());
						arena.setData(player.getItemInHand().getDurability());
						sendMessage(player, false, "§aVocê definiu o item §7" + ItemName.valueOf(arena.getIcon(), arena.getData()).getName() + " §acomo ícone da arena!");
					}
					return true;
				} else if(args.length == 2) { 
					ArenaInventorys.getInstance().listArenas(player, 1);
					return true;
				} else if(args.length == 4) { 
					Arena arena = ArenaManager.getInstance().get(args[1]);
					if(arena == null)  {
						sendMessage(player, false, "§cNão existe uma arena com este nome!");
						return true;
					}
					if(args[2].equalsIgnoreCase("definirkit")) { 
						Kit kit = KitManager.getInstance().get(args[3]);
						if(kit == null) {
							sendMessage(player, false, "§cNão existe um kit com este nome!");
							return true;
						}
						arena.setKit(kit);
						sendMessage(player, false, "§aVocê definiu o kit §7" + kit.getName() + " §apara a arena §7" + arena.getName() + "§a!");
					} else {
						help(label, "arena", sender);
					}
				} else if(args.length == 5) { 
					Arena arena = ArenaManager.getInstance().get(args[1]);
					if(arena == null)  {
						sendMessage(player, false, "§cNão existe uma arena com este nome!");
						return true;
					}
					if(args[2].equalsIgnoreCase("habilidade")) { 
						Abilitie abilitie = AbilitieManager.getInstance().get(args[4]);
						if(abilitie == null) {
							sendMessage(player, false, "§cNão existe uma habilidade com este nome!");
							return true;
						}
						List<Abilitie> abilities = arena.getAbilities();
						if(args[3].equalsIgnoreCase("add")) {
							if(abilities.contains(abilitie)) { 
								sendMessage(player, false, "§cEsta arena já possui esta habilidade!");
								return true;
							}
							abilities.add(abilitie);
							arena.setAbilities(abilities);
							sendMessage(player, false, "§aVocê adicionou a habilidade §7" + abilitie.getName() + " §aà arena §7" + arena.getName() + "§a!");
						} else if(args[3].equalsIgnoreCase("remover")) { 
							if(!abilities.contains(abilitie)) { 
								sendMessage(player, false, "§cEsta arena não possui esta habilidade!");
								return true;
							}
							abilities.remove(abilitie);
							arena.setAbilities(abilities);
							sendMessage(player, false, "§aVocê removeu a habilidade §7" + abilitie.getName() + " §ada arena §7" + arena.getName() + "§a!");
						}
					}
				} else { 
					help(label, "arena", sender);
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
	
	void help(String label, String session, CommandSender sender) {
		if(session.equalsIgnoreCase("kit")) { 
			sintaxCommand(sender, 
					"§c/" + label + " kit <criar, deletar> <nome>",
					"§c/" + label + " kit lista",
					"§c/" + label + " kit setar <jogador> <nome>");
		} else if(session.equalsIgnoreCase("arena")) {
			sintaxCommand(sender, 
					"§c/" + label + " arena <criar, deletar> <nome>",
					"§c/" + label + " arena lista",
					"§c/" + label + " arena <nome da arena> definirkit <nome do kit>",
					"§c/" + label + " arena <nome da arena> redefinirkit",
					"§c/" + label + " arena <nome da arena> ir",
					"§c/" + label + " arena <nome da arena> redefinirloc",
					"§c/" + label + " arena <nome da arena> icone", 
					"§c/" + label + " arena <nome da arena> habilidade <add, remover> <nome>");
		} else { 
			sintaxCommand(sender, "§c/" + label + " kit - Gerenciamento dos Kits",
								  "§c/" + label + " arena - Gerenciamento de Arenas");
		}
	}
	
	//var kit <criar, deletar> <nome>
}
