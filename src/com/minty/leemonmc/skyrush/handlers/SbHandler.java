package com.minty.leemonmc.skyrush.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.minty.leemonmc.basics.core.GameState;
import com.minty.leemonmc.core.CoreMain;
import com.minty.leemonmc.core.events.GameStateChangeEvent;
import com.minty.leemonmc.games.core.Team;
import com.minty.leemonmc.skyrush.SkyRush;

public class SbHandler implements Listener {

	private static SkyRush main = SkyRush.getPlugin(SkyRush.class);
	private static Objective objective;
	private static String displayName;
	
	public SbHandler()
	{

	}
	
	public static void updateScoreboard(Player p) {
		displayName = "§6§lSkyRush";
		if(main.getGameApi().getGameManager().getCurrentState() == GameState.WAITING)
		{
			updateWaiting(p);
		}
		if(main.getGameApi().getGameManager().getCurrentState() == GameState.PLAYING)
		{
			updatePlaying(p);
		}
		if(main.getGameApi().getGameManager().getCurrentState() == GameState.FINISH)
		{
			updateFinish(p);
		}
	}
	
	@EventHandler
	public void onStateChange(GameStateChangeEvent e)
	{
		objective.unregister();
		Bukkit.broadcastMessage("§cUnregistering objectives");
	}
	
	private static void updateFinish(Player p)
	{
	    if(p.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); //Per-player scoreboard, not necessary if all the same data, but we're personalizing the displayname and all
	    objective.unregister();
	    Scoreboard score = p.getScoreboard();
	    
	    objective = score.getObjective(p.getName()) == null ? score.registerNewObjective(p.getName(), "dummy") : score.getObjective(p.getName()); //Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.
	    
	    objective.setDisplayName("\u00A7d\u00A7l" + displayName);
	    replaceScore(objective, 5, "§3");
	    replaceScore(objective, 4, "§cJeu terminé !");
	    replaceScore(objective, 3, "§7");
	    replaceScore(objective, 2, "§7Redémarrage dans §6" + main.getGameApi().getGameManager().getRestartTimer() + "s");
	    replaceScore(objective, 1, "§7");
	    replaceScore(objective, 0, CoreMain.getInstance().getScoreboardAnimator().getFooter());
	    if(objective.getDisplaySlot() != DisplaySlot.SIDEBAR) objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    p.setScoreboard(score);
	}
	
	private static void updatePlaying(Player p)
	{
	    if(p.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); //Per-player scoreboard, not necessary if all the same data, but we're personalizing the displayname and all
	    Scoreboard score = p.getScoreboard();
	    objective = score.getObjective(p.getName()) == null ? score.registerNewObjective(p.getName(), "dummy") : score.getObjective(p.getName()); //Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.

	    objective.setDisplayName("\u00A7d\u00A7l" + displayName);
	    
	    List<String> scoreContent = new ArrayList<>();
	    scoreContent.add(CoreMain.getInstance().getScoreboardAnimator().getFooter());
	    scoreContent.add("§d");
	    scoreContent.add("§4➠ §cAlliances interdites");
	    scoreContent.add("§7");
	    
	    String timerSeconds = String.format("%02d", main.getGameApi().getGameManager().getGameTimerSeconds());
	    String timerMinutes = String.format("%02d", main.getGameApi().getGameManager().getGameTimerMinutes());
	    
	    scoreContent.add("§7Timer: §6" + timerMinutes + ":" + timerSeconds);
	    scoreContent.add("§8");
	    
	    for(Team team : main.getGameApi().getTeamsManager().getTeams())
	    {
	    	scoreContent.add(team.getTag() + "➠ §7" + team.getName() + ": §6" + main.getGameManager().getPointsFormat(team));
	    }
	    
	    scoreContent.add("§3");
	    
	    for(int i = 0; i < scoreContent.size(); i++)
	    {
	    	replaceScore(objective, i, scoreContent.get(i));
	    }
	    
	    if(objective.getDisplaySlot() != DisplaySlot.SIDEBAR) objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    p.setScoreboard(score);

	}
	
	private static void updateWaiting(Player p)
	{

	    if(p.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); //Per-player scoreboard, not necessary if all the same data, but we're personalizing the displayname and all
	    Scoreboard score = p.getScoreboard(); //Personalized scoreboard
	    objective = score.getObjective(p.getName()) == null ? score.registerNewObjective(p.getName(), "dummy") : score.getObjective(p.getName()); //Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.

	    String color = "§c";

	    if(main.getGameApi().getGameManager().gs.countingDown == true)
	    {
	    	color = "§a";
	    } else {
	    	color = "§c";
	    }
	    
	    objective.setDisplayName("\u00A7d\u00A7l" + displayName);
	    replaceScore(objective, 9, "§8");
	    replaceScore(objective, 8, "§7En attente de joueurs");
	    replaceScore(objective, 7, "§7pour démarrer la partie...");
	    replaceScore(objective, 6, "§3");
	    replaceScore(objective, 5, "§7Joueurs: " + color + main.getGameApi().getGameManager().getPlayingPlayers() + "/" +  main.getGameApi().getGameManager().getPlayersNeededFull());
	    
	    if(main.getGameApi().getGameManager().gs.countingDown == true) {
		    replaceScore(objective, 4, "§7Démarrage dans: §6" + main.getGameApi().getGameManager().gs.getCountdown() + "s");
	    } else {
		    replaceScore(objective, 4, "§7Démarrage dans: §cAttente...");
	    }
	    replaceScore(objective, 3, "§7");
	    replaceScore(objective, 2, "§4➠ §cAlliances interdites");
	    replaceScore(objective, 1, "§d");
	    replaceScore(objective, 0, CoreMain.getInstance().getScoreboardAnimator().getFooter());
	    if(objective.getDisplaySlot() != DisplaySlot.SIDEBAR) objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    p.setScoreboard(score);
	}
	
	public static String getEntryFromScore(Objective o, int score) {
	    if(o == null) return null;
	    if(!hasScoreTaken(o, score)) return null;
	    for (String s : o.getScoreboard().getEntries()) {
	        if(o.getScore(s).getScore() == score) return o.getScore(s).getEntry();
	    }
	    return null;
	}

	public static boolean hasScoreTaken(Objective o, int score) {
	    for (String s : o.getScoreboard().getEntries()) {
	        if(o.getScore(s).getScore() == score) return true;
	    }
	    return false;
	}

	public static void replaceScore(Objective o, int score, String name) {
	    if(hasScoreTaken(o, score)) {
	        if(getEntryFromScore(o, score).equalsIgnoreCase(name)) return;
	        if(!(getEntryFromScore(o, score).equalsIgnoreCase(name))) o.getScoreboard().resetScores(getEntryFromScore(o, score));
	    }
	    o.getScore(name).setScore(score);
	}
	
}
