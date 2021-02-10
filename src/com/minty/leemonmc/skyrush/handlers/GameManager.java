package com.minty.leemonmc.skyrush.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.minty.leemonmc.basics.core.cache.Account;
import com.minty.leemonmc.core.stats.StatsData;
import com.minty.leemonmc.core.stats.StatsDataHandler;
import com.minty.leemonmc.core.util.Title;
import com.minty.leemonmc.games.core.Team;
import com.minty.leemonmc.games.events.GameFinishEvent;
import com.minty.leemonmc.games.rewards.GameReward;
import com.minty.leemonmc.games.rewards.GameRewardType;
import com.minty.leemonmc.skyrush.SkyRush;
import com.minty.leemonmc.skyrush.perks.Perk;

public class GameManager {

	private SkyRush main;
	private Title title;
	private boolean roundStarted;
	private List<Block> placedBlocks = new ArrayList<>();
	private Map<Location, Material> minedBlocksTypes = new HashMap<>();
	private List<Location> minedBlocksLocations = new ArrayList<>();
	
	public GameManager(SkyRush main) {
		this.main = main;
		title = new Title();
	}
	
	public void setup()
	{
		Bukkit.getWorld("world").getWorldBorder().setSize(getWorldBorderSizeLobby());
		Bukkit.getWorld("world").getWorldBorder().setCenter(getWorldBorderCenterLobby());
		
		title = new Title();
	}
	
