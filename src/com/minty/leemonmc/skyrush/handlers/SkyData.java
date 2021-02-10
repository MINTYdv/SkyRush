package com.minty.leemonmc.skyrush.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.minty.leemonmc.skyrush.perks.Perk;

public class SkyData
{

	private Perk selectedPerk;
	private Player lastHittedBy;
	private Player owner;
	
	public SkyData(Player _owner) {
		owner = _owner;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public Player getLastHitPlayer() {
		return lastHittedBy;
	}
	
	public Perk getSelectedPerk() {
		return selectedPerk;
	}
	
	public void setLastHitted(Player player) {
		if(player != getOwner()) {
			lastHittedBy = player;
		}
	}
	
	public void setSelectedPerk(Perk perk)
	{
		selectedPerk = perk;
	}
	
}
