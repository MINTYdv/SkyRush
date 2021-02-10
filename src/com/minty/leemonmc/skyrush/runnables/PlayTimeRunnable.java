package com.minty.leemonmc.skyrush.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.minty.leemonmc.basics.core.GameState;
import com.minty.leemonmc.core.stats.StatsData;
import com.minty.leemonmc.core.stats.StatsDataHandler;
import com.minty.leemonmc.skyrush.SkyRush;

public class PlayTimeRunnable extends BukkitRunnable {

	private SkyRush main = SkyRush.getInstance();
	
	@Override
	public void run()
	{
		if(main.getGameApi().getGameManager().getCurrentState() != GameState.PLAYING) return;
		
		for(Player player : main.getGameApi().getGameManager().getPlayingPlayersList())
		{
			StatsData data = StatsDataHandler.getPlayerStats(player);
			data.addStat("playtime", 1);
		}
	}
	
}