	public void startGame()
	{
		for(Player player : main.getGameApi().getGameManager().getPlayingPlayersList())
		{
			StatsData data = StatsDataHandler.getPlayerStats(player);
			data.addStat("games", 1);
			SkyData skyData = main.getSkyDataHandler().getPlayerData(player);
			
			if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk() == null)
			{
				// Give random perk
				main.getSkyDataHandler().getPlayerData(player).setSelectedPerk(main.getPerksManager().getRandomPerk(main.getPerksManager().getPerks()));
			}
			player.sendMessage("§6§l" + main.getGameApi().getGameManager().getMinigameName() + " §f§l» " + "§7Vous commencez la partie avec l'atout §6" + main.getSkyDataHandler().getPlayerData(player).getSelectedPerk().getDisplayName() + " §7!");
		
			player.sendMessage("§7§m---------------------------------------");
			player.sendMessage("");
			player.sendMessage("§f➠ §7Atout sélectionné : §6" + ChatColor.stripColor(skyData.getSelectedPerk().getDisplayName()));
			player.sendMessage("");
			String[] split = skyData.getSelectedPerk().getDescription().split(" \n ");
			for(String string : split)
			{
				player.sendMessage(string);
			}
			player.sendMessage("");
			player.sendMessage("§7§m---------------------------------------");
		
		}
		startRound();
	}
	
	public void resetPlacedBlocks()
	{
		if(getPlacedBlocks() != null) {
			for(Block block : getPlacedBlocks()) {
				block.setType(Material.AIR);;
			}
			
    		getPlacedBlocks().clear();
		}
	}
	
	public void resetMinedBlocks()
	{
		if(getMinedBlocks() != null)
		{
			for(int i = 0; i < getMinedBlocks().size(); i++)
			{
				Location loc = getMinedBlocks().get(i);
				Material mat = getMinedBlocksTypes().get(loc);
				loc.getBlock().setType(mat);
			}
			getMinedBlocks().clear();
		}
	}
	
	public String getPointsFormat(Team _team)
	{
		String ptsChar = "⬤";
		String tag = _team.getTag();
		
		String result = "";
		
		for(int i = 0; i < getPointsGoal(); i++)
		{
			if(_team.getPoints() > i)
			{
				result += tag + ptsChar;
			} else {
				result += "§7" + ptsChar;
			}
		}
		return result;
	}
	
	public void mineBlock(BlockBreakEvent e) {
		minedBlocksTypes.put(e.getBlock().getLocation(), e.getBlock().getType());
		minedBlocksLocations.add(e.getBlock().getLocation());
	}
	
	public void placeBlock(Block block) {
		placedBlocks.add(block);
	}
	
	public List<Block> getPlacedBlocks() {
		return placedBlocks;
	}
	
	public Map<Location, Material> getMinedBlocksTypes() {
		return minedBlocksTypes;
	}
	
	public List<Location> getMinedBlocks() {
		return minedBlocksLocations;
	}
	
	public void startRound()
	{
		for(Team team : main.getGameApi().getTeamsManager().getTeams())
		{
			team.revive();
		}
		
		for(Entity entity : Bukkit.getWorld("world").getEntities())
		{
			if(entity.getType() == EntityType.ENDER_PEARL)
			{
				entity.remove();
			}
		}
		
		for(Player player : main.getGameApi().getGameManager().getPlayingPlayersList())
		{
			Team team = main.getGameApi().getTeamsManager().getTeam(player);

			player.teleport(team.getSpawn());
			
			player.getInventory().clear();
			
			player.getInventory().setHelmet(team.getColoredArmor(Material.LEATHER_HELMET));
			player.getInventory().setChestplate(team.getColoredArmor(Material.LEATHER_CHESTPLATE));
			player.getInventory().setLeggings(team.getColoredArmor(Material.LEATHER_LEGGINGS));
			player.getInventory().setBoots(team.getColoredArmor(Material.LEATHER_BOOTS));
			
			float defaultWalkSpeed = player.getWalkSpeed();
			float defaultFlySpeed = player.getFlySpeed();
			
			player.setWalkSpeed(0);
			player.setFlySpeed(0);
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, true, false));
			player.setGameMode(GameMode.SURVIVAL);

			main.getSkyDataHandler().getPlayerData(player).setLastHitted(null);
			main.getGameApi().getSpectatorHandler().leaveSpectator(player);
			
			setRoundStarted(false);
			resetPlacedBlocks();
			resetMinedBlocks();
			
	        @SuppressWarnings("unused")
			BukkitTask _task = new BukkitRunnable() {
				int timer = 3;
	        	@Override
	        	public void run()
	        	{

	        		player.setLevel(0);
	        		player.setTotalExperience(0);
	        		main.getSkyDataHandler().getPlayerData(player).setLastHitted(null);
	        		
	        		if(timer == 2)
	        		{
		        		title.sendTitle(player, 0, 20, 20, "", "§cA vos marques...");
		        		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 0.5f);
	        		}
	        		if(timer == 1)
	        		{
		        		title.sendTitle(player, 0, 20, 20, "", "§6Prêt...");
		        		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
	        		}
	        		if(timer == 0)
	        		{
		        		title.sendTitle(player, 0, 20, 20, "", "§aPartez !");
		        		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 2f);
	        			player.setWalkSpeed(defaultWalkSpeed);
	        			player.setFlySpeed(defaultFlySpeed);
	        			main.getSkyDataHandler().getPlayerData(player).setLastHitted(null);
	        			
	        			if(player.hasPotionEffect(PotionEffectType.JUMP))
	        			{
	        				player.removePotionEffect(PotionEffectType.JUMP);
	        			}
	        			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 255, true, false));
	        			
	        			ItemStack stik = main.leemonmc.getGuiUtils().createItem(Material.STICK, "§8* §6Bâton intersidéral §8*", (byte) 0);
	        			ItemMeta meta = stik.getItemMeta();
	        			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
	        			stik.setItemMeta(meta);
	        			
	        			player.getInventory().setItem(0, stik);
	        			main.getGameManager().setRoundStarted(true);
	        			
	        			// Give perks stuff
	        			Perk playerPerk = main.getSkyDataHandler().getPlayerData(player).getSelectedPerk();
	        			if(playerPerk != null)
	        			{
	        				if(playerPerk.getItems() != null) {
		        				for(ItemStack it : playerPerk.getItems()) {
		        					player.getInventory().addItem(it);
		        				}
	        				}

	        				if(playerPerk.getPotionsEffects() != null) {
		        				for(PotionEffect eff : playerPerk.getPotionsEffects()) {
		        					player.addPotionEffect(eff);
		        				}
	        				}
	        			
	        			}
	        			
	        			this.cancel();
	        		}
	        		timer--;
	        	}
	        	
	        }.runTaskTimer(main, 0, 20);
		}
		
		Bukkit.getWorld("world").getWorldBorder().setSize(getWorldBorderSizeGame());
		Bukkit.getWorld("world").getWorldBorder().setCenter(getWorldBorderCenterGame());
	}
	
	public void eliminate(Player player)
	{
		Account accounts = main.leemonmc.getAccountManager().getAccount(player.getUniqueId().toString());
		if(accounts.isModEnabled() == true) return;
		
		Team team = main.getGameApi().getTeamsManager().getTeam(player);
		
		main.getGameApi().getSpectatorHandler().setSpectator(player);
		
		player.teleport(team.getSpawn());
		
		Player damager = main.getSkyDataHandler().getPlayerData(player).getLastHitPlayer();
		
		if(damager != null) {
			Account account = main.leemonmc.getAccountManager().getAccount(damager.getUniqueId().toString());
			
			if(account.isModEnabled() == true) {
				damager = null;
			}
		}
		
		StatsData data = StatsDataHandler.getPlayerStats(player);
		data.addStat("deaths", 1);
		
		if(damager == null) {
			Bukkit.broadcastMessage("§6§l" + main.getGameApi().getGameManager().getMinigameName() + " §f§l» " + team.getTag() + team.getName() + " " + player.getDisplayName() + " §7est mort.");
		} else {
			Team teamTarget = main.getGameApi().getTeamsManager().getTeam(damager);
			Bukkit.broadcastMessage("§6§l" + main.getGameApi().getGameManager().getMinigameName() + " §f§l» " + team.getTag() + team.getName() + " " + player.getDisplayName() + " §7a été poussé dans le vide par " + teamTarget.getTag() + teamTarget.getName() + " " + damager.getDisplayName() + " §7!");
			damager.playSound(damager.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			
			StatsData damagerData = StatsDataHandler.getPlayerStats(damager);
			damagerData.addStat("kills", 1);
			title.sendActionBar(damager, "§eGain§6: §e+5 pulpes §8(§7Kill§8)");
			main.getGameApi().getGameRewardsManager().getPlayerRewards(damager).getGameRewards().add(new GameReward(GameRewardType.KILLS, 5, 0));
		}
		main.getSkyDataHandler().getPlayerData(player).setLastHitted(null);
		
		title.sendTitle(player, 0, 20, 20, "", "§c☠  Vous êtes mort ! ☠");
		team.eliminatePlayer(player);
		player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1f, 1f);
		
		if(team.getAlivePlayers().size() == 0)
		{
			team.eliminate();
		}
		
		if(main.getGameApi().getTeamsManager().getAliveTeams().size() == 1)
		{
			Team winner = main.getGameApi().getTeamsManager().getAliveTeams().get(0);
			scorePoint(winner);
		}

	}
	
	public void setRoundStarted(boolean _target) {
		roundStarted = _target;
	}
	
	public boolean roundStarted() {
		return roundStarted;
	}

	public int getWorldBorderSizeGame()
	{
		return main.getConfig().getInt("worldborder.game.size");
	}
	
	public int getWorldBorderSizeLobby()
	{
		return main.getConfig().getInt("worldborder.lobby.size");
	}
	
	public Location getWorldBorderCenterGame()
	{
		float x = (float) main.getConfig().getDouble("worldborder.game.center.x");
		float y = 0;
		float z = (float) main.getConfig().getDouble("worldborder.game.center.z");
		
		return new Location(Bukkit.getWorld("world"), x, y, z);
	}
	
	public Location getWorldBorderCenterLobby()
	{
		float x = (float) main.getConfig().getDouble("worldborder.lobby.center.x");
		float y = 0;
		float z = (float) main.getConfig().getDouble("worldborder.lobby.center.z");
		
		return new Location(Bukkit.getWorld("world"), x, y, z);
	}
	
	public int getDeathLevel()
	{
		return main.getConfig().getInt("death-level"); 
	}
	
	public void scorePoint(Team team)
	{
		team.addPoint();

		
		if(team.getPoints() >= getPointsGoal())
		{
			for(Player player : main.getGameApi().getGameManager().getPlayingPlayersList())
			{
				Team targetTeam = main.getGameApi().getTeamsManager().getTeam(player);
				StatsData data = StatsDataHandler.getPlayerStats(player);
				
				if(targetTeam.getArmorColor() == team.getArmorColor()) {
					data.addStat("victories", 1);
				} else {
					data.addStat("defeats", 1);
				}
			}
			Bukkit.getPluginManager().callEvent(new GameFinishEvent(team));
		} else {
			startRound();
			for(Player player : team.getPlayers())
			{
				StatsData data = StatsDataHandler.getPlayerStats(player);
				data.addStat("points", 1);
				title.sendTitle(player, 0, 20, 20, "", "§e✦ + 1 point ✦");
				player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1f, 1f);
			}
		}
		
	}
	
	public int getPointsGoal()
	{
		return main.getConfig().getInt("points-goal");
	}
	


}
