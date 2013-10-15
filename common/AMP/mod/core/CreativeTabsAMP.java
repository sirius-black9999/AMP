package AMP.mod.core;

import AMP.mod.blocks.BlockRedCell;
import AMP.mod.entry.AMPMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class CreativeTabsAMP extends CreativeTabs {

	public static CreativeTabsAMP instance = new CreativeTabsAMP("Aerial Mage Prototype");
	public CreativeTabsAMP(String label) {
		super(label);
	}

	@SideOnly(Side.CLIENT)

	@Override
    public ItemStack getIconItemStack() {
		return new ItemStack(AMPMod.blockRedCellV2, 0, 2);
	}
	
	@Override
    public String getTranslatedTabLabel() {
		return "Aerial mage Prototype";
	}
}
