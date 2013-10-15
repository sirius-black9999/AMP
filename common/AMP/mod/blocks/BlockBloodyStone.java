package AMP.mod.blocks;

import java.util.Random;

import AMP.mod.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockBloodyStone extends Block {


    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;
	public BlockBloodyStone(int par1, Material par2Material) {
		super(par1, par2Material);
		setTickRandomly(true);
	}
	@Override
	public void updateTick(World par1World, int x, int y, int z,
			Random par5Random) {

    	int meta = par1World.getBlockMetadata(x, y, z);
    	if(meta+1 < 8)
    	{
    		par1World.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    	}
    	else
    	{
    		par1World.setBlock(x, y, z, Block.blockRedstone.blockID);
    	}
		super.updateTick(par1World, x, y, z, par5Random);
	}
	@Override
	public Icon getIcon(int par1, int par2) 
	{
	
		return iconArray[par2];
	};

    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        iconArray = new Icon[8];
        for (int i = 0; i < this.iconArray.length; ++i)
        {
            iconArray[i] = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName() + "_" + i);
        }
    }
}
