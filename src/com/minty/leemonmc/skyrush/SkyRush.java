package com.minty.leemonmc.skyrush;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.minty.leemonmc.core.CoreMain;
import com.minty.leemonmc.games.LeemonGame;
import com.minty.leemonmc.games.handlers.MiniGameManager;
import com.minty.leemonmc.skyrush.handlers.GameManager;
import com.minty.leemonmc.skyrush.handlers.SbHandler;
import com.minty.leemonmc.skyrush.handlers.SkyDataHandler;
import com.minty.leemonmc.skyrush.listeners.SkyRushListeners;
import com.minty.leemonmc.skyrush.perks.PerksManager;
import com.minty.leemonmc.skyrush.runnables.PlayTimeRunnable;

public class SkyRush extends JavaPlugin {

	private SbHandler scoreboardHandler;
	
	public CoreMain leemonmc = (CoreMain) Bukkit.getPluginManager().getPlugin("LeemonCore");
	private LeemonGame gameApi = (LeemonGame) Bukkit.getPluginManager().getPlugin("LeemonGame");
	
	private GameManager gameManager;
	private PerksManager perksManager;
	private SkyDataHandler skyDataHandler;
	private MiniGameManager mgm;
	
	private static SkyRush instance;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable()
	{
		saveDefaultConfig(); // Load default config
		Bukkit.setDefaultGameMode(GameMode.ADVENTURE);
		Bukkit.getWorld("world").setTime(13000);
		Bukkit.getWorld("world").setGameRuleValue("DO_DAYLIGHT_CYCLE", "false");
		Bukkit.getWorld("world").setGameRuleValue("DO_MOB_SPAWNING", "false");
		Bukkit.getWorld("world").setGameRuleValue("DO_MOB_LOOT", "false");
		
		/*
		 * REGISTER STUFF AND REFENRENCES
		 * */
		
		leemonmc = (CoreMain) Bukkit.getServer().getPluginManager().getPlugin("LeemonCore");
		gameApi = (LeemonGame) Bukkit.getPluginManager().getPlugin("LeemonGame");
		
		instance = this;
		
		gameManager = new GameManager(this);
		Bukkit.getWorld("world").getWorldBorder().setSize(getGameManager().getWorldBorderSizeLobby());
		Bukkit.getWorld("world").getWorldBorder().setCenter(getGameManager().getWorldBorderCenterLobby());
		perksManager = new PerksManager(this);
		skyDataHandler = new SkyDataHandler(this);
		
		/*
		 * REGISTER COMMANDS AND EVENTS
		 *  */
		
		getServer().getPluginManager().registerEvents(new SkyRushListeners(this), this);
		getServer().getPluginManager().registerEvents(new SbHandler(), this);
		
		/* 
		 * UPDATE PART
		 * */
        new BukkitRunnable() {
        	@Override
        	public void run()
        	{
        		updateScoreboardforAll();
        	}
        }.runTaskTimer(this, 0, 2);
        
        Bukkit.getScheduler().runTaskTimer(this, new PlayTimeRunnable(), 20L * 60, 20L * 60);
	}
	
	@SuppressWarnings("static-access")
	public void updateScoreboardforAll() {
		for(Player online : Bukkit.getOnlinePlayers())
		{
			getScoreboardHandler().updateScoreboard(online);
		}
	}
	
	@Override
	public void onDisable()
	{
		getGameManager().resetPlacedBlocks();
		getGameManager().resetMinedBlocks();
		System.out.println("[SkyRush] Plugin inactif !");
	}
	
	public SkyDataHandler getSkyDataHandler() {
		return skyDataHandler;
	}
	
	public PerksManager getPerksManager() {
		return perksManager;
	}
	
	public SbHandler getScoreboardHandler() {
		return scoreboardHandler;
	}

	public GameManager getGameManager() {
		return gameManager;
	}
	
	public static SkyRush getInstance() {
		return instance;
	}
	
	public LeemonGame getGameApi() {
		return gameApi;
	}
	
}
