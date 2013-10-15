/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package AMP.mod.core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import AMP.mod.entry.AMPMod;
import AMP.mod.tileentities.TileEntityMagnetic;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
    {
        ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
        int x = dat.readInt();
        int y = dat.readInt();
        int z = dat.readInt();
        float gauss = dat.readFloat();
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
        TileEntityMagnetic icte = (TileEntityMagnetic) te;
        icte.handlePacketData(gauss, items);
    }
	public static Packet getPacket(TileEntityMagneticInductionFurnace clientTileEntity) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = clientTileEntity.xCoord;
		int y = clientTileEntity.yCoord;
		int z = clientTileEntity.zCoord;
		float gauss = ((TileEntityMagnetic)clientTileEntity).gauss;
		int[] items = clientTileEntity.buildIntDataList();
		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeFloat(gauss);
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
		
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "AMP";
		pkt.data = bos.toByteArray();
		//System.out.println("sending : "+Arrays.toString(pkt.data));
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		return pkt;
	}
	public static Packet getPacketMagnetic(TileEntityMagnetic tileEntityMagnetic) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tileEntityMagnetic.xCoord;
		int y = tileEntityMagnetic.yCoord;
		int z = tileEntityMagnetic.zCoord;
		float gauss = tileEntityMagnetic.gauss;
		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeFloat(gauss);
			dos.writeByte(0);
		}
		catch (IOException e)
		{
          // UNPOSSIBLE?
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "AMP";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		return pkt;
	}
}
