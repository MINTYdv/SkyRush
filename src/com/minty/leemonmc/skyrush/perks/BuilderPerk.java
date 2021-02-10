package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.minty.leemonmc.skyrush.SkyRush;

public class BuilderPerk implements Perk {

	@SuppressWarnings("unused")
	private SkyRush main;
	
	public BuilderPerk(SkyRush _main) {
		main = _main;
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "§f➠ §7Reçois au §edémarrage de chaque partie \n §7de quoi construire §eun pont§7 ou te §edéfendre§7 !";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Builder";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "§6Constructeur";
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		ItemStack it = new ItemStack(Material.SANDSTONE, 4, (byte) 2);
		items.add(it);
		return items;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		return null;
	}

	@Override
	public Material getIcon() {
		return Material.SANDSTONE;
	}

}
