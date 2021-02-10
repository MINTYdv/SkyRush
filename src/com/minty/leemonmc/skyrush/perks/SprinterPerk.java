package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.minty.leemonmc.skyrush.SkyRush;

public class SprinterPerk implements Perk {

	@SuppressWarnings("unused")
	private SkyRush main;
	
	public SprinterPerk(SkyRush _main) {
		main = _main;
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "§f➠ §7Cet atout vous permet de §ecourir plus vite §7!";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Sprinter";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "§6Coureur";
	}

	@Override
	public List<ItemStack> getItems() {
		return null;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		List<PotionEffect> effects = new ArrayList<>();
		effects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		return effects;
	}

	@Override
	public Material getIcon() {
		return Material.FEATHER;
	}

}
