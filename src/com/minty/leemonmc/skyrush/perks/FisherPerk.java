package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class FisherPerk implements Perk {
	
	@Override
	public String getDescription() {
		return "§f➠ §7Reçois une §ecanne à pêche §7pour attirer les ennemis vers toi lors \n §7des combats §eà distance §7!";
	}

	@Override
	public String getName() {
		return "Fisherman";
	}

	@Override
	public String getDisplayName() {
		return "§6Pêcheur";
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		rod.setDurability((short) 2);
		items.add(rod);
		return items;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		return null;
	}

	@Override
	public Material getIcon() {
		return Material.FISHING_ROD;
	}

}
