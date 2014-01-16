package AMP.mod.abstracts;

import AMP.mod.entry.AMPMod;
import AMP.mod.interfaces.IToolGaussMeter;
import AMP.mod.tileentities.TileEntityMagnetic;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMagnetic extends BlockContainer {

	protected BlockMagnetic(int par1, Material par2Material) {
		super(par1, par2Material);
		// TODO Auto-generated constructor stub
	}
	public boolean blockActivated(EntityPlayer entityplayer) {
		Item equipped = entityplayer.getCurrentEquippedItem() != null ? entityplayer.getCurrentEquippedItem().getItem() : null;
		
		if (equipped instanceof IToolGaussMeter && ((IToolGaussMeter) equipped).canMeasure(entityplayer)) {
			((IToolGaussMeter) equipped).MeterUsed(entityplayer);
			return true;
		}
		return false;
	}
	protected void applyMagneticTransmission(World world, int x, int y, int z) {
		TileEntity te = null;
		switch(world.getBlockMetadata(x, y, z)%6)
		{
		case 0:
			te = world.getBlockTileEntity(x, y-1, z);
			break;
		case 1:
			te = world.getBlockTileEntity(x, y+1, z);
			break;
		case 2:
			te = world.getBlockTileEntity(x, y, z-1);
			break;
		case 3:
			te = world.getBlockTileEntity(x, y, z+1);
			break;
		case 4:
			te = world.getBlockTileEntity(x-1, y, z);
			break;
		case 5:
			te = world.getBlockTileEntity(x+1, y, z);
			break;
		}
		//System.out.println("calling "+world.getBlockMetadata(x, y, z)%6+": "+TE);
		//System.out.println("from: "+world.getBlockTileEntity(x, y, z));
		if(te != null && te instanceof TileEntityMagnetic)
		{
			((TileEntityMagnetic) world.getBlockTileEntity(x, y, z)).adjacentMagnets((TileEntityMagnetic)te);
		}
		else
		{
			((TileEntityMagnetic) world.getBlockTileEntity(x, y, z)).dieOff();
		}
	}


	protected boolean BlockIsActiveCell(World world, int x, int y, int z) {
		if(world.getBlockId(x, y, z) == AMPMod.blockRedCellV2.blockID)
			if(world.getBlockMetadata(x, y, z)%5 == 2 || world.getBlockMetadata(x, y, z)%5 == 3)
				return true;
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return createTileEntity(world, 0);
	};

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityMagnetic();
	}
}
