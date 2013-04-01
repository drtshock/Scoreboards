package com.drtshock.scoreboards;

import net.minecraft.server.v1_5_R1.EntityPlayer;
import net.minecraft.server.v1_5_R1.Packet206SetScoreboardObjective;
import net.minecraft.server.v1_5_R1.Packet207SetScoreboardScore;
import net.minecraft.server.v1_5_R1.Packet208SetScoreboardDisplayObjective;
import net.minecraft.server.v1_5_R1.Scoreboard;
import net.minecraft.server.v1_5_R1.ScoreboardBaseObjective;
import net.minecraft.server.v1_5_R1.ScoreboardScore;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ScoreboardHandler {

	public enum StatType {
		KILL,
		DEATH;
	}

	Scoreboard sb;
	ScoreboardScore killScore;
	ScoreboardScore deathScore;

	Main plugin;
	EntityPlayer player;

	public ScoreboardHandler(Main m, Player p) {
		plugin = m;
		player = ((CraftPlayer) p).getHandle();
		sb = new Scoreboard();
		sb.a("Stats", new ScoreboardBaseObjective("Stats"));
		
		killScore = sb.a(ChatColor.GREEN + "Kills", sb.b("Stats"));
		killScore.c(0);
		
		deathScore = sb.a(ChatColor.DARK_RED + "Deaths", sb.b("Stats"));
		deathScore.c(0);

		Packet206SetScoreboardObjective scoreboardP = new Packet206SetScoreboardObjective(sb.b("Stats"), 0);
		Packet208SetScoreboardDisplayObjective displayP = new Packet208SetScoreboardDisplayObjective(1, sb.b("Stats"));
		
		Packet207SetScoreboardScore scoreKill = new Packet207SetScoreboardScore(killScore, 0);
		Packet207SetScoreboardScore scoreDeath = new Packet207SetScoreboardScore(deathScore, 0);

		player.playerConnection.sendPacket(scoreboardP);
		player.playerConnection.sendPacket(displayP);
		player.playerConnection.sendPacket(scoreKill);
		player.playerConnection.sendPacket(scoreDeath);

	}

	public void increment(StatType type) {
		switch(type) {
		case KILL:
			killScore.c(killScore.c() + 1);
			Packet207SetScoreboardScore scoreKill = new Packet207SetScoreboardScore(killScore, 0);
			player.playerConnection.sendPacket(scoreKill);
			break;
		case DEATH:
			deathScore.c(deathScore.c() + 1);
			Packet207SetScoreboardScore scoreDeath = new Packet207SetScoreboardScore(deathScore, 0);
			player.playerConnection.sendPacket(scoreDeath);
			break;
		}
	}
}
