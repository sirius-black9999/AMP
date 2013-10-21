package AMP.mod.core.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import AMP.mod.core.BasePacket;
import AMP.mod.entry.AMPMod;
import AMP.mod.tileentities.TileEntityMagnetic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketMagneticTileEntity extends BasePacket {

	public PacketMagneticTileEntity() {
		ID = 1;
	}
	@Override
	public boolean appliesTo(Object ent) {
		if(ent.getClass() == TileEntityMagnetic.class)
			return true;
			return false;
	}
	@Override
	public byte[] convertData(Object ent) {
		TileEntityMagnetic entity = (TileEntityMagnetic)ent;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = entity.xCoord;
		int y = entity.yCoord;
		int z = entity.zCoord;
		float gauss = entity.gauss;
		try
		{
			dos.writeByte(ID);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeFloat(gauss);
			dos.flush();
			//System.out.println("sending packet at "+gauss+" / "+Arrays.toString(items));	
		}
		catch (IOException e)
		{
          // UNPOSSIBLE?
		}
		return bos.toByteArray();
	}
	@Override
	public void parseData(byte[] packetData) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packetData);
		dat.readByte();
        int x = dat.readInt();
        int y = dat.readInt();
        int z = dat.readInt();
        float gauss = dat.readFloat();
        World world = AMPMod.proxy.getClientWorld();
        TileEntity te = world.getBlockTileEntity(x, y, z);
        TileEntityMagnetic icte = (TileEntityMagnetic) te;
        icte.handlePacketData(gauss);
	}
}
