package com.minty.leemonmc.skyrush.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.minty.leemonmc.core.CoreMain;
import com.minty.leemonmc.core.util.GuiBuilder;
import com.minty.leemonmc.core.util.GuiUtils;
import com.minty.leemonmc.skyrush.SkyRush;

public class PerksSelectorMenu implements GuiBuilder {

	private SkyRush main;
	private GuiUtils utils = CoreMain.getInstance().getGuiUtils();
	
	private List<Integer> slots = new ArrayList<>();
	
	public PerksSelectorMenu(SkyRush _main) {
		main = _main;
		
		slots.add(20);
		slots.add(21);
		slots.add(22);
		slots.add(23);
		slots.add(24);
		
		slots.add(30);
		slots.add(31);
		slots.add(32);
	}
	
	@Override
	public void contents(Player player, Inventory inv) {
		
		inv.setItem(0, utils.pane());
		inv.setItem(1, utils.pane());
		inv.setItem(2, utils.pane());
		inv.setItem(3, utils.pane());
		inv.setItem(4, utils.pane());
		inv.setItem(5, utils.pane());
		inv.setItem(6, utils.pane());
		inv.setItem(7, utils.pane());
		inv.setItem(8, utils.pane());
		
		inv.setItem(9, utils.pane());
		inv.setItem(9 * 2, utils.pane());
		inv.setItem(9 * 3, utils.pane());
		inv.setItem(9 * 4, utils.pane());
		inv.setItem(9 * 5, utils.pane());
		inv.setItem(51, utils.pane());
		inv.setItem(52, utils.pane());
		inv.setItem(53, utils.pane());
		
		inv.setItem(46, utils.pane());
		inv.setItem(47, utils.pane());

		inv.setItem(8 * 2 + 1, utils.pane());
		
		inv.setItem(8 * 3 + 2, utils.pane());
		inv.setItem(8 * 4 + 3, utils.pane());
		
		inv.setItem(8 * 5 + 4, utils.pane());
		
		for(int i = 0; i < main.getPerksManager().getPerks().size(); i++)
		{
			int slot = slots.get(i);
			inv.setItem(slot, main.getPerksManager().getPerkIcon(main.getPerksManager().getPerks().get(i), player));
		}
		
		inv.setItem(44, utils.pane());
		inv.setItem(45, utils.pane());
	
		inv.setItem(50, main.leemonmc.getGuiUtils().cancelItem());
		inv.setItem(49, getRandomPerkItem(player));
	}

	private ItemStack getRandomPerkItem(Player player)
	{
		ItemStack it = main.leemonmc.getGuiUtils().randomItem();
		ItemMeta meta = it.getItemMeta();
		
		meta.setDisplayName("§6Aléatoire");
		
		List<String> lore = new ArrayList<>();
		
		lore.add("");
		if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk() == null)
		{
			lore.add("§4» §cVous avez déjà choisi un atout aléatoire !");
		} else {
			lore.add("§6» §eCliquez pour choisir un atout aléatoire !");
		}
		
		meta.setLore(lore);
		it.setItemMeta(meta);
		return it;
	}
	
	@Override
	public int getSize()
	{
		return 54;
	}

	@Override
	public String name() {
		return "§6Sélection d'un atout";
	}

	@Override
	public void onClick(Player player, Inventory inv, ItemStack it, int slot) {

		if(it == null || it.getType() == Material.AIR) return;
		
		switch(it.getType()) {
			case BARRIER:
				player.closeInventory();
				break;
			case SKULL_ITEM:
				if(main.getSkyDataHandler().getPlayerData(player).getSelectedPerk() == null)
				{
					break;
				} else  {
					player.closeInventory();
					main.getPerksManager().randomPerk(player);
					break;
				}
			case STAINED_GLASS_PANE:
				break;
			default:
				main.getSkyDataHandler().selectPerk(player, main.getPerksManager().iconToPerk(it));
				player.closeInventory();
				break;
		}
		
	}

	@Override
	public void onRightClick(Player arg0, Inventory arg1, ItemStack arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	
	
}
