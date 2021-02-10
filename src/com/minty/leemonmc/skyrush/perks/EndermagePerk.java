package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class EndermagePerk implements Perk {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "§f➠ §7Reçois une §eperle de l'ender §7! De quoi te donner \n §eune deuxième chance §7au cas où le pire arriverait !";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Endermage";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "§6Endermage";
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(Material.ENDER_PEARL, 1));
		return items;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Material getIcon() {
		// TODO Auto-generated method stub
		return Material.ENDER_PEARL;
	}

}
