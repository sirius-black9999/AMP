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
package AMP.mod.GUI;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import AMP.mod.blocks.BlockMagnetInductionFurnace;
import AMP.mod.containers.MagneticInductionContainer;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;


public class MagFurnaceGui extends GuiContainer {

		InventoryPlayer player;
		TileEntityMagneticInductionFurnace inventory;
        private MagFurnaceGui( IInventory player, TileEntityMagneticInductionFurnace chest)
        {
            super(makeContainer(player, chest));
            this.allowUserInput = false;
            	inventory = chest;
            	this.player = (InventoryPlayer) player;
        }

        protected static Container makeContainer(IInventory player, TileEntityMagneticInductionFurnace chest)
        {
            return new MagneticInductionContainer(player, chest);
        }

        public static MagFurnaceGui buildGUI(IInventory playerInventory, TileEntityMagneticInductionFurnace chestInventory)
        {
            return new MagFurnaceGui(playerInventory, chestInventory);
        }

    public int getRowLength()
    {
        return 1;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
            //draw text and stuff here
            //the parameters for drawString are: string, x, y, color
            fontRenderer.drawString("Magnetic induction furnace", 25, -10, 4210752);
            
            //draws "Inventory" or your regional equivalent
            //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(new ResourceLocation("amp", "textures/gui/magneticinductionfurnace.png"));
        int x = (width - 184) / 2;
        int y = (height - 202) / 2;
        drawTexturedModalRect(x, y, 0, 0, 175, 202);
        if(inventory != null)
        {
            int i1 = (int) this.inventory.gauss/1000;
            //System.out.println("got inventory, gauss: "+i1);
            //int i2 = 72-i1;
            if(i1 < 72000)
            	this.drawTexturedModalRect(x+6, y+6+72-i1, 176, 72-i1, 16,i1);
            else
            	this.drawTexturedModalRect(x+6, y+6, 176, 0, 16,72);
        }
        drawContainerGUI();
    }

	private void drawContainerGUI() {
		int counter = 0;
        fontRenderer.drawString(""+counter++, 168, 65, 0);
        //addSlotToContainer(new Slot(chest, 0, 52, 16));
        fontRenderer.drawString(""+counter++, 228, 67, 0);
        //addSlotToContainer(new Slot(chest, 1, 112, 17));
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
            	fontRenderer.drawString(""+counter++, 120+18*playerInvCol,115+18*playerInvRow,0);
            	//fontRenderer.drawString(""+counter++, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, 148 - (4 - playerInvRow) * 18 - 10);
//                addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, 148 - (4 - playerInvRow) * 18 - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
        	fontRenderer.drawString(""+counter++, 120+18*hotbarSlot,173, 0);
            //addSlotToContainer(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, 148 - 24));
        }
        //System.out.println(this.inventorySlots.size());
	}
}
