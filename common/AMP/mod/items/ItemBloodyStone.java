package AMP.mod.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import AMP.mod.core.CreativeTabsAMP;
import AMP.mod.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBloodyStone  extends Item {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;
	public static final String[] bandedNames = new String[] {"blood infused stone"};
	public ItemBloodyStone(int par1) {
		super(par1);
		setCreativeTab(CreativeTabsAMP.instance);
		setHasSubtypes(true);
		setMaxDamage(64);
	}
	
	@SuppressWarnings({ "all" })
	@Override
	public Icon getIconFromDamage(int i) {
		if(i == 64)
			return icons[0];
		i=(int) Math.ceil(i/8);
	    return i < icons.length ? icons[icons.length-i-1] : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	    icons = new Icon[8];
	    for (int i = 0; i < this.icons.length; i++) {
	        icons[i] = par1IconRegister.registerIcon(Reference.MOD_ID + ":item_bloody_stone_" + i);
	    }
	}

}
