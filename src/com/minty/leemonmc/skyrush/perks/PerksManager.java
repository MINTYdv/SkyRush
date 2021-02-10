package com.minty.leemonmc.skyrush.perks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.minty.leemonmc.skyrush.SkyRush;

public class PerksManager {

	private List<Perk> perks = new ArrayList<>();
	
	private SkyRush main;
	
	public PerksManager(SkyRush _main) {
		this.main = _main;
		registerPerk(new FisherPerk());
		registerPerk(new MinerPerk());
		registerPerk(new BuilderPerk(main));
		registerPerk(new EndermagePerk());
		registerPerk(new RabbitPerk());
		registerPerk(new ArcherPerk(main));
		registerPerk(new SprinterPerk(main));
		registerPerk(new SwitcherPerk());
	}
	
	public void registerPerk(Perk perk) {
		perks.add(perk);
	}
	
	public List<Perk> getPerks() {
		return perks;
	}
	
	public Perk iconToPerk(ItemStack it) 
	{
		for(Perk perk : perks) 
		{
			if(perk.getIcon() == it.getType()) {
				if(getPerkIcon(perk, null).getItemMeta().getDisplayName() == it.getItemMeta().getDisplayName())
				{
					return perk;
				}
			}
		}
		return null;
	}
	
	public void randomPerk(Player player)
	{
		main.getSkyDataHandler().selectPerk(player, null);
	}
	
	@SuppressWarnings("unused")
	public Perk getRandomPerk(List<Perk> givenList) {
	    Random rand = new Random();
	 
	    int numberOfElements = 2;
	 
	    for (int i = 0; i < numberOfElements; i++) {
	        int randomIndex = rand.nextInt(givenList.size());
	        Perk randomElement = givenList.get(randomIndex);
	        return randomElement;
	    }
	    return null;
	}
	
	public ItemStack getPerkIcon(Perk perk, Player player)
	{
		List<String> lore = new ArrayList<>();
		String[] split = perk.getDescription().split(" \n ");
		for(String string : split) {
			lore.add(string);
		}
		lore.add("");

		if(player == null) {
			lore.add("§6» §eCliquez pour sélectionner cet atout");
		} else {
			if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk() != null)
			{
				if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk() == perk)
				{
					lore.add("§4» §cVous avez déjà sélectionné cet atout !");
				} else {
					lore.add("§6» §eCliquez pour sélectionner cet atout");
				}
			} else {
				lore.add("§6» §eCliquez pour sélectionner cet atout");
			}
		}
		
		ItemStack it = main.leemonmc.getGuiUtils().createItem(perk.getIcon(), perk.getDisplayName(), (byte) 0, lore);
		return it;
	}
	
}
