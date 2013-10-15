package AMP.mod.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import AMP.mod.GUI.MagFurnaceGui;
import AMP.mod.blocks.BlockMagneticConductor;
import AMP.mod.entry.AMPMod;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
        
        @Override
        public void registerRenderers() {
                // This is for rendering entities and so forth later on
        }
        

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
        {
        	TileEntity te = null;
        		te = world.getBlockTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityMagneticInductionFurnace)
            {
                return MagFurnaceGui.buildGUI(player.inventory, (TileEntityMagneticInductionFurnace) te);
            }
            else
            {
                return null;
            }
        }

        @Override
        public World getClientWorld()
        {
            return FMLClientHandler.instance().getClient().theWorld;
        }
}