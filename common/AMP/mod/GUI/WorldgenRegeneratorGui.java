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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.network.ForgePacket;
import net.minecraftforge.common.network.ForgePacketHandler;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import AMP.mod.containers.WorldgenRegeneratorContainer;
import AMP.mod.core.worldgen.WorldgenMonitor;
import AMP.mod.entry.AMPMod;
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
    	GL11.glDisable(GL11.GL_DEPTH_TEST);
    	GL11.glEnable(GL11.GL_BLEND);
		this.mc.getTextureManager().bindTexture(myBG);
    	this.drawTexturedModalRect(34+18*selectedCol, -11+18*selectedRow, 215, 0, 17,17);
    	GL11.glDisable(GL11.GL_BLEND);
    	GL11.glEnable(GL11.GL_DEPTH_TEST);
            //draws "Inventory" or your regional equivalent
            //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }
    @Override
    public void drawScreen(int par1, int par2, float par3) 
    {
    	super.drawScreen(par1, par2, par3);

    };
    ResourceLocation myBG = new ResourceLocation("amp", "textures/gui/worldgenregenerator.png");
    int button1Phase = 1;
    int button2Phase = 1;
    int button3Phase = 1;
    int button4Phase = 1;
    int x;
    int y;
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	//System.out.println("F: "+f+", I: "+i+", J: "+j);
    	mouseMovedOrUp(i, j, -1);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(myBG);
        x = (width - 184) / 2;
        y = (height - 202) / 2;
        drawTexturedModalRect(x, y, 0, 0, 175, 202);

        drawTexturedModalRect(x+152, y+57, 176, 73+16*button1Phase, 16, 16);
        drawTexturedModalRect(x+133, y+57, 176, 121+16*button2Phase, 16, 16);
        

        drawTexturedModalRect(x+152, y+10, 192, 121+16*button3Phase, 16, 16);
        drawTexturedModalRect(x+133, y+10, 208, 121+16*button4Phase, 16, 16);
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
        }
        
        if(inventory.tank.amount > 0)
        {
        	FluidStack liquid = inventory.tank;
        	int squaled = (int)(((float)liquid.amount/4000f)*63);
    		

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

        this.mc.getTextureManager().bindTexture(myBG);
        drawTexturedModalRect(x+25, y+8, 193, 15, 15, 62);
        
        drawContainerGUI();
    }
    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int type) {
    	
    	//System.out.println(type);
        if(type >= 0)
        {
        	if(button1Phase == 0)
        	{
        		button1Phase = 1;
        	}
        	if(button2Phase == 0)
        	{
        		button2Phase = 1;
        	}
        	if(button3Phase == 0)
        	{
        		button3Phase = 1;
        	}
        	if(button4Phase == 0)
        	{
        		button4Phase = 1;
        	}
        }
        else
        {
        	if(button1Phase != 0)
        	{
	        	if(isPointInRegion(148, 39, 16, 16, mouseX, mouseY))
	    		{
	        		button1Phase = 2;
	    		}
	        	else
	        	{
	        		button1Phase = 1;
	        	}
        	}
        	if(button2Phase != 0)
        	{
	        	if(isPointInRegion(129, 39, 16, 16, mouseX, mouseY))
	    		{
	        		button2Phase = 2;
	    		}
	        	else
	        	{
	        		button2Phase = 1;
	        	}
        	}
        	if(button3Phase != 0)
        	{
	        	if(isPointInRegion(148, -8, 16, 16, mouseX, mouseY))
	    		{
	        		button3Phase = 2;
	    		}
	        	else
	        	{
	        		button3Phase = 1;
	        	}
        	}
        	if(button4Phase != 0)
        	{
	        	if(isPointInRegion(129, -8, 16, 16, mouseX, mouseY))
	    		{
	        		button4Phase = 2;
	    		}
	        	else
	        	{
	        		button4Phase = 1;
	        	}
        	}
        }
    	
    super.mouseMovedOrUp(mouseX, mouseY, type);
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
    	
    	if(isPointInRegion(148, 39, 16, 16, mouseX, mouseY))
		{
    		button1Phase = 0;
		}
    	if(isPointInRegion(129, 39, 16, 16, mouseX, mouseY))
		{
    		button2Phase = 0;
		}
    	if(isPointInRegion(148, -8, 16, 16, mouseX, mouseY))
		{
    		button3Phase = 0;
    		if(inventory.selectedPageNum*20 < WorldgenMonitor.getAllUniqueBlockIds().length)
    		{
    			inventory.selectedPageNum++;
    			((WorldgenRegeneratorContainer) inventorySlots).redoLayout();
    			
    		}
    		else
    		{
    			System.out.println("startIndex: "+inventory.selectedPageNum*20+", endIndex: "+WorldgenMonitor.getAllUniqueBlockIds().length);
    		}
		}
    	if(isPointInRegion(129, -8, 16, 16, mouseX, mouseY))
		{
    		button4Phase = 0;
    		if(inventory.selectedPageNum>0)
    		{
    			inventory.selectedPageNum--;
    			((WorldgenRegeneratorContainer) inventorySlots).redoLayout();
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
