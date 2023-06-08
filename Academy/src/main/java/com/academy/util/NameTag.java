package com.academy.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.academy.Main;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;

public class NameTag implements Listener {

	public static final HashSet<TeamInfo> teams = new HashSet<TeamInfo>();
	private static int count;

	/**
	 * Used to define name tag prefix of a player
	 *
	 * @param playerName
	 *            Player name
	 * @param teamName
	 *            Team name
	 * @param prefix
	 *            Team prefix
	 */
	public static void setPrefix(String playerName, String teamName, String prefix) {
		setNameTag(playerName, teamName, prefix, "");
	}

	/**
	 * Used to define name tag suffix of a player
	 *
	 * @param playerName
	 *            Player name
	 * @param teamName
	 *            Team name
	 * @param suffix
	 *            Team suffix
	 */
	public static void setSuffix(String playerName, String teamName, String suffix) {
		setNameTag(playerName, teamName, "", suffix);
	}

	/**
	 * Used to define name tag prefix of a player
	 *
	 * @param playerName
	 *            Player name
	 * @param prefix
	 *            Team prefix
	 */
	public static void setPrefix(String playerName, String prefix) {
		setNameTag(playerName, "A", prefix, "");
	}

	/**
	 * Used to define name tag suffix of a player
	 *
	 * @param playerName
	 *            Player name
	 * @param suffix
	 *            Team suffix
	 */
	public static void setSuffix(String playerName, String suffix) {
		setNameTag(playerName, "A", "", suffix);
	}

	/**
	 * Used to define name tag of a player
	 *
	 * @param playerName
	 *            Player name
	 * @param teamName
	 *            Team name
	 * @param prefix
	 *            Team prefix
	 * @param suffix
	 *            Team suffix
	 */
	public static void setNameTag(final String playerName, final String teamName, final String prefix, final String suffix) {
		new BukkitRunnable() {
			public void run() {
				removeTag(playerName);

				TeamInfo teamInfo = getTeam(teamName, prefix, suffix);
				if (teamInfo.getPlayers().contains(playerName))
					return;
				teamInfo.getPlayers().add(playerName);

				PacketInfo packetInfo = new PacketInfo(teamInfo.getName(), Collections.singleton(playerName), 3);
				for (Player player : Bukkit.getOnlinePlayers()) {
					packetInfo.sendToPlayer(player);
				}
			}
		}.runTask(Main.getPlugin());
	}

	// Remove player from old team
	public static void removeTag(String playerName) {
		TeamInfo oldTeam = getPlayerTeam(playerName);
		if (oldTeam != null) {
			oldTeam.getPlayers().remove(playerName);
			PacketInfo packetInfo = new PacketInfo(oldTeam.getName(), Collections.singleton(playerName), 4);
			for (Player player : Bukkit.getOnlinePlayers()) {
				packetInfo.sendToPlayer(player);
			}

			checkTeam(oldTeam);
		}
	}

	// Gets or create a team with the arguments
	private static TeamInfo getTeam(String teamName, String prefix, String suffix) {
		for (TeamInfo team : teams) {
			if (team.getPrefix().equals(prefix) && team.getSuffix().equals(suffix)) {
				return team;
			}
		}

		TeamInfo teamInfo = new TeamInfo(teamName + count++);
		teamInfo.setPrefix(prefix);
		teamInfo.setSuffix(suffix);
		teams.add(teamInfo);

		PacketInfo packetInfo = new PacketInfo(teamInfo.getName(), prefix, suffix, teamInfo.getPlayers(), 0);
		for (Player player : Bukkit.getOnlinePlayers()) {
			packetInfo.sendToPlayer(player);
		}

		return teamInfo;
	}

	// Delete the team if doesn't have any player
	private static void checkTeam(TeamInfo teamInfo) {
		if (teamInfo.getPlayers().isEmpty()) {
			PacketInfo packetInfo = new PacketInfo(teamInfo.getName(), null, null, null, 1);
			for (Player player : Bukkit.getOnlinePlayers()) {
				packetInfo.sendToPlayer(player);
			}
			teams.remove(teamInfo);
		}
	}

	// Gets the team of any player
	private static TeamInfo getPlayerTeam(String player) {
		for (TeamInfo team : teams) {
			if (team.getPlayers().contains(player)) {
				return team;
			}
		}
		return null;
	}

	public static void updateTeamsToPlayer(Player player){
		for (TeamInfo teamInfo : teams) {
			PacketInfo packetInfo = new PacketInfo(teamInfo.getName(), teamInfo.getPrefix(), teamInfo.getSuffix(), teamInfo.getPlayers(), 0);
			packetInfo.sendToPlayer(player);
		}
	}
	
	// Listeners

	@EventHandler
	public void join(PlayerJoinEvent event) {
		updateTeamsToPlayer(event.getPlayer());
	}

	@EventHandler
	public void quit(PlayerQuitEvent event) {
		removeTag(event.getPlayer().getName());
	}

	public static class PacketInfo {

		private final PacketPlayOutScoreboardTeam packet;

		public PacketInfo(String name, String prefix, String suffix, Collection<String> players, int updateType) {
			packet = new PacketPlayOutScoreboardTeam();
			ReflectionUtils.setValue("a", packet, name);
			ReflectionUtils.setValue("f", packet, updateType);

			if (updateType == 0 || updateType == 2) {
				ReflectionUtils.setValue("b", packet, name);
				ReflectionUtils.setValue("c", packet, prefix);
				ReflectionUtils.setValue("d", packet, suffix);
				ReflectionUtils.setValue("g", packet, 1);
			}

			if (updateType == 0) {
				addAll(players);
			}
		}

		public PacketInfo(String name, Collection<String> players, int updateType) {
			packet = new PacketPlayOutScoreboardTeam();

			if (updateType != 3 && updateType != 4) {
				throw new IllegalArgumentException("Method must be join or leave for player constructor");
			}

			if (players == null || players.isEmpty()) {
				players = new ArrayList<String>();
			}

			ReflectionUtils.setValue("a", packet, name);
			ReflectionUtils.setValue("f", packet, updateType);
			addAll(players);
		}

		public void sendToPlayer(Player bukkitPlayer) {
			((CraftPlayer) bukkitPlayer).getHandle().playerConnection.sendPacket(packet);
		}

		@SuppressWarnings("all")
		private void addAll(Collection<String> col) {
			((Collection<String>) ReflectionUtils.getValue("e", packet)).addAll(col);
		}
	}

	static class TeamInfo {
		private final String name;
		private String prefix;
		private String suffix;
		private Set<String> players;

		TeamInfo(String name) {
			this.name = name;
			this.players = new HashSet<String>();
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getSuffix() {
			return suffix;
		}

		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}

		public String getName() {
			return name;
		}

		public Set<String> getPlayers() {
			return players;
		}
	}

}