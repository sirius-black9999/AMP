package AMP.mod.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import AMP.mod.core.PacketHandler;

public class TileEntityMagnetic extends TileEntity {

	public float gauss = 0;
	public float totalGauss = 0;
	private static int GUID = 0;
	public TileEntityMagnetic() {
		addMapping(this.getClass(), "TEMagnetic"+GUID++);
	}
	
	public void adjacentWires(int activeWires)
	{
		gauss += activeWires;
	}
	@Override
	public int getBlockMetadata()
	{		
		return (gauss<10?0:gauss<100?1:gauss<1000?2:3)*5;
	};
	public void adjacentMagnets(TileEntityMagnetic neighbor)
	{
		float ref = this.gauss*0.9f;
		neighbor.gauss += ref;
		this.gauss -= ref;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		par1nbtTagCompound.setFloat("Gauss", gauss);
		par1nbtTagCompound.setFloat("MaxGauss", totalGauss);
		
		super.writeToNBT(par1nbtTagCompound);
	}
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		
		super.readFromNBT(par1nbtTagCompound);
		gauss = par1nbtTagCompound.getFloat("Gauss");
	}

	public void dieOff() {	

		totalGauss += this.gauss;
		this.gauss = this.gauss-(0.1f*this.gauss+1);
		if(this.gauss < 0)
			this.gauss = 0;
		//worldObj.markBlocksDirtyVertical(xCoord, yCoord, zCoord, blockType.blockID);
	}

	public void handlePacketData(float gauss2) {
		this.gauss = gauss2;
		
	}
	@Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.getPacket(this);
    }
}
