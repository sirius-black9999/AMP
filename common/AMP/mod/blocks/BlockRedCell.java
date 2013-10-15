package AMP.mod.blocks;

import java.util.ArrayList;
import java.util.List;

import codechicken.multipart.BlockMultipart;
import paulscode.sound.Vector3D;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import AMP.mod.core.CreativeTabsAMP;
import AMP.mod.core.Reference;
import AMP.mod.entry.AMPMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraftforge.common.ForgeDirection;

public class BlockRedCell extends Block{

    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;
	public BlockRedCell(int par1) {
		super(par1, Material.circuits);
		setCreativeTab(CreativeTabsAMP.instance);
	}
	

    @SideOnly(Side.CLIENT)	

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int metadata)
    {
        return this.iconArray[metadata % this.iconArray.length];
    }
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int metadata)
    {
        return (metadata/5)*5;
    }


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) 
	{
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 5));
		par3List.add(new ItemStack(this, 1, 10));
	}
    
    @SideOnly(Side.CLIENT)
    String[] iconNames = new String[]{"inactive", "pre_active", "active", "pre_cooldown", "cooldown"};
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.iconArray = new Icon[5];

        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName() + "_" + iconNames[i]);
        }
    }
    public int tickRate()
    {
    	return 1;
    }
    @Override
	    public void updateTick(World par1World, int x, int y, int z, java.util.Random par5Random)
	    {
    	
    	int meta = par1World.getBlockMetadata(x, y, z)%5;
    	if(meta < 4)
    	{
			par1World.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
			doBlockUpdate(par1World, x, y, z);
			par1World.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(), -1);
    	}
    	else
    	{
    		if(!par1World.isBlockIndirectlyGettingPowered(x, y, z))
    		{
    			par1World.setBlockMetadataWithNotify(x, y, z, damageDropped(meta), 2);
    			doBlockUpdate(par1World, x, y, z);
			}
			else
				par1World.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(), -1);
    	}
    };

  @Override
  public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {return true;};
    
    @Override
    public void onNeighborBlockChange(World par1World, int x, int y, int z, int otherID) {
    	if(par1World.getBlockMetadata(x, y, z)%5 == 0)
    	{
    		if(par1World.isBlockIndirectlyGettingPowered(x, y, z))
    		{	
    			par1World.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(), -1);
    		}
    		if(checkOtherBlocks(par1World, x,y,z,otherID)) 
    			if(otherIDIsCooldown(par1World, x,y,z,otherID))
    			{
	    			par1World.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(), -1);
    			}
    	}
    }


	private void doBlockUpdate(World par1World, int par2, int par3, int par4) {
	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3, par4, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3, par4, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3, par4 + 1, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3, par4 - 1, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3 - 1, par4, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3 + 1, par4, this.blockID);	
  	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3+1, par4, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3+1, par4, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3+1, par4 + 1, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3+1, par4 - 1, this.blockID);
  	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3-1, par4, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3-1, par4, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3-1, par4 + 1, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2, par3-1, par4 - 1, this.blockID);
  	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3, par4+1, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2 + 1, par3, par4-1, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3, par4 + 1, this.blockID);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3, par4 - 1, this.blockID);
	}
	private boolean checkOtherBlocks(World World, int x, int y, int z, int otherID) {
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				for(int k = -1; k < 2; k++)
					if((i==0 || j == 0 || k == 0) && !(i==0 && j==0 && k==0))
						if(World.getBlockId(x+i, y+j, z+k) == this.blockID)
							if(World.getBlockMetadata(x+i, y+j, z+k) == 2)
								return true;
		return false;
	}
	private boolean otherIDIsCooldown(World World, int x, int y, int z, int otherID) {
		int active = 0;
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				for(int k = -1; k < 2; k++)
					if((i==0 || j == 0 || k == 0) && !(i==0 && j==0 && k==0))
						if(World.getBlockId(x+i, y+j, z+k) == this.blockID)
						{
							if(World.getBlockMetadata(x+i, y+j, z+k) == 2 || World.getBlockMetadata(x+i, y+j, z+k) == 1)
								active++;
							if(World.getBlockMetadata(x+i, y+j, z+k) == 3 )
								active = 10;
						}
		return active > 0 && active < 3;
	}
}
 