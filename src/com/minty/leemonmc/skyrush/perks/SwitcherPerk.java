package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SwitcherPerk implements Perk {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "§f➠ §7Les boules de neige de cet atout vous permettent \n §ed'échanger §7de §eposition §7avec la personne qui les reçoit !";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Switcher";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "§6Switcher";
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> result = new ArrayList<>();
		result.add(new ItemStack(Material.SNOW_BALL, 2));
		return result;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		List<PotionEffect> effects = new ArrayList<>();
		return effects;
	}

	@Override
	public Material getIcon() {
		return Material.SNOW_BALL;
	}

}
