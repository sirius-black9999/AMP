package AMP.mod.items;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import AMP.mod.core.CreativeTabsAMP;
import AMP.mod.core.Reference;
import AMP.mod.interfaces.IToolGaussMeter;
import AMP.mod.tileentities.TileEntityMagnetic;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat;
import net.minecraftforge.common.ForgeDirection;

public class ItemGaussMeter extends Item implements IToolGaussMeter {

	public ItemGaussMeter(int par1) {

		super(par1);
		setCreativeTab(CreativeTabsAMP.instance);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)
			return false;
		int blockId = world.getBlockId(x, y, z);
		Block block = Block.blocksList[blockId];
		if(block == null)
			return false;
		
		if (!player.isSneaking())
			return false;
		
		TileEntityMagnetic TE = (TileEntityMagnetic)world.getBlockTileEntity(x, y, z);
		System.out.println("("+x+", "+y+", "+z+"):"+world.getBlockMetadata(x, y, z)+" is "+TE);
		float gauss = -1;
		if(TE != null)
			gauss =  TE.gauss;
		float maxGauss = -1;
		if(TE != null)
		{
			maxGauss =  TE.totalGauss;
			TE.totalGauss = 0;
		}
		if(gauss == -1)
			FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage("this block is not magnetic");
		else
			FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage("this block contains "+gauss+"G, with a max of "+maxGauss+"G");
		
		return true;
	}
	
	@Override
	public boolean canMeasure(EntityPlayer player) {
		return true;
	}

	@Override
	public void MeterUsed(EntityPlayer player) {
		player.swingItem();
	}

	@Override
	public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6) {
		return true;
	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon(getUnlocalizedName().replace("item.", ""));
	}
}
