package AMP.mod.core;

import net.minecraftforge.common.Configuration;

/**
 * LetsMod
 * 
 * Reference
 * 
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class Reference {

    /**
     *	The mod id
     */
    public static final String MOD_ID = "AMP";
    
    /**
     *	The mod name
     */
    public static final String MOD_NAME = "Aerial Mage Prototype";
    
    /**
     *	The version of the mod
     */
    public static final String VERSION = "0.0.1";
    
    /**
     *	The common proxy class for the mod
     */
    public static final String SERVER_PROXY_CLASS = "AMP.mod.proxy.CommonProxy";
    
    /**
     * 	The client specific proxy class for the mod
     */
    public static final String CLIENT_PROXY_CLASS = "AMP.mod.proxy.ClientProxy";

    /**
     * 	blockID's
     */
    public static int multiColorWoolId;
    public static int magneticConductorId;
    public static int magneticConductionFurnaceId;
    public static int magneticWorldgenLiquifierId;
    public static int magneticWorldgenRegeneratorId;
    public static int bloodyStoneId;
    public static int redCellId;
    /**
     * 	itemID's
     */
    public static int bloodStoneId;
    public static int gaussMeterId;
    public static int bandedItemsId;

	public static void load(Configuration cfg) {
		multiColorWoolId = cfg.getBlock("multiColorWool", 1000).getInt();
		magneticConductorId = cfg.getBlock("magneticConductor", 1001).getInt();
		magneticConductionFurnaceId = cfg.getBlock("magneticConductionFurnace", 1002).getInt();
		bloodyStoneId = cfg.getBlock("bloodyStone", 1003).getInt();
		redCellId = cfg.getBlock("redCell", 1004).getInt();
		magneticWorldgenLiquifierId = cfg.getBlock("magneticWorldgenLiquifier", 1005).getInt();
		magneticWorldgenRegeneratorId = cfg.getBlock("magneticWorldgenRegenerator", 1006).getInt();
		
		bloodStoneId = cfg.getItem("bloodStone", 10000).getInt();
		gaussMeterId = cfg.getItem("gaussMeter", 10001).getInt();
		bandedItemsId = cfg.getItem("bandedItems", 10002).getInt();
	}
}