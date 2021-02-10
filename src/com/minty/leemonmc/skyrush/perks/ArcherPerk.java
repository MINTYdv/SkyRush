package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.minty.leemonmc.skyrush.SkyRush;

public class ArcherPerk implements Perk {

	@SuppressWarnings("unused")
	private SkyRush main;
	
	public ArcherPerk(SkyRush _main) {
		main = _main;
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "§f➠ §7Utilise ton §earc §7et ta \n §eflèche unique §7à bon escient !";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Archer";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "§6Archer";
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		ItemStack it = new ItemStack(Material.BOW, 1, (byte) 2);
		it.setDurability((short) 384);
		items.add(new ItemStack(Material.ARROW, 1));
		items.add(it);
		return items;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		return null;
	}

	@Override
	public Material getIcon() {
		return Material.BOW;
	}

}
