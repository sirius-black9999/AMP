package AMP.mod.tileentities;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import AMP.mod.core.PacketHandler;
import AMP.mod.core.worldgen.WorldgenMonitor;
import AMP.mod.entry.AMPMod;

public class TileEntityWorldgenRegenerator extends TileEntityMagnetic implements ISidedInventory, IFluidHandler {
    private static final int[] slots_top = new int[] {0};
    private static final int[] slots_bottom = new int[] {1};
	private ItemStack[] furnaceItemStacks = new ItemStack[21];
    public FluidStack tank;
    private FluidTankInfo[] tankInfo = new FluidTankInfo[]{new FluidTankInfo(tank, 4000)};
    public int selectedItemId = 1;
    public int selectedPageNum = 0;
	int cooldown;
	public int accuValue = 0;
	public int selectedSlot;
    
	public TileEntityWorldgenRegenerator() {
		tank = new FluidStack(AMPMod.fluidLiquidWorldgen, 0);
	}
	@Override
	public boolean canUpdate() {
		return true;
	}
	public void adjacentWires(int activeWires)
	{
	}
	@Override
	public int getBlockMetadata()
	{		
		return 0;
	};
	public void adjacentMagnets(TileEntityWorldgenRegenerator neighbor)
	{
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) 
	{
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < furnaceItemStacks.length; i++)
        {
            if (furnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        
        tag.setTag("Items", nbttaglist);
		super.writeToNBT(tag);
	}
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		
		super.readFromNBT(tag);
		NBTTagList nbttaglist = tag.getTagList("Items");
        furnaceItemStacks = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < furnaceItemStacks.length)
            {
                furnaceItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
	}
	/**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        boolean flag1 = false;
        if (!this.worldObj.isRemote)
        {
        	if(this.gauss > 72000)
        		this.gauss = 72000;
			if(cooldown <= 0)
			{
				if(gauss == 1000 && tank.amount >= 100)
				{
					tank.amount -= 100;
					accuValue += 10;
				}
				if(accuValue > WorldgenMonitor.getItemRarity(new ItemStack(Block.blocksList[selectedItemId]), worldObj, xCoord, zCoord))
				{
					smeltItem();
				}
			}
			else
			{
				cooldown--;
			}
        }
        
        if (flag1)
        {
            this.onInventoryChanged();
        }
        dieOff();
    }
	@Override
	public void dieOff() 
	{
		totalGauss += this.gauss;
		this.gauss -= 100+this.gauss/300;
		if(this.gauss < 0)
			this.gauss = 0;
		//worldObj.markBlocksDirtyVertical(xCoord, yCoord, zCoord, blockType.blockID);
	};
	 /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
    	furnaceItemStacks[0] = new ItemStack(Block.blocksList[selectedItemId]);
    	accuValue = 0;
    }

	@Override
	public int getSizeInventory() {
		return furnaceItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return furnaceItemStacks[i];
	}

	/**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack itemstack;

            if (this.furnaceItemStacks[par1].stackSize <= par2)
            {
                itemstack = this.furnaceItemStacks[par1];
                this.furnaceItemStacks[par1] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.furnaceItemStacks[par1].splitStack(par2);

                if (this.furnaceItemStacks[par1].stackSize == 0)
                {
                    this.furnaceItemStacks[par1] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack itemstack = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }


	@Override
	public String getInvName() {
		return "worldgen regenerator";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	/**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return par1 == 2 ? false:true;//par1 == 2 ? false : (par1 == 1 ? isItemFuel(par2ItemStack) : true);
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int par1)
    {
        return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_bottom);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(par1, par2ItemStack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
    }
    
    public int[] buildIntDataList()
    {
        int[] sortList = new int[furnaceItemStacks.length * 3];
        int pos = 0;
        for (ItemStack is : furnaceItemStacks)
        {
            if (is != null)
            {
                sortList[pos++] = is.itemID;
                sortList[pos++] = is.getItemDamage();
                sortList[pos++] = is.stackSize;
            }
            else
            {
                sortList[pos++] = 0;
                sortList[pos++] = 0;
                sortList[pos++] = 0;
            }
        }
        return sortList;
    }
    
    public void handlePacketData(float gauss,int pageNum, int itemId, int phase, int fluidAmount, int[] intData)
    {
    	//System.out.println("handling packet data for "+gauss+ " / "+Arrays.toString(intData));
        TileEntityWorldgenRegenerator chest = this;
        this.gauss = gauss;
        this.tank.amount = fluidAmount;
        this.accuValue = phase;
        if(FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
	        this.selectedItemId = itemId;
	        this.selectedPageNum = pageNum;
	        System.out.println("received pagenum "+pageNum+" on "+FMLCommonHandler.instance().getEffectiveSide().toString()+" side");
        }
        if (intData != null)
        {
            int pos = 0;
            for (int i = 0; i < chest.furnaceItemStacks.length; i++)
            {
            	if(intData.length > pos+2)
            	{
	                if (intData[pos + 2] != 0)
	                {
	                    ItemStack is = new ItemStack(intData[pos], intData[pos + 2], intData[pos + 1]);
	                    chest.furnaceItemStacks[i] = is;
	                }
	                else
	                {
	                    chest.furnaceItemStacks[i] = null;
	                }
            	}
                pos += 3;
            }
        }
        
    }
    @Override
    public Packet getDescriptionPacket()
    {
    	//System.out.println("getting description packet for magnetic induction furnace");
        return PacketHandler.getPacket(this);
    }
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(doFill && resource.isFluidEqual(tank))
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
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(resource.isFluidEqual(tank))
		{
		if(resource.amount > tank.amount)
			{
				int tankAmount = tank.amount;
				tank.amount = 0;
				return new FluidStack(AMPMod.fluidLiquidWorldgen, tankAmount);
			}
			else
			{
				tank.amount -= resource.amount;
				return new FluidStack(AMPMod.fluidLiquidWorldgen, resource.amount);
			}
		}
		return null;
	}
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
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
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid.getID() == tank.fluidID;
	}
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return fluid.getID() == tank.fluidID;
	}
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return tankInfo;
	}
}
