package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class MinerPerk implements Perk {

	@Override
	public String getDescription() {
		return "§f➠ §7Pour creuser §esous les pieds §7des ennemis ou \n §7rendre certaines zones §einaccessibles §7!";
	}

	@Override
	public String getName() {
		return "Miner";
	}

	@Override
	public String getDisplayName() {
		return "§6Mineur";
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		pick.setDurability((short) 246);
		items.add(pick);
		return items;
	}

	@Override
	public List<PotionEffect> getPotionsEffects() {
		return null;
	}

	@Override
	public Material getIcon() {
		return Material.IRON_PICKAXE;
	}

}
