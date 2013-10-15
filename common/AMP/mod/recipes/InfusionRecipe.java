package AMP.mod.recipes;

import AMP.mod.entry.AMPMod;
import AMP.mod.items.ItemBloodyStone;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class InfusionRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {

		boolean hasBloodyStone = false;
		boolean hasRottenFlesh = false;
		boolean reject = false;
		int bloodyStoneVal = 0;
		int rottenFleshVal = 0;
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
			{
				ItemStack invSlot = inventorycrafting.getStackInRowAndColumn(i, j);
				if(invSlot != null)
				{
					if(invSlot.getItem().itemID == AMPMod.itemBloodyStone.itemID && hasBloodyStone == false)
					{
						hasBloodyStone = true;
						bloodyStoneVal = invSlot.getItemDamage();
					}
					else if(invSlot.getItem().itemID == Item.rottenFlesh.itemID)
					{
						hasRottenFlesh = true;
						rottenFleshVal++;
					}
					else
					{
						reject = true;
					}
				}
			}
		return hasBloodyStone && hasRottenFlesh && bloodyStoneVal-rottenFleshVal >= 0 && !reject;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		int bloodyStoneVal = 0;
		int rottenFleshVal = 0;
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				{
					ItemStack invSlot = inventorycrafting.getStackInRowAndColumn(i, j);
					if(invSlot != null)
					{
						if(invSlot.getItem().itemID == AMPMod.itemBloodyStone.itemID)
							bloodyStoneVal = invSlot.getItemDamage();
						if(invSlot.getItem().itemID == Item.rottenFlesh.itemID)
							rottenFleshVal++;
					}
				}
		if(bloodyStoneVal - rottenFleshVal > 0)
			return new ItemStack(AMPMod.itemBloodyStone, 1, bloodyStoneVal - rottenFleshVal);
		else
			return new ItemStack(AMPMod.itemBloodyStone, 1, 0);
	}

	@Override
	public int getRecipeSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getRecipeOutput() {
		// TODO Auto-generated method stub
		return null;
	}

}
