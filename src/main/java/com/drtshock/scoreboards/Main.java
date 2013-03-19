package com.drtshock.scoreboards;

import java.io.File;
import java.util.List;

import net.minecraft.server.v1_5_R1.Packet;
import net.minecraft.server.v1_5_R1.Packet206SetScoreboardObjective;
import net.minecraft.server.v1_5_R1.Packet207SetScoreboardScore;
import net.minecraft.server.v1_5_R1.Packet208SetScoreboardDisplayObjective;
import net.minecraft.server.v1_5_R1.Scoreboard;
import net.minecraft.server.v1_5_R1.ScoreboardBaseObjective;
import net.minecraft.server.v1_5_R1.ScoreboardScore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public Main plugin;

	public void onEnable() {
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if(!file.exists())
			saveDefaultConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("sb")) {
			if(sender.hasPermission("scoreboard.admin")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.AQUA + "================");
					sender.sendMessage(ChatColor.WHITE + "/sc reload - reloads the config");
					sender.sendMessage(ChatColor.WHITE + "/sc update - updates scoreboards");
					sender.sendMessage(ChatColor.AQUA + "================");
					return true;
				}
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload")) {
						reloadConfig();
						sender.sendMessage(ChatColor.GREEN + "Scoreboard config reloaded!");
						return true;
					}
					if(args[0].equalsIgnoreCase("update")) {
						run();
						sender.sendMessage(ChatColor.GREEN + "Updated scoreboards for all players!");
						return true;
					}
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "No permissions :(");
				return true;
			}
		}
		return true;
	}

	public void run() {
		List<String> messages = getConfig().getStringList("messages");

		Scoreboard sb = new Scoreboard();
		sb.a("Messages", new ScoreboardBaseObjective("Messages"));

		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			Packet206SetScoreboardObjective newPacket = new Packet206SetScoreboardObjective();
			Packet208SetScoreboardDisplayObjective displayPacket = new Packet208SetScoreboardDisplayObjective(1, sb.b("Messages"));
			sendPacket(p, newPacket);
			sendPacket(p, displayPacket);

			for(String s : messages) {
				String display = ChatColor.translateAlternateColorCodes('&', s);
				ScoreboardScore score = sb.a(display, sb.b("Messages"));
				score.c();
				Packet207SetScoreboardScore scoreItem = new Packet207SetScoreboardScore(score, 0);
				sendPacket(p, scoreItem);
			}
		}
		return;
	}

	public void sendPacket(Player player, Packet packet) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

}
