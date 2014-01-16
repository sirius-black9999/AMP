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
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import AMP.mod.entry.AMPMod;
import AMP.mod.statics.ClassFinder;
import AMP.mod.tileentities.TileEntityMagnetic;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;
import AMP.mod.tileentities.TileEntityWorldgenLiquifier;
import AMP.mod.tileentities.TileEntityWorldgenRegenerator;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	static HashMap<Byte, BasePacket> packets;
	public PacketHandler()
	{
		packets = new HashMap<Byte, BasePacket>();
		Set<Class<?>> classes;
		try {
			classes = ClassFinder.getClasses("AMP.mod.core.packets");
		
			BasePacket[] packs = new BasePacket[classes.size()];
			int counter = 0;
			for(Class<?> c : classes)
			{
					packs[counter++] = (BasePacket) c.newInstance();
			}
			for(BasePacket pack : packs)
			{
				if(pack != null)
				packets.put(pack.ID, pack);
			}
			System.out.println("succesfully listed "+packets.size()+" classes");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
    {
		
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		byte id = dat.readByte();
        BasePacket target = packets.get(id);
        if(target != null)
        {
        	target.parseData(packet.data);
        }
        else
        {
        	System.out.println("no target found for id: "+id);
        }
        
    }
	public static Packet getPacket(Object ent) {
		BasePacket target = null;
		//System.out.println("comparing "+packets.size()+" packet types");
		for(Entry<Byte, BasePacket> entry : packets.entrySet())
		{	
			if(entry.getValue().appliesTo(ent))
			{
				target = entry.getValue();
				break;
			}
		}
		if(target != null)
		{
			Packet250CustomPayload pkt = new Packet250CustomPayload();
			pkt.channel = "AMP";
			pkt.data = target.convertData(ent);
			pkt.length = pkt.data.length;
			pkt.isChunkDataPacket = true;
			return pkt;
		}
		else
		{
			System.out.println("please write a packet handler for "+ent.toString());
			return null;
		}
	}
	public static void SendPacketServerToAllClients(Packet p)
	{
		PacketDispatcher.sendPacketToAllPlayers(p);
	}
	public static void SendPacketServerToAllClients(Object o)
	{
		PacketDispatcher.sendPacketToAllPlayers(getPacket(o));
	}
	public static void SendPacketServerToClient(Packet p, Player player)
	{
		PacketDispatcher.sendPacketToPlayer(p, player);
	}
	public static void SendPacketServerToClient(Object o, Player player)
	{
		PacketDispatcher.sendPacketToPlayer(getPacket(o), player);
	}
	public static void SendPacketServerToClient(Packet p, int dimID)
	{
		PacketDispatcher.sendPacketToAllInDimension(p, dimID);
	}
	public static void SendPacketServerToClient(Object o, int dimID)
	{
		PacketDispatcher.sendPacketToAllInDimension(getPacket(o), dimID);
	}
	public static void SendPacketClientToServer(Packet p)
	{
		PacketDispatcher.sendPacketToServer(p);
	}
	public static void SendPacketClientToServer(Object o)
	{
		System.out.println("sending "+o.toString()+" to server");
		PacketDispatcher.sendPacketToServer(getPacket(o));
	}
}
