package AMP.mod.blocks;

import AMP.mod.core.CreativeTabsAMP;
import AMP.mod.core.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMulticolorWool extends Block {

	
	private static Icon topIconA;
	private static Icon sideIconA;
	private static Icon bottomIconA;
	
	public BlockMulticolorWool(int par1, Material par2Material) {
		super(par1, par2Material);
		
	}



	@SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
			return par1 == 1 ? BlockMulticolorWool.topIconA : (par1 == 0 ? BlockMulticolorWool.bottomIconA : BlockMulticolorWool.sideIconA);
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    	//System.out.println(mod_AMP.modid + ":" + (this.getUnlocalizedName().substring(5)));
        BlockMulticolorWool.bottomIconA = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName()+"_bottom");
        BlockMulticolorWool.sideIconA = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName()+"_side");
        BlockMulticolorWool.topIconA = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName()+"_top");
    }
}
