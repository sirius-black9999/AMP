package AMP.mod.interfaces;

import net.minecraft.entity.player.EntityPlayer;

public interface IToolGaussMeter {

/***
 * Implement this interface on subclasses of Item to have that item work as a gauss meter for AMP mods
 * credit for original class goes to buildcraft's IToolWrench ;)
 */
	/***
	 * Called to ensure that the gauss meter can be used. To get the ItemStack that is used, check player.inventory.getCurrentItem()
	 * 
	 * @param player - The player doing the action
	 * @param x,y,z - The coordinates for the block being measured
	 * 
	 * @return true if measuring is allowed, false if not
	 */
	public boolean canMeasure(EntityPlayer player);

	/***
	 * Callback after the gauss meter has been used. This can be used to decrease durability or for other purposes. To get the ItemStack that was used, check
	 * player.inventory.getCurrentItem()
	 * 
	 * @param player - The player doing the action
	 * @param x,y,z - The coordinates of the block being wrenched
	 */
	public void MeterUsed(EntityPlayer player);
}

