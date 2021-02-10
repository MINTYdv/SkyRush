package com.minty.leemonmc.skyrush.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.minty.leemonmc.basics.core.GameState;
import com.minty.leemonmc.basics.core.ServerType;
import com.minty.leemonmc.basics.core.cache.Account;
import com.minty.leemonmc.core.CoreMain;
import com.minty.leemonmc.core.events.CoreInitEvent;
import com.minty.leemonmc.core.events.GuisLoadingEvent;
import com.minty.leemonmc.core.events.dataLoadedEvent;
import com.minty.leemonmc.games.events.GameStartEvent;
import com.minty.leemonmc.games.events.LobbyPlayerJoinEvent;
import com.minty.leemonmc.skyrush.SkyRush;
import com.minty.leemonmc.skyrush.gui.PerksSelectorMenu;
import com.minty.leemonmc.skyrush.perks.SwitcherPerk;

public class SkyRushListeners implements Listener {

	private SkyRush main;
	private ItemStack perkItem;
	
	public SkyRushListeners(SkyRush main) {
		this.main = main;
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e)
	{
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		
		if(!(e.getDamager() instanceof Arrow))
		{
			return;
		}

		Arrow arrow = (Arrow) e.getDamager();
		
		if(!(arrow.getShooter() instanceof Player)) {
			return;
		}
		
		main.getSkyDataHandler().getPlayerData((Player) e.getEntity()).setLastHitted((Player) arrow.getShooter());
	}
	
	@EventHandler
	public void onDataLoaded(dataLoadedEvent e)
	{
		Player player = e.getPlayer();
		
		perkItem = main.leemonmc.getGuiUtils().createItem(Material.BLAZE_POWDER, "§6Choix de l'atout §7§o(Clic droit)", (byte) 0);
		
		player.sendMessage("§7§m---------------------------------------");
		player.sendMessage("");
		player.sendMessage("                            §6§lSkyRush");
		player.sendMessage("                           §7By LeemonMC");
		player.sendMessage("");
		player.sendMessage("§f➠ §eBut du jeu :");
		player.sendMessage("§7Expulsez vos ennemis en dehors de l'arène en utilisant");
		player.sendMessage("§7vos atouts à votre avantage ! Que le meilleur gagne !");
		player.sendMessage("");
		player.sendMessage("§7§m---------------------------------------");
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if(player == null) return;
		
		if(main.leemonmc.getGameApi().getGameManager().getCurrentState() == GameState.PLAYING)
		{
			Account account = main.leemonmc.getAccountManager().getAccount(player.getUniqueId().toString());
			
			if(account.isModEnabled()) return;
			
			if(main.getGameManager().roundStarted() == false)
			{
				if(e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())
				{
					e.setCancelled(true);
				}
			}
			
			if(player.getLocation().getY() <= main.getGameManager().getDeathLevel())
			{
				main.getGameManager().eliminate(player);
			}
			
		}
	}
	
	@EventHandler
	public void onGuisLoading(GuisLoadingEvent e) {
		e.getGuiManager().addMenu(new PerksSelectorMenu(main));
	}
	
	@EventHandler
	public void onCoreInit(CoreInitEvent e) {
		System.out.println("Initiating");
		CoreMain leemonmcMain = e.getMain();
		leemonmcMain.init(ServerType.MINIGAME, "SkyRush");
		
		List<String> stats = new ArrayList<>();
		stats.add("victories"); stats.add("defeats"); stats.add("kills"); stats.add("deaths"); stats.add("games"); stats.add("points");
		
		stats.add("fisher");
		stats.add("miner");
		stats.add("builder");
		stats.add("endermage");
		stats.add("bunny");
		stats.add("archer");
		stats.add("sprinter");
		stats.add("switcher");
		leemonmcMain.getStatsHandler().init("skyrush", stats);
	}
	
