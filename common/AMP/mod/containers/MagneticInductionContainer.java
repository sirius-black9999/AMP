package AMP.mod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class MagneticInductionContainer extends Container
{
	private EntityPlayer player;
    private IInventory chest;
    
    public MagneticInductionContainer(IInventory playerInventory, IInventory chestInventory)
    {
        chest = chestInventory;
        player = ((InventoryPlayer) playerInventory).player;
        chestInventory.openChest();
        layoutContainer(playerInventory, chestInventory);
    }
    

    public EntityPlayer getPlayer()
    {
        return player;
    }
    

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        chest.closeChest();
    }

    protected void layoutContainer(IInventory playerInventory, IInventory chestInventory)
    {
    	System.out.println("-------------layoutContainer---------------------");
    	System.out.println(this.inventorySlots.size());
        int leftCol = (168 - 162) / 2 + 1;
        addSlotToContainer(new Slot(chest, 0, 52, 16));
        addSlotToContainer(new Slot(chest, 1, 112, 17));
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, 148 - (4 - playerInvRow) * 18 - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            addSlotToContainer(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, 148 - 24));
        }
        System.out.println(this.inventorySlots.size());
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return chest.isUseableByPlayer(player);
    }
    
    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 1)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                	System.out.println("merged slot "+par2+" with 2-38 succesfully");
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 1 && par2 != 0)
            {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                    	System.out.println("merged slot "+par2+" with 0-1 succesfully");
                        return null;
                    }
                }
                else if (par2 >= 3 && par2 < 29)
                {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false))
                    {
                    	System.out.println("merged slot "+par2+" with 29-38 succesfully");
                        return null;
                    }
                }
                else if (par2 >= 29 && par2 < 38 && !this.mergeItemStack(itemstack1, 2, 29, false))
                {
                	System.out.println("merged slot "+par2+" with 2-29 succesfully");
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 2, 38, false))
            {
            	System.out.println("merged slot "+par2+" with 2-38 succesfully");
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

}
