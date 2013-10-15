package AMP.mod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import AMP.mod.abstracts.BlockMagnetic;
import AMP.mod.core.Reference;
import AMP.mod.entry.AMPMod;
import AMP.mod.tileentities.TileEntityMagnetic;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMagneticConductor extends BlockMagnetic{

    private Icon[] icons;
	public BlockMagneticConductor(int par1, Material par2Material) {
		super(par1, par2Material);
	}
	
	@Override
	public void updateTick(World World, int x, int y, int z, java.util.Random Random)
	{
		
		TileEntityMagnetic TE = (TileEntityMagnetic)World.getBlockTileEntity(x, y, z);
		
		int meta = World.getBlockMetadata(x, y, z);
		if(meta < 6)
			World.setBlockMetadataWithNotify(x, y, z, meta+6, 3);
		else
			World.setBlockMetadataWithNotify(x, y, z, meta-6, 3);
		//System.out.println("updating");
		
		switch(World.getBlockMetadata(x, y, z)%6)
		{
		case 0:
			World.notifyBlockOfNeighborChange(x, y-1, z, blockID);
			break;
		case 1:
			World.notifyBlockOfNeighborChange(x, y+1, z, blockID);
			break;
		case 2:
			World.notifyBlockOfNeighborChange(x, y, z-1, blockID);
			break;
		case 3:
			World.notifyBlockOfNeighborChange(x, y, z+1, blockID);
			break;
		case 4:
			World.notifyBlockOfNeighborChange(x-1, y, z, blockID);
			break;
		case 5:
			World.notifyBlockOfNeighborChange(x+1, y, z, blockID);
			break;
		}
		//World.notifyBlockChange(x, y, z, blockID);
		//World.notifyBlocksOfNeighborChange(x, y, z, blockID);
	}
	@Override
	public void onNeighborBlockChange(World World, int x, int y, int z, int blockID) {
		//World.markBlockForUpdate(x, y, z);
		TileEntityMagnetic TE = (TileEntityMagnetic)World.getBlockTileEntity(x, y, z);
		int activeNeighbors = 0;
		if( BlockIsActiveCell(World, x-1, y, z) || 
			BlockIsActiveCell(World, x+1, y, z) ||
			BlockIsActiveCell(World, x, y-1, z) ||
			BlockIsActiveCell(World, x, y+1, z) ||
			BlockIsActiveCell(World, x, y, z-1) || 
			BlockIsActiveCell(World, x, y, z+1))
			activeNeighbors = 4;
		if(activeNeighbors > 0)
		{
			TE.adjacentWires(activeNeighbors);
		}
		applyMagneticTransmission(World, x, y, z);
		if(TE.gauss>0)
		{
			TE.dieOff();
			World.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, 4, -1);
		}
		else
		{
			if(TE.gauss < 0)
				TE.gauss = 0;
			if(World.getBlockMetadata(x,y,z) > 6)
				World.setBlockMetadataWithNotify(x, y, z, World.getBlockMetadata(x,y,z)-6, 3);
		}
			
		super.onNeighborBlockChange(World, x, y, z, blockID);
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, net.minecraft.entity.EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		int k = MathHelper.floor_double((double)(par5EntityLivingBase.rotationPitch * 3.0F / 360.0F) + 2.5D) & 3;
		switch(k)
			{
		case 1:
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
			return;
		case 2:
			switch(l)
			{
			case 0:
		        par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
		        return;
			case 1:
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
				return;
			case 2:
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
				return;
			case 3:
				par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
				return;
			}
			break;
		case 3:
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
			return;
			}
	}
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getBlockTexture(net.minecraft.world.IBlockAccess world, int x, int y, int z, int side) 
	{
		
		TileEntityMagnetic TE;
		/*if(w != null)
			TE = (TileEntityMagnetic) w.getBlockTileEntity(x, y, z);
		else
			TE = (TileEntityMagnetic) world.getBlockTileEntity(x, y, z);
			*/
		TE = (TileEntityMagnetic) world.getBlockTileEntity(x, y, z);
		//System.out.println("("+x+", "+y+", "+z+"): "+TE);
		if(TE != null)
		{
			int g = TE.getBlockMetadata();
			int meta;
				meta = world.getBlockMetadata(x, y, z);
			//System.out.println("Meta:"+meta);
			//System.out.println("g:"+g);
			switch(meta%6)
			{
			case 0:
				return side == 0 || side == 1 ? icons[4+g] : icons[2+g];
			case 1:
				return side == 0 || side == 1 ? icons[4+g] : icons[0+g];
			case 2:
				return side == 0 || side == 1 ? icons[0+g] : side == 2 || side == 3 ? icons[4+g] : side == 4 ? icons[3+g] :icons[1+g];
			case 3:
				return side == 0 || side == 1 ? icons[2+g] : side == 2 || side == 3 ? icons[4+g] : side == 4 ? icons[1+g] :icons[3+g];
			case 4:
				return side == 0 || side == 1 ? icons[3+g] : side == 2 ? icons[1+g] : side == 3 ? icons[3+g] : icons[4+g];
			case 5:
				return side == 0 || side == 1 ? icons[1+g] : side == 2 ? icons[3+g] : side == 3 ? icons[1+g] : icons[4+g];
			}
		}
		return super.getBlockTexture(world, x, y, z, side);
	}
	@Override 
	public Icon getIcon(int side, int meta)
	{
		switch(meta%6)
		{
			case 0:
				return side == 0 || side == 1 ? icons[4] : icons[2];
			case 1:
				return side == 0 || side == 1 ? icons[4] : icons[0];
			case 2:
				return side == 0 || side == 1 ? icons[0] : side == 2 || side == 3 ? icons[4] : side == 4 ? icons[3] :icons[1];
			case 3:
				return side == 0 || side == 1 ? icons[2] : side == 2 || side == 3 ? icons[4] : side == 4 ? icons[1] :icons[3];
			case 4:
				return side == 0 || side == 1 ? icons[3] : side == 2 ? icons[1] : side == 3 ? icons[3] : icons[4];
			case 5:
				return side == 0 || side == 1 ? icons[1] : side == 2 ? icons[3] : side == 3 ? icons[1] : icons[4];
		}
		return side == 0 || side == 1 ? icons[4] : icons[2];
	}

	String itemNames[] = new String[]{"magnet_sides_1","magnet_sides_2","magnet_sides_3","magnet_sides_4", "magnet_ends"
									, "magnet_sides_low_1","magnet_sides_low_2","magnet_sides_low_3","magnet_sides_low_4", "magnet_ends_low"
									, "magnet_sides_mid_1","magnet_sides_mid_2","magnet_sides_mid_3","magnet_sides_mid_4", "magnet_ends_mid"
									, "magnet_sides_high_1","magnet_sides_high_2","magnet_sides_high_3","magnet_sides_high_4", "magnet_ends_high"}; 
	@Override
	public void registerIcons(IconRegister par1IconRegister) {

		icons = new Icon[itemNames.length];
		int i = 0;
	    for (String csName : itemNames) 
	        icons[i++] = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + csName);
		super.registerIcons(par1IconRegister);
	}
	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z,
			ForgeDirection axis) {
		//System.out.println("block at ("+x+", "+y+", "+z+") rotated to "+(Integer.toBinaryString(axis.flag).length()-1));
		if(Integer.toBinaryString(axis.flag).length()-1 == worldObj.getBlockMetadata(x, y, z))
		{
			worldObj.setBlockToAir(x, y, z);
			dropBlockAsItem(worldObj, x, y, z, blockID, 0);
		}
		worldObj.setBlockMetadataWithNotify(x, y, z, Integer.toBinaryString(axis.flag).length()-1, 3);
		return true;//RotationHelper.rotateVanillaBlock(this, worldObj, x, y, z, axis);
	}
	@Override
	public boolean onBlockActivated(World World, int x, int y, int z, EntityPlayer Player, int side, float par7, float par8, float par9)
	{
		if(Player.isSneaking())
		{
			if(Player.getCurrentEquippedItem().getItem() instanceof IToolWrench)
			{
				rotateBlock(World, x,y,z, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[side]));
			}
		}
		return super.onBlockActivated(World, x, y, z, Player, side, par7, par8, par9);
	};

	
}
