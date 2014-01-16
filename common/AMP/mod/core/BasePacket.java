package AMP.mod.core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.tileentity.TileEntity;

public class BasePacket {

	public Byte ID = 0;
	public void parseData(byte[] packetData)
	{
		
	}
	public byte[] convertData(Object ent)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(ID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	public boolean appliesTo(Object ent)
	{
		return false;
	}
}
