package com.minty.leemonmc.skyrush.perks;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public interface Perk
{

	public String getDescription();
	public String getName();
	public String getDisplayName();
	public List<ItemStack> getItems();
	public List<PotionEffect> getPotionsEffects();
	public Material getIcon();
	
}
