package com.academy.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.academy.Main;
import com.academy.abilities.Abilitie;
import com.academy.abilities.AbilitieManager;
import com.academy.abilities.inventorys.AbilitieInventorys;
import com.academy.arenas.Arena;
import com.academy.arenas.ArenaManager;
import com.academy.arenas.feast.Feast;
import com.academy.arenas.feast.FeastManager;
import com.academy.arenas.inventorys.ArenaInventorys;
import com.academy.gamer.Gamer;
import com.academy.gamer.GamerManager;
import com.academy.gamer.State;
import com.academy.kit.Kit;
import com.academy.kit.KitManager;
import com.academy.kit.Type;
import com.academy.kit.inventorys.KitInventorys;
import com.academy.minigames.damager.DamageManager;
import com.academy.minigames.damager.Damager;
import com.academy.minigames.damager.inventorys.DamageInventorys;
import com.academy.minigames.refill.Refill;
import com.academy.minigames.refill.RefillManager;
import com.academy.minigames.refill.TypesRefill;
import com.academy.minigames.refill.inventorys.RefillInventorys;
import com.academy.util.Base64Encode;
import com.academy.util.ItemName;
import com.academy.util.TimeCount;
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
						kit = new Kit(args[2], itens, armor, Type.NONE, Material.STONE, 0);
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
					} else if(args[2].equalsIgnoreCase("info")) { 
						Kit kit = KitManager.getInstance().get(args[1]);
						if(kit == null) {
							sendMessage(player, false, "§cNão existe um kit com este nome!");
							return true;
						}
						sendMessage(player, true, 
								"§aKit §7" + kit.getName(),
								"§aTipo: §7" + (kit.getType().equals(Type.NONE) ? "Nenhum" : (kit.getType().equals(Type.COMBAT) ? "Combate" : "Resistência"))
								);
					} else if(args[2].equalsIgnoreCase("tipo")) { 
						Kit kit = KitManager.getInstance().get(args[1]);
						if(kit == null) {
							sendMessage(player, false, "§cNão existe um kit com este nome!");
							return true;
						}
						kit.setType(kit.getType().equals(Type.NONE) ? Type.COMBAT : (kit.getType().equals(Type.COMBAT) ? Type.RESISTANCE : Type.NONE));
						sendMessage(player, false, "§aVocê alterou o kit §7" + kit.getName() + " §apara o tipo §7" + (kit.getType().equals(Type.NONE) ? "Nenhum" : (kit.getType().equals(Type.COMBAT) ? "Combate" : "Resistência")) + "§a!");
					} else if(args[2].equalsIgnoreCase("icone")) { 
						Kit kit = KitManager.getInstance().get(args[1]);
						if(kit == null) {
							sendMessage(player, false, "§cNão existe um kit com este nome!");
							return true;
						}
						if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
							sendMessage(player, false, "§cVocê precisa estar com algum item em mãos!");
							return true;
						}
						kit.setIcon(player.getItemInHand().getType());
						kit.setData(player.getItemInHand().getDurability());
						sendMessage(player, false, "§aVocê definiu o item §7" + ItemName.valueOf(kit.getIcon(), kit.getData()).getName() + " §acomo ícone do kit §7" + kit.getName() + "§a!");
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
					Arena arena = ArenaManager.getInstance().get(args[1]);
					if(arena == null) {
						sendMessage(player, false, "§cNão existe uma arena com este nome!");
						return true;
					}
					if(args[1].equalsIgnoreCase("criar")) { 
						arena = new Arena(args[2], Material.STONE, 0, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), player.getLocation().getWorld().getName(), new ArrayList<>(), null, new ArrayList<>(), false, false, false);
						ArenaManager.getInstance().add(arena);
						sendMessage(player, false, "§aVocê criou a arena §7" + arena.getName() + "§a!");
					} else if(args[1].equalsIgnoreCase("deletar")) { 
						arena.delete();
						ArenaManager.getInstance().remove(arena);
						sendMessage(player, false, "§cVocê deletou a arena §7" + arena.getName() + "§c!");
					} else if(args[2].equalsIgnoreCase("redefinirkit")) { 
						arena.setKit(null);
						sendMessage(player, false, "§aVocê redefiniu o kit para a arena §7" + arena.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("ir")) { 
						Gamer gamer = GamerManager.getInstance().get(player.getName());
						if(gamer == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
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
						arena.setX(player.getLocation().getX());
						arena.setY(player.getLocation().getY());
						arena.setZ(player.getLocation().getZ());
						arena.setYaw(player.getLocation().getYaw());
						arena.setPitch(player.getLocation().getPitch());
						arena.setWorld(player.getLocation().getWorld().getName());
						sendMessage(player, false, "§aVocê redefiniu a localização da arena §7" + arena.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("icone")) { 
						if(player.getItemInHand() != null || player.getItemInHand().getType() == Material.AIR) {
							sendMessage(player, false, "§cVocê precisa estar com algum item em mãos!");
							return true;
						}
						arena.setIcon(player.getItemInHand().getType());
						arena.setData(player.getItemInHand().getDurability());
						sendMessage(player, false, "§aVocê definiu o item §7" + ItemName.valueOf(arena.getIcon(), arena.getData()).getName() + " §acomo ícone da arena §7" + arena.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("feast")) { 
						arena.setAllowFeast((arena.isAllowFeast() ? false : true));
						sendMessage(player, false, "§aVocê " + (arena.isAllowFeast() ? "ativou" : "desativou") + " o feast nesta arena!");
						if(!arena.isAllowFeast() && arena.getFeast() != null) { 
							arena.setFeast(null);
						}
					} else if(args[2].equalsIgnoreCase("habilidades")) { 
						arena.setAllowAbilities((arena.isAllowAbilities() ? false : true));
						sendMessage(player, false, "§aVocê " + (arena.isAllowAbilities() ? "ativou" : "desativou") + " as habilidades nesta arena!");
						if(!arena.isAllowAbilities() && arena.getAbilities().size() > 0) { 
							arena.setAbilities(new ArrayList<>());
						}
					} else if(args[2].equalsIgnoreCase("construcao")) { 
						arena.setBuild((arena.isBuild() ? false : true));
						sendMessage(player, false, "§aVocê " + (arena.isBuild() ? "ativou" : "desativou") + " as construções nesta arena!");
						if(arena.isBuild()) { 
							arena.setBlocks(new ArrayList<>());
							arena.startClear();
						}
					} else if(args[2].equalsIgnoreCase("feastcriar")) { 
						if(!FeastManager.getInstance().getSelectPos().containsKey(player)) { 
							sendMessage(player, false, "§cVocê deve selecionar alguns baús primeiro!");
							return true;
						}
						if(FeastManager.getInstance().getSelectPos().get(player).size() == 0) {
							sendMessage(player, false, "§cVocê deve selecionar alguns baús primeiro!");
							return true;
						}
						if(!arena.isAllowFeast()) { 
							sendMessage(player, false, "§cNesta arena não é permitido um feast!");
							return true;
						}
						List<ItemStack> itens = new ArrayList<>();
						for(Location lo : FeastManager.getInstance().getSelectPos().get(player)) { 
							Chest chest = (Chest) lo.getBlock().getState();
							if(inventoryEmpty(chest.getInventory())) { 
								sendMessage(player, false, "§cAlgum dos baús selecionados está vazio!");
								return true;
							}
							for(ItemStack it : chest.getInventory().getContents()) { 
								if(it != null && !it.getType().equals(Material.AIR)) { 
									itens.add(it);
								}
							}
						}
						Feast feast = new Feast(arena, FeastManager.getInstance().getSelectPos().get(player), itens, 301, 301, false, true);
						FeastManager.getInstance().add(feast);
						FeastManager.getInstance().getSelectPos().remove(player);
						sendMessage(player, false, "§aFeast adicionado à arena §7" + arena.getName() + "§a!");
						arena.setFeast(feast);
						feast.start();
					 } else if(args[2].equalsIgnoreCase("info")) { 
							sendMessage(player, true, 
									"§aArena §7" + arena.getName(),
									"§aFeast? §7" + (arena.isAllowFeast() ? "Sim" : "Não"),
									"   §8Feast configurado? " + (arena.getFeast() != null ? "Sim" : "Não"),
									"§aHabilidades? §7" + (arena.isAllowAbilities() ? "Sim" : "Não"),
									"§aKit: §7" + (arena.getKit() != null ? arena.getKit().getName() : "Nenhum")
									);
					 }
					return true;
				} else if(args.length == 2) { 
					if(args[1].equalsIgnoreCase("feastpos")) { 
						Gamer gamer = GamerManager.getInstance().get(player.getName());
						if(gamer == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
							return true;
						}
						if(!FeastManager.getInstance().getSelectPos().containsKey(gamer.getPlayer())) { 
							FeastManager.getInstance().getSelectPos().put(gamer.getPlayer(), new ArrayList<>());
							sendMessage(player, false, "§aVocê deve selecionar os baús que compõem o feast de alguma arena!");
						} else { 
							FeastManager.getInstance().getSelectPos().remove(gamer.getPlayer());
							sendMessage(player, false, "§cVocê saiu da seleção de baús que compõem o feast da alguma arena!");
						}
					} else if(args[1].equalsIgnoreCase("lista")) { 
						ArenaInventorys.getInstance().listArenas(player, 1);
					}
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
						if(!arena.isAllowAbilities()) { 
							sendMessage(player, false, "§cNesta arena não é permitido habilidades!");
							return true;
						}
						Abilitie abilitie = AbilitieManager.getInstance().get(args[4]);
						if(abilitie == null) {
							sendMessage(player, false, "§cNão existe uma habilidade com este nome!");
							return true;
						}
						List<String> abilities = arena.getAbilities();
						if(args[3].equalsIgnoreCase("add")) {
							if(abilities.contains(abilitie.getName())) { 
								sendMessage(player, false, "§cEsta arena já possui esta habilidade!");
								return true;
							}
							abilities.add(abilitie.getName());
							arena.setAbilities(abilities);
							sendMessage(player, false, "§aVocê adicionou a habilidade §7" + abilitie.getName() + " §aà arena §7" + arena.getName() + "§a!");
						} else if(args[3].equalsIgnoreCase("remover")) { 
							if(!abilities.contains(abilitie.getName())) { 
								sendMessage(player, false, "§cEsta arena não possui esta habilidade!");
								return true;
							}
							abilities.remove(abilitie.getName());
							arena.setAbilities(abilities);
							sendMessage(player, false, "§aVocê removeu a habilidade §7" + abilitie.getName() + " §ada arena §7" + arena.getName() + "§a!");
						}
					}
				} else { 
					help(label, "arena", sender);
				}
				break;
			case "habilidade":
				if(args.length == 2) { 
					if(args[1].equalsIgnoreCase("lista")) { 
						AbilitieInventorys.getInstance().listAbilities(player, null, 0, false);
					} else if(args[1].equalsIgnoreCase("cu")) { 
						for(Feast fe : FeastManager.getInstance().getFeast()) { 
							Main.debug("Arena: " + fe.getArena().getName());
						}
					}
					return true;
				} else if(args.length == 3) {
					Abilitie abilitie = AbilitieManager.getInstance().get(args[1]);
					if(abilitie == null) {
						sendMessage(player, false, "§cNão existe uma habilidade com este nome!");
						return true;
					}
					if(args[2].equalsIgnoreCase("gratis")) { 
						abilitie.setFree((abilitie.isFree() ? false : true));
						sendMessage(player, false, "§aVocê deixou a habilidade §7" + abilitie.getName() + " §acomo " + (abilitie.isFree() ? "grátis" : "não grátis") + "!");
					} else if(args[2].equalsIgnoreCase("icone")) { 
						if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
							sendMessage(player, false, "§cVocê precisa estar com algum item em mãos!");
							return true;
						}
						abilitie.setIcon(player.getItemInHand().getType());
						abilitie.setDataIcon(player.getItemInHand().getDurability());
						sendMessage(player, false, "§aVocê definiu o item §7" + ItemName.valueOf(abilitie.getIcon(), abilitie.getDataIcon()).getName() + " §acomo ícone da habilidade §7" + abilitie.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("item")) { 
						if(player.getItemInHand() != null || player.getItemInHand().getType() == Material.AIR) {
							sendMessage(player, false, "§cVocê precisa estar com algum item em mãos!");
							return true;
						}
						abilitie.setItem(player.getItemInHand().getType());
						abilitie.setDataItem(player.getItemInHand().getDurability());
						sendMessage(player, false, "§aVocê definiu o item §7" + ItemName.valueOf(abilitie.getIcon(), abilitie.getDataIcon()).getName() + " §acomo item da habilidade §7" + abilitie.getName() + "§a!");
					} else if(args[2].equalsIgnoreCase("info")) {
						sendMessage(player, true, 
								"§aHabilidade §7" + abilitie.getName(),
								"§aGrátis? §7" + (abilitie.isFree() ? "Sim" : "Não"),
								"§aPreço: §7" + formatMoney(abilitie.getPrice()),
								"§aCooldown: §7" + abilitie.getCooldown());
					}
					return true;
				} else if(args.length == 4) { 
					Abilitie abilitie = AbilitieManager.getInstance().get(args[1]);
					if(abilitie == null) {
						sendMessage(player, false, "§cNão existe uma habilidade com este nome!");
						return true;
					}
					if(args[2].equalsIgnoreCase("preco")) { 
						double price = 0.0D;
						try {
							price = Double.parseDouble(args[3]);
						} catch (Exception e) {
							sendMessage(player, false, "§cFormato incorreto!");
							return true;
						}
						if(price == 0.0D) {
							sendMessage(player, false, "§cVocê precisa definir um valor maior que 0!");
							return true;
						}
						abilitie.setPrice(price);
						sendMessage(player, false, "§aVocê alterou o preço da habilidade §7" + abilitie.getName() + " §apara §7" + formatMoney(price) + "§a!");
					} else if(args[2].equalsIgnoreCase("tempo")) { 
						long time = TimeCount.getInstance().getTime(args[3]);
						if(time <= 0) {
							sendMessage(player, false, "§cFormato incorreto!");
							return true;
						}
						abilitie.setCooldown(args[3]);
						sendMessage(player, false, "§aVocê alterou o tempo de espera da habilidade §7" + abilitie.getName() + " §apara §7" + args[3] + "§a!");
					}
					return true;
				} else { 
					help(label, "habilidade", sender);
				}
				break;
			case "minijogos":
				if(args.length == 2) { 
					if(args[1].equalsIgnoreCase("resistencia")) { 
						Gamer gamer = GamerManager.getInstance().get(player.getName());
						if(gamer == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
							return true;
						}
						Damager damager = new Damager(player, 1.0, null, null, 0, false, false);
						DamageManager.getInstance().add(damager);
						DamageInventorys.getInstance().create(player, damager);
					} else if(args[1].equalsIgnoreCase("refill")) { 
						Gamer gamer = GamerManager.getInstance().get(player.getName());
						if(gamer == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
							return true;
						}
						Refill refill = RefillManager.getInstance().get(player);
						if(refill == null) { 
							refill = new Refill(player, TypesRefill.LINE, 0, false);
							RefillManager.getInstance().add(refill);
						}
						RefillInventorys.getInstance().create(player, refill);
					}
					return true;
				} else { 
					help(label, "minijogos", sender);
				}
				break;
			case "moderacao":
				if(args.length == 2) { 
					if(args[1].equalsIgnoreCase("admin")) {
						Gamer gamer = GamerManager.getInstance().get(player.getName());
						if(gamer == null) {
							Main.debug("Gamer nulo (" + getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
							return true;
						}
						if(gamer.isAdmin()) { 
							gamer.getPlayer().setGameMode(GameMode.SURVIVAL);
							gamer.setStateGamer(State.PLAYER);
							for(Player pl : Bukkit.getOnlinePlayers()) { 
								pl.showPlayer(gamer.getPlayer()); 
							}
							for(Player pl : Bukkit.getOnlinePlayers()) { 
								if(GamerManager.getInstance().get(pl.getName()).isAdmin()) { 
									gamer.getPlayer().hidePlayer(pl);
								}
							}
						} else { 
							gamer.getPlayer().setGameMode(GameMode.CREATIVE);
							gamer.setStateGamer(State.ADMIN);
							for(Player pl : Bukkit.getOnlinePlayers()) { 
								pl.getPlayer().hidePlayer(gamer.getPlayer()); 
							}
						}
						sendMessage(player, false, "§aVocê " + (gamer.isAdmin() ? "entrou no" : "saiu do") + " modo admin!");
					}
					return true;
				} else if(args.length == 3) { 
					if(args[1].equalsIgnoreCase("tp")) { 
						Player target = Bukkit.getPlayerExact(args[2]);
						if(target == null) {
							sendMessage(player, false, "§cEste jogador não está online!");
							return true;
						}
						player.teleport(target);
						sendMessage(player, false, "§aTeleportado até §7" + target.getName() + "§a!");
					} else if(args[1].equalsIgnoreCase("gamemode")) { 
						int mode = 0;
						try {
							mode = Integer.parseInt(args[2]);
						} catch (Exception e) {
							sendMessage(player, false, "§cFormato incorreto!");
							return true;
						}
						if(mode > 1) { 
							sendMessage(player, false, "§cFormato incorreto!");
							return true;
						}
						player.setGameMode(mode == 0 ? GameMode.SURVIVAL : GameMode.CREATIVE);
						sendMessage(player, false, "§aVocê alterou seu modo de jogo para §7" + player.getGameMode().toString().toLowerCase() + "§a!");
					}
					return true;
				} else if(args.length == 4) { 
					if(args[1].equalsIgnoreCase("tp")) { 
						Player target1 = Bukkit.getPlayerExact(args[2]);
						if(target1 == null) {
							sendMessage(player, false, "§cO jogador §7" + args[2] + " §cnão está online!");
							return true;
						}
						Player target2 = Bukkit.getPlayerExact(args[3]);
						if(target2 == null) {
							sendMessage(player, false, "§cO jogador §7" + args[3] + " §cnão está online!");
							return true;
						}
						target1.teleport(target2);
						sendMessage(target1, false, "§aTeleportado até §7" + target2.getName() + "§a!");
						sendMessage(player, false, "§aTeleportado §7" + target1.getName() + " §aaté §7" + target2.getName() + "§a!");
					}
					return true;
				} else if(args.length == 5) { 
					if(args[1].equalsIgnoreCase("tp")) { 
						double x, y, z = 0.0;
						try {
							x = Double.parseDouble(args[2]);
							y = Double.parseDouble(args[3]);
							z = Double.parseDouble(args[4]);
						} catch (Exception e) {
							sendMessage(player, false, "§cFormato incorreto!");
							return true;
						}
						player.teleport(new Location(player.getWorld(), x, y, z));
						sendMessage(player, false, "§aTeleportado até §7" + x + "§a, §7" + y + "§a, §7" + z + "§a!");
					}
					return true;
				} else { 
					help(label, "moderacao", sender);
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
					"§c/" + label + " kit setar <jogador> <nome>",
					"§c/" + label + " kit <nome do kit> info", 
					"§c/" + label + " kit <nome do kit> tipo",
					"§c/" + label + " kit <nome do kit> icone");
		} else if(session.equalsIgnoreCase("arena")) {
			sintaxCommand(sender, 
					"§c/" + label + " arena <criar, deletar> <nome>",
					"§c/" + label + " arena lista",
					"§c/" + label + " arena <nome da arena> definirkit <nome do kit>",
					"§c/" + label + " arena <nome da arena> redefinirkit",
					"§c/" + label + " arena <nome da arena> ir",
					"§c/" + label + " arena <nome da arena> redefinirloc",
					"§c/" + label + " arena <nome da arena> icone", 
					"§c/" + label + " arena <nome da arena> feast",
					"§c/" + label + " arena <nome da arena> info",
					"§c/" + label + " arena feastpos",
					"§c/" + label + " arena <nome da arena> feastcriar",
					"§c/" + label + " arena <nome da arena> construcao",
					"§c/" + label + " arena <nome da arena> habilidades",
					"§c/" + label + " arena <nome da arena> habilidade <add, remover> <nome>");
		} else if(session.equalsIgnoreCase("habilidade")) { 
			sintaxCommand(sender, 
					"§c/" + label + " habilidade lista",
					"§c/" + label + " habilidade <nome da habilidade> gratis", 
					"§c/" + label + " habilidade <nome da habilidade> preco <preço>", 
					"§c/" + label + " habilidade <nome da habilidade> tempo <tempo de espera>", 
					"§c/" + label + " habilidade <nome da habilidade> icone", 
					"§c/" + label + " habilidade <nome da habilidade> item",
					"§c/" + label + " habilidade <nome da habilidade> info"
					);
		} else if(session.equalsIgnoreCase("moderacao")) { 
			sintaxCommand(sender, 
					"§c/" + label + " moderacao admin",
					"§c/" + label + " moderacao tp <jogador>",
					"§c/" + label + " moderacao tp <jogador> <alvo>",
					"§c/" + label + " moderacao tp <x> <y> <z>",
					"§c/" + label + " moderacao gamemode <0, 1>");
		} else if(session.equalsIgnoreCase("minijogos")) { 
			sintaxCommand(sender, 
					"§c/" + label + " minijogos resistencia",
					"§c/" + label + " minijogos refill");
		} else { 
			sintaxCommand(sender, "§c/" + label + " kit - Lista de comandos para gerenciar os kits.",
								  "§c/" + label + " arena - Lista de comandos para gerenciar as arenas. ", 
								  "§c/" + label + " habilidade - Lista de comandos para gerenciar as habilidades.", 
								  "§c/" + label + " minijogos - Lista de comandos para jogar mini-jogos para treino.", 
								  "§c/" + label + " moderacao - Lista de comandos para moderação.");
		}
	}
	
	boolean inventoryEmpty(Inventory inventory) { 
		for(ItemStack is : inventory.getContents()) { 
			if(is != null) { 
				return false;
			}
		}
		return true;
	}
	
	//var kit <criar, deletar> <nome>
}
