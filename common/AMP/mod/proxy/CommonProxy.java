package AMP.mod.proxy;

import AMP.mod.containers.MagneticInductionContainer;
import AMP.mod.containers.WorldgenLiquifierContainer;
import AMP.mod.containers.WorldgenRegeneratorContainer;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;
import AMP.mod.tileentities.TileEntityWorldgenLiquifier;
import AMP.mod.tileentities.TileEntityWorldgenRegenerator;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class CommonProxy implements IGuiHandler{

    // Client stuff
    public void registerRenderers() {
            // Nothing here as the server doesn't render graphics or entities!
    }


    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z)
    {
        TileEntity te = world.getBlockTileEntity(X, Y, Z);
        if (te != null && te instanceof TileEntityMagneticInductionFurnace)
        {
        	TileEntityMagneticInductionFurnace icte = (TileEntityMagneticInductionFurnace) te;
            return new MagneticInductionContainer(player.inventory, icte);
        }
        else if (te != null && te instanceof TileEntityWorldgenLiquifier)
        {
        	TileEntityWorldgenLiquifier icte = (TileEntityWorldgenLiquifier) te;
            return new WorldgenLiquifierContainer(player.inventory, icte);
        }
        else if (te != null && te instanceof TileEntityWorldgenRegenerator)
        {
        	TileEntityWorldgenRegenerator icte = (TileEntityWorldgenRegenerator) te;
            return new WorldgenRegeneratorContainer(player.inventory, icte);
        }
        else
        {
            return null;
        }
    }


    public World getClientWorld()
    {
        return null;
    }
}