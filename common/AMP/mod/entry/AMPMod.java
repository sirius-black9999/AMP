package AMP.mod.entry;

import java.io.File;
import java.sql.Ref;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import AMP.mod.blocks.BlockBloodyStone;
import AMP.mod.blocks.BlockMagnetInductionFurnace;
import AMP.mod.blocks.BlockMagneticConductor;
import AMP.mod.blocks.BlockMulticolorWool;
import AMP.mod.blocks.BlockRedCell;
import AMP.mod.blocks.BlockWorldgenLiquifier;
import AMP.mod.blocks.BlockWorldgenRegenerator;
import AMP.mod.core.CreativeTabsAMP;
import AMP.mod.core.PacketHandler;
import AMP.mod.core.Reference;
import AMP.mod.core.worldgen.WorldgenMonitor;
import AMP.mod.fluids.LiquidWorldgen;
import AMP.mod.items.ItemBloodyStone;
import AMP.mod.items.ItemRedCells;
import AMP.mod.proxy.CommonProxy;
import AMP.mod.recipes.InfusionRecipe;
import AMP.mod.tileentities.TileEntityMagnetic;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * LetsMod
 * 
 * LetsMod
 * 
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[7.0,);required-after:FML@[5.0.5,)")
@NetworkMod(channels = { "AMP" }, versionBounds = "[0.0.1]", clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class AMPMod{
	@Instance(Reference.MOD_ID)
	public static AMPMod instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static Item itemBandedStuff;
	public static Item itemBloodyStone;
	public static Item GaussMeter;
	public static Block blockRedCellV2;
	public static Block blockMultiColorWool;
	public static Block blockBloodyStone;
	public static Block blockMagneticConductor;
	public static Block blockMagneticInductionFurnace;
	public static Block blockWorldgenLiquifier;
	public static Block blockWorldgenRegenerator;
	public static Fluid fluidLiquidWorldgen;
	public static File configAlt;
    /***
     * This is code that is executed prior to your mod being initialized into of Minecraft
     * Examples of code that could be run here;
     * 
     * Initializing your items/blocks (you must do this here)
     * Setting up your own custom logger for writing log data to
     * Loading your language translations for your mod (if your mod has translations for other languages)
     * Registering your mod's key bindings and sounds
     * 
     * @param event The Forge ModLoader pre-initialization event
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
    	configAlt = new File(event.getModConfigurationDirectory().getAbsolutePath()+"/AMP_BlockValues.cfg");
    	try
    	{
    		cfg.load();
            Reference.load(cfg);
    	}
    	catch(Exception e)
    	{
    		System.err.println("Aerial mage prototype has a problem loading its configuration file:");
    		e.printStackTrace(System.err);
    	}
    	finally
    	{
    		if(cfg.hasChanged())
    			cfg.save();
    	}
    	
    	//proxy.registerTickHandlers();
    	instance = this;
    	blockMultiColorWool = new BlockMulticolorWool(Reference.multiColorWoolId, Material.cloth).setHardness(1.5F).setStepSound(Block.soundClothFootstep).setTextureName("multi_color_wool").setUnlocalizedName("AMP:multi_color_wool").setCreativeTab(CreativeTabsAMP.instance);
    	blockRedCellV2 = new BlockRedCell(Reference.redCellId).setHardness(1.5F).setStepSound(Block.soundClothFootstep).setTextureName("red_cell").setUnlocalizedName("AMP:red_cell").setCreativeTab(CreativeTabsAMP.instance);
    	blockBloodyStone = new BlockBloodyStone(Reference.bloodyStoneId, Material.rock).setHardness(1.5F).setStepSound(Block.soundClothFootstep).setTextureName("block_bloody_stone").setUnlocalizedName("AMP:bloody_stone").setCreativeTab(CreativeTabsAMP.instance);
    	blockMagneticConductor = new BlockMagneticConductor(Reference.magneticConductorId, Material.rock).setHardness(1.5F).setStepSound(Block.soundClothFootstep).setTextureName("block_magnetic_conductor").setUnlocalizedName("AMP:magnet_block").setCreativeTab(CreativeTabsAMP.instance);
    	blockMagneticInductionFurnace = new BlockMagnetInductionFurnace(Reference.magneticConductionFurnaceId, Material.rock).setHardness(1.5F).setStepSound(Block.soundClothFootstep).setTextureName("block_magnetic_furnace_block").setUnlocalizedName("AMP:magnet_furnace_block").setCreativeTab(CreativeTabsAMP.instance);
    	blockWorldgenLiquifier = new BlockWorldgenLiquifier(Reference.magneticWorldgenLiquifierId, Material.rock).setHardness(1.5F).setStepSound(Block.soundClothFootstep).setTextureName("block_worldgen_liquifier").setUnlocalizedName("AMP:worldgen_liquifier_block").setCreativeTab(CreativeTabsAMP.instance);
    	blockWorldgenRegenerator = new BlockWorldgenRegenerator(Reference.magneticWorldgenRegeneratorId, Material.rock).setHardness(1.5F).setStepSound(Block.soundClothFootstep).setTextureName("block_worldgen_regenerator").setUnlocalizedName("AMP:worldgen_regenerator_block").setCreativeTab(CreativeTabsAMP.instance);
    	GameRegistry.registerBlock(blockMultiColorWool, "multiColorWool");
    	GameRegistry.registerBlock(blockRedCellV2, "redCell");
    	GameRegistry.registerBlock(blockBloodyStone, "bloodyStone");
    	GameRegistry.registerBlock(blockMagneticConductor, "magneticConductor");
    	GameRegistry.registerBlock(blockMagneticInductionFurnace, "magneticInductionFurnace");
    	GameRegistry.registerBlock(blockWorldgenLiquifier, "worldgenLiquifier");
    	GameRegistry.registerBlock(blockWorldgenRegenerator, "worldgenRegenerator");
    	

    	itemBandedStuff = new ItemRedCells(Reference.bandedItemsId);
    	itemBandedStuff.setUnlocalizedName(Reference.MOD_ID+":bandedstuff");
    	
    	itemBloodyStone = new ItemBloodyStone(Reference.bloodStoneId);
    	itemBloodyStone.setUnlocalizedName(Reference.MOD_ID+":bloodystone");
    	
    	GaussMeter = new AMP.mod.items.ItemGaussMeter(Reference.gaussMeterId);
    	GaussMeter.setUnlocalizedName(Reference.MOD_ID+":gaussmeter");
    	
    	fluidLiquidWorldgen = new LiquidWorldgen("liquid worldgen");
    	FluidRegistry.registerFluid(fluidLiquidWorldgen);
    	
    }
    
    /***
     * This is code that is executed when your mod is being initialized in Minecraft
     * Examples of code that could be run here;
     * 
     * Registering your GUI Handler
     * Registering your different event listeners
     * Registering your different tile entities
     * Adding in any recipes you have 
     * 
     * @param event The Forge ModLoader initialization event
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	//FMLNetworkHandler.instance().registerNetworkMod();
    	NetworkRegistry.instance().registerGuiHandler(this, proxy);	
    	net.minecraft.item.crafting.CraftingManager.getInstance().getRecipeList().add(new InfusionRecipe());
    	GameRegistry.addRecipe(new ItemStack(blockRedCellV2), "BBB", "BRB", "BBB", 'B', new ItemStack(itemBandedStuff, 1, 0), 'R', new ItemStack(itemBandedStuff, 1, 1));
    	GameRegistry.addShapelessRecipe(new ItemStack(blockMultiColorWool), new Object[]{new ItemStack(Block.cloth, 1), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 2), new ItemStack(Item.dyePowder, 1, 4)});
    	GameRegistry.addRecipe(new ItemStack(itemBandedStuff, 1, 0), "SSS", "SGS", "SSS", 'S', Block.stone, 'G', Block.thinGlass);
    	GameRegistry.addRecipe(new ItemStack(itemBandedStuff, 1, 1), "I I", " R ", "I I", 'I', Item.ingotIron, 'R', Block.blockRedstone);
    	GameRegistry.addRecipe(new ItemStack(itemBloodyStone, 1, 64), "RRR", "RSR", "RRR", 'R', Item.rottenFlesh, 'S', Block.stone);
    	GameRegistry.addRecipe(new ItemStack(blockBloodyStone, 1, 0), "ILI", "LSL", "ILI", 'I', Item.ingotIron, 'L', Item.leather, 'S', new ItemStack(itemBloodyStone, 1, 0));
    	GameRegistry.registerTileEntity(TileEntityMagnetic.class, "magnetBlock");
       
        MinecraftForge.EVENT_BUS.register(new WorldgenMonitor());
        MinecraftForge.EVENT_BUS.register(fluidLiquidWorldgen);
        MinecraftForge.EVENT_BUS.register(blockRedCellV2);
        
    }
    
    /***
     * This is code that is executed after all mods are initialized in Minecraft
     * This is a good place to execute code that interacts with other mods (ie, loads an addon module
     * of your mod if you find a particular mod).
     * 
     * @param event The Forge ModLoader post-initialization event
     */	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        
    }
}