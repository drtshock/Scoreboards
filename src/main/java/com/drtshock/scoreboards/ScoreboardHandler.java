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
		BREAK_BLOCK,
		PlACE_BLOCK;
	}
	
	Scoreboard sb;
	ScoreboardScore placedBlocksScore;
	ScoreboardScore breakBlocksScore;
	
	Main plugin;
	EntityPlayer player;
	
	public ScoreboardHandler(Main m, Player p) {
		plugin = m;
		player = ((CraftPlayer) p).getHandle();
		sb = new Scoreboard();
		sb.a("Stats", new ScoreboardBaseObjective("Stats"));
		placedBlocksScore = sb.a(ChatColor.GREEN + "Blocks Placed", sb.b("Stats"));
		placedBlocksScore.c(0);
		breakBlocksScore = sb.a(ChatColor.RED + "Blocks Broken", sb.b("Stats"));
		breakBlocksScore.c(0);
		Packet206SetScoreboardObjective scoreboardP = new Packet206SetScoreboardObjective(sb.b("Stats"), 0);
		Packet208SetScoreboardDisplayObjective displayP = new Packet208SetScoreboardDisplayObjective(1, sb.b("Stats"));
		Packet207SetScoreboardScore scoreItemBreak = new Packet207SetScoreboardScore(breakBlocksScore, 0);
		Packet207SetScoreboardScore scoreItemPlace = new Packet207SetScoreboardScore(placedBlocksScore, 0);
		player.playerConnection.sendPacket(scoreboardP);
		player.playerConnection.sendPacket(displayP);
		player.playerConnection.sendPacket(scoreItemBreak);
		player.playerConnection.sendPacket(scoreItemPlace);
	}
	
	public void increment(StatType type) {
		switch(type) {
		case BREAK_BLOCK:
			breakBlocksScore.c(breakBlocksScore.c() + 1);
			Packet207SetScoreboardScore scoreItemBreak = new Packet207SetScoreboardScore(breakBlocksScore, 0);
			player.playerConnection.sendPacket(scoreItemBreak);
			break;
		case PlACE_BLOCK:
			placedBlocksScore.c(placedBlocksScore.c() + 1);
			Packet207SetScoreboardScore scoreItemPlace = new Packet207SetScoreboardScore(placedBlocksScore, 0);
			player.playerConnection.sendPacket(scoreItemPlace);
			break;
		}
	}
}
