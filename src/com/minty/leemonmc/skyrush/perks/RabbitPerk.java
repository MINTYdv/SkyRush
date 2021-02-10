package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RabbitPerk implements Perk {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "§f➠ §7Vous permet de §esauter plus haut §7lors \n §7des parties.";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Rabbit";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "§6Lapin sauteur";
	}

	@Override
	public List<ItemStack> getItems() {
		return null;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		List<PotionEffect> effects = new ArrayList<>();
		effects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
		return effects;
	}

	@Override
	public Material getIcon() {
		return Material.RABBIT_FOOT;
	}

}
