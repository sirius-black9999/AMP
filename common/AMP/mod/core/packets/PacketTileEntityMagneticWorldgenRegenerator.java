package AMP.mod.core.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import AMP.mod.core.BasePacket;
import AMP.mod.entry.AMPMod;
import AMP.mod.tileentities.TileEntityMagnetic;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;
import AMP.mod.tileentities.TileEntityWorldgenLiquifier;
import AMP.mod.tileentities.TileEntityWorldgenRegenerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTileEntityMagneticWorldgenRegenerator extends BasePacket {

	public PacketTileEntityMagneticWorldgenRegenerator() {
		ID = 4;
	}
	@Override
	public boolean appliesTo(Object ent) {
			if(ent.getClass() == TileEntityWorldgenRegenerator.class)
				return true;
				return false;
	}
	@Override
	public byte[] convertData(Object ent) {
		TileEntityWorldgenRegenerator entity = (TileEntityWorldgenRegenerator)ent;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = entity.xCoord;
		int y = entity.yCoord;
		int z = entity.zCoord;
		float gauss = entity.gauss;
		int fluidAmount = entity.tank.amount;
		int phase = entity.accuValue;
		int pageNum = entity.selectedPageNum;
		int itemID = entity.selectedItemId;
		int[] items = entity.buildIntDataList();
		try
		{
			dos.writeByte(ID);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeFloat(gauss);
			dos.writeInt(phase);
			dos.writeInt(fluidAmount);
			dos.writeInt(pageNum);
			dos.writeInt(itemID);
			dos.writeByte(items.length);
			if (items.length > 0)
			{
				for (int i = 0; i < items.length; i++)
				{
					dos.writeInt(items[i]);
				}
			}
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

        int phase = dat.readInt();
        int fluidAmount = dat.readInt();
        int pageNum = dat.readInt();
        int itemID = dat.readInt();
        byte hasStacks = dat.readByte();
        int[] items = new int[0];
        if (hasStacks > 0)
        {
            items = new int[hasStacks];
            for (int i = 0; i < items.length; i++)
            {
                items[i] = dat.readInt();
            }
        }
        
        World world = AMPMod.proxy.getClientWorld();
        TileEntity te = world.getBlockTileEntity(x, y, z);
        TileEntityWorldgenRegenerator icte = (TileEntityWorldgenRegenerator) te;
        icte.handlePacketData(gauss, pageNum, itemID, phase, fluidAmount, items);
	}
}
