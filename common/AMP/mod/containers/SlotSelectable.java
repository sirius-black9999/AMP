package AMP.mod.containers;

import AMP.mod.tileentities.TileEntityWorldgenRegenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSelectable extends Slot {

	public SlotSelectable(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}
	@Override
	public void putStack(ItemStack par1ItemStack) {
		return;
	}
	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return false;
	}
	@Override
	public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return;
	}
	@Override
	public void onSlotChanged() {
		return;
	}
	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer,
			ItemStack par2ItemStack) {
		return;
	}
	
}
