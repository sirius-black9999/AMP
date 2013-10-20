package AMP.mod.fluids;

import AMP.mod.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;

public class LiquidWorldgen extends Fluid {

	Icon[] iconArray;
	public LiquidWorldgen(String fluidName) {
		super(fluidName);
		
	}
	@SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */

	@ForgeSubscribe
    public void registerIcons(TextureStitchEvent.Pre evt)
    {
        iconArray = new Icon[2];
        iconArray[0] = evt.map.registerIcon(Reference.MOD_ID + ":liquid_worldgen_still");
        iconArray[1] = evt.map.registerIcon(Reference.MOD_ID + ":liquid_worldgen_flow");
    }
	@ForgeSubscribe
	public void finishIcons(TextureStitchEvent.Post evt)
	{
		setFlowingIcon(iconArray[1]);
        setStillIcon(iconArray[0]);
	}
}
