package com.drtshock.scoreboards;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	HashMap<Player, ScoreboardHandler> handlers = new HashMap<Player, ScoreboardHandler>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		ScoreboardHandler handler = new ScoreboardHandler(this, p);
		handlers.put(p, handler);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		ScoreboardHandler sbh = handlers.get(event.getPlayer());
		sbh.increment(ScoreboardHandler.StatType.PlACE_BLOCK);
		handlers.put(event.getPlayer(), sbh);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		ScoreboardHandler sbh = handlers.get(event.getPlayer());
		sbh.increment(ScoreboardHandler.StatType.BREAK_BLOCK);
		handlers.put(event.getPlayer(), sbh);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		handlers.remove(p);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		ScoreboardHandler sbh = handlers.get(event.getEntity());
		sbh.increment(ScoreboardHandler.StatType.DEATH);
		handlers.put(event.getEntity(), sbh);

		if(event.getEntity().getKiller() != null) {
			ScoreboardHandler sbh1 = handlers.get(event.getEntity().getKiller());
			sbh1.increment(ScoreboardHandler.StatType.KILL);
			handlers.put(event.getEntity().getKiller(), sbh1);
		}
	}

}
