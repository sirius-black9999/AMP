package AMP.mod.containers;

import java.util.HashMap;
import java.util.Map.Entry;

import AMP.mod.core.PacketHandler;
import AMP.mod.core.worldgen.WorldgenMonitor;
import AMP.mod.entry.AMPMod;
import AMP.mod.tileentities.TileEntityWorldgenRegenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

public class WorldgenRegeneratorContainer extends Container implements IFluidTank
{
	private EntityPlayer player;
    public IInventory chest;
    private FluidStack tank;
    private FluidTankInfo tankInfo;
    
    public WorldgenRegeneratorContainer(IInventory playerInventory, IInventory chestInventory)
    {
    	tankInfo = new FluidTankInfo(this);
    	tank = new FluidStack(AMPMod.fluidLiquidWorldgen, 0);
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
    	inventorySlots.clear();
    	System.out.println("-------------layoutContainer---------------------"+player.worldObj.isRemote);
    	System.out.println(this.inventorySlots.size());
        int leftCol = (168 - 162) / 2 + 1;
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
        

        addSlotToContainer(new Slot(chest, 0, 150, 16));
        for(int row = 0; row < 4; row++)
        {
        	for(int col = 0; col < 5; col++)
        	{
        		addSlotToContainer(new Slot(chest, row*5+col+1, 35+(18*col), -10+18*row));
        	}
        }
        ((TileEntityWorldgenRegenerator)chest).fillItemSlots();//fillItemSlots(37,56, (TileEntityWorldgenRegenerator)chest);
        System.out.println(this.inventorySlots.size());
    }
//    @SuppressWarnings("unchecked")
//	public void fillItemSlots(int start, int end, TileEntityWorldgenRegenerator inv) {
//    	
//    	int pageNum = inv.selectedPageNum;
//		Integer[] entries = WorldgenMonitor.getAllUniqueBlockIds();
//    	for(int i = 0; i < end-start+1; i++)
//		{
//    		
//    		int entry = 20*pageNum+i;
//    		Slot target = (Slot) inventorySlots.get(start+i);
//    		if(entry < entries.length)
//    		{
//    			System.out.println("putting stack "+entries[entry]+" page "+entry+"/"+entries.length);
//				target.inventory.setInventorySlotContents(i+1, new ItemStack(Item.itemsList[entries[entry]]));
//    		}
//    		else
//    		{
//    			System.out.println("clearing stack " + entry);
//    			target.inventory.setInventorySlotContents(i+1, null);
//    		}
//		}
//    	//detectAndSendChanges();
//    	//inv.onInventoryChanged();
//    	//PacketHandler.SendPacketClientToServer(inv);
//	}

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

            if (par2 == 36)
            {
                if (!this.mergeItemStack(itemstack1, 0, 36, true))
                {
                	System.out.println("merged slot "+par2+" with 2-38 succesfully");
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 < 36)
            {
                if (par2 >= 0 && par2 < 27)
                {
                    if (!this.mergeItemStack(itemstack1, 27, 36, false))
                    {
                    	System.out.println("merged slot "+par2+" with 29-38 succesfully");
                        return null;
                    }
                }
                else if (par2 >= 27 && par2 < 36 && !this.mergeItemStack(itemstack1, 0, 27, false))
                {
                	System.out.println("merged slot "+par2+" with 2-29 succesfully");
                    return null;
                }
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


	@Override
	public FluidStack getFluid() {
		return tank;
	}


	@Override
	public int getFluidAmount() {
		return tank.amount;
	}


	@Override
	public int getCapacity() {
		return 4000;
	}


	@Override
	public FluidTankInfo getInfo() {
		return tankInfo;
	}


	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if(doFill)
		{
			if(tank.isFluidEqual(resource))
			{
				if(tank.amount + resource.amount < 4000)
				{
					tank.amount += resource.amount;
					return resource.amount;
				}
				else
				{
					int difference = 4000-tank.amount;
					tank.amount = 4000;
					return resource.amount-difference;
				}
			}
		}
		return 0;
	}


	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if(maxDrain > tank.amount)
		{
			int tankAmount = tank.amount;
			tank.amount = 0;
			return new FluidStack(AMPMod.fluidLiquidWorldgen, tankAmount);
		}
		else
		{
			tank.amount -= maxDrain;
			return new FluidStack(AMPMod.fluidLiquidWorldgen, maxDrain);
		}
	}

	public void redoLayout() {
		layoutContainer(player.inventory, chest);
		
	}

}
