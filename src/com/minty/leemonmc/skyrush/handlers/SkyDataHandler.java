package com.minty.leemonmc.skyrush.handlers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.minty.leemonmc.skyrush.SkyRush;
import com.minty.leemonmc.skyrush.perks.Perk;

public class SkyDataHandler {

	private SkyRush main;
	private Map<Player, SkyData> playerDatas = new HashMap<>();
	
	public SkyDataHandler(SkyRush _main) {
		main = _main;
	}
	
	public SkyData getPlayerData(Player player) {
		if(playerDatas.get(player) == null) {
			playerDatas.put(player, new SkyData(player));
		}
		return playerDatas.get(player);
	}
	
	public Map<Player, SkyData> getPlayerDatas() {
		return playerDatas;
	}
	
	public void selectPerk(Player player, Perk perk)
	{
		if(getPlayerData(player).getSelectedPerk() == perk)
		{
			player.sendMessage("§6§l" + main.getGameApi().getGameManager().getMinigameName() + " §f§l» " + "§7Vous avez déjà sélectionné cet atout !");
		} else {
			player.sendMessage("§6§l" + main.getGameApi().getGameManager().getMinigameName() + " §f§l» " + "§7Vous avez sélectionné l'atout §6" + perk.getDisplayName() + " §7!");
			getPlayerData(player).setSelectedPerk(perk);
		}

	}
	
}
