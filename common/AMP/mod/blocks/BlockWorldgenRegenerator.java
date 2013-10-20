package AMP.mod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import AMP.mod.abstracts.BlockMagnetic;
import AMP.mod.entry.AMPMod;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;
import AMP.mod.tileentities.TileEntityWorldgenLiquifier;
import AMP.mod.tileentities.TileEntityWorldgenRegenerator;

public class BlockWorldgenRegenerator extends BlockMagnetic {
	

	public BlockWorldgenRegenerator(int par1, Material par2Material) {
		super(par1, par2Material);
		
	}
	
	@Override
	public void onNeighborBlockChange(World World, int x, int y, int z, int blockID) {
		World.markBlockForUpdate(x, y, z);
	}
	/**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
        	TileEntityWorldgenRegenerator tileentityfurnace = (TileEntityWorldgenRegenerator)par1World.getBlockTileEntity(par2, par3, par4);
        	
            if (tileentityfurnace != null)
            {
                par5EntityPlayer.openGui(AMPMod.instance, 0, par1World, par2, par3, par4);
            }

            return true;
        }
    }
    
    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
    }
	

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return AMPMod.blockWorldgenRegenerator.blockID;
    }
    
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityWorldgenRegenerator();
	}
}
