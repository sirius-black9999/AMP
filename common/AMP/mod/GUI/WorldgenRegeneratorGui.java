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
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import AMP.mod.blocks.BlockMagnetInductionFurnace;
import AMP.mod.containers.MagneticInductionContainer;
import AMP.mod.containers.SlotSelectable;
import AMP.mod.containers.WorldgenLiquifierContainer;
import AMP.mod.containers.WorldgenRegeneratorContainer;
import AMP.mod.tileentities.TileEntityMagneticInductionFurnace;
import AMP.mod.tileentities.TileEntityWorldgenLiquifier;
import AMP.mod.tileentities.TileEntityWorldgenRegenerator;


public class WorldgenRegeneratorGui extends GuiContainer {

		InventoryPlayer player;
		TileEntityWorldgenRegenerator inventory;
		int selectedRow;
		int selectedCol;
		private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
        private WorldgenRegeneratorGui( IInventory player, TileEntityWorldgenRegenerator te)
        {
            super(makeContainer(player, te));
            this.allowUserInput = false;
            	inventory = te;
            	this.player = (InventoryPlayer) player;
        }

        protected static Container makeContainer(IInventory player, TileEntityWorldgenRegenerator te)
        {
            return new WorldgenRegeneratorContainer(player, te);
        }

        public static WorldgenRegeneratorGui buildGUI(IInventory playerInventory, TileEntityWorldgenRegenerator te)
        {
            return new WorldgenRegeneratorGui(playerInventory, te);
        }

    public int getRowLength()
    {
        return 1;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
            //draw text and stuff here
            //the parameters for drawString are: string, x, y, color
            //fontRenderer.drawString("Magnetic worldgen liquifier", 25, -10, 4210752);
            
            //draws "Inventory" or your regional equivalent
            //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(new ResourceLocation("amp", "textures/gui/worldgenregenerator.png"));
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
            //System.out.println("got inventory, gauss: "+i1);
            //int i2 = 72-i1;
            	this.drawTexturedModalRect(x+38+18*selectedCol, y+7+18*selectedRow, 215, 0, 17,17);
        }
        
        if(inventory.tank.amount > 0)
        {
        	FluidStack liquid = inventory.tank;
        	int squaled = (int)(((float)liquid.amount/4000f)*62);
    		
    		int start = 0;

    		Icon liquidIcon = null;
    		Fluid fluid = liquid.getFluid();
    		//System.out.println("displaying gauge for "+liquid.getFluid().getLocalizedName());
    		if (fluid != null && fluid.getStillIcon() != null) {
    			liquidIcon = fluid.getStillIcon();
    		}
    		mc.renderEngine.bindTexture(BLOCK_TEXTURE);

    		if (liquidIcon != null) {
    				//System.out.println("drawing liquid");

				int offset = 0;
    			while (squaled > 0) {
    				int tx;

    				if (squaled > 11) {
    					tx = 11;
    					squaled -= 11;
    				} else {
    					tx = squaled;
    					squaled = 0;
    				}
    				
    				//drawTexturedModelRectFromIcon(j + col, k + line + 58 - x - start, liquidIcon, 16, 16 - (16 - x));
    				drawTexturedModelRectFromIcon(x+25,y+71-tx-offset, liquidIcon, 11, 16 - (16 - tx));
    				offset += 11;
    			}
    		}
        }
        drawContainerGUI();
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    	boolean gotTriggered = false;
    	for(int row = 0; row < 4; row++)
    	{
    		for(int col = 0; col < 5; col++)
    		{
    			if(isPointInRegion(35+(18*col), -10+18*row, 16, 16, mouseX, mouseY))
    			{
    				selectedRow = row;
    				selectedCol = col;
    				gotTriggered = true;
    			}
    		}
    	}
    	if(!gotTriggered)
    		super.mouseClicked(mouseX, mouseY, mouseButton);
    }

	private void drawContainerGUI() {
		int counter = 0;
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
        
        

        fontRenderer.drawString(""+counter++, 265, 65, 0);
        //addSlotToContainer(new Slot(chest, 0, 52, 16));
        for(int row = 0; row < 4; row++)
        	for(int col = 0; col < 5; col++)
                fontRenderer.drawString(""+counter++, 150+col*18, 38+row*18, 0);
        //System.out.println(this.inventorySlots.size());
	}
}