	@EventHandler
	public void onSnowball(EntityDamageByEntityEvent e)
	{
		Entity entity = e.getEntity();
		
		if(entity instanceof Player)
		{
			Player target = (Player) entity;
			
			if(e.getDamager() instanceof Snowball)
			{
				Snowball snowball = (Snowball) e.getDamager();
				
				if(snowball.getShooter() instanceof Player) {
					Player player = (Player) snowball.getShooter();
					
					if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk().getName() == new SwitcherPerk().getName())
					{
						Location targetLocation = target.getLocation();
						Location shooterLocation = player.getLocation();
						
						target.teleport(shooterLocation);
						player.teleport(targetLocation);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent e) {
		main.getGameManager().startGame();
	}
	
	@EventHandler
	public void onEndermiteSpawn(EntitySpawnEvent e) {
		if(e.getEntityType() == EntityType.ENDERMITE)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerJoinLobby(LobbyPlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(main.getGameApi().getGameManager().getCurrentState() == GameState.WAITING)
		{
			p.getInventory().setItem(4, perkItem);
		}

	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player placer = e.getPlayer();
		Block block = e.getBlock();
		
		if(main.getGameApi().getGameManager().getCurrentState() == GameState.WAITING) return;
		if(main.getGameApi().getGameManager().getCurrentState() == GameState.FINISH) return;
		
		if(placer.getGameMode() == GameMode.SURVIVAL) {
			if(main.getSkyDataHandler().getPlayerData(placer).getSelectedPerk().getName() == "Builder")
			{
				main.getGameManager().placeBlock(block);
			} else {
				e.setCancelled(true);
			}

		}

	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		main.getGameManager().eliminate(e.getPlayer());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		Action action = e.getAction();
		ItemStack it = e.getItem();
		if(it == null) return;
		if(action == null) return;
		if(player == null) return;
		if(perkItem == null) return;
		
		if(it.getItemMeta().getDisplayName() == perkItem.getItemMeta().getDisplayName())
		{
			if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
			{
				main.leemonmc.getGuiManager().open(player, PerksSelectorMenu.class);
			}
		}
		
	}
	
	@EventHandler
	public void onArrowPickup(PlayerPickupArrowEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage2(EntityDamageByEntityEvent e) {
		Player target;
		Player damager;
		if(e.getDamager() instanceof Player)
		{
			damager = (Player) e.getDamager();
			
			if(e.getEntity() instanceof Player)
			{
				target = (Player) e.getEntity();
			
				main.getSkyDataHandler().getPlayerData(target).setLastHitted(damager);
			} else {
				return;
			}
		} else {
			return;
		}
	
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		Player player;
		if(e.getEntity() instanceof Player)
		{
			player = (Player) e.getEntity();
		} else {
			return;
		}
		if(player == null) return;
		
		if(main.getGameApi().getGameManager().getCurrentState() == GameState.WAITING || main.getGameApi().getGameManager().getCurrentState() == GameState.FINISH)
		{
			e.setCancelled(true);
		} else
		{
			main.getSkyDataHandler().getPlayerData(player).setLastHitted(player);
			DamageCause cause = e.getCause();
			if(cause == DamageCause.FALL || cause == DamageCause.VOID)
			{
				e.setCancelled(true);
			}

		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		
		if(player.getGameMode() != GameMode.CREATIVE)
		{
			if(main.getGameApi().getGameManager().getCurrentState() != GameState.PLAYING)
			{
				e.setCancelled(true);
			} else
			{
				if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk() != null)
				{
					if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk().getName() == "Miner")
					{
						if(e.getBlock().getType() == Material.SANDSTONE)
						{
							if(!main.getGameManager().getPlacedBlocks().contains(e.getBlock()))
							{
								main.getGameManager().mineBlock(e);
							}

						} else {
							e.setCancelled(true);
						}

					} else {
						e.setCancelled(true);
					}
				} else
				{
					e.setCancelled(true);
				}

			}
			
		}

	}
	
}
