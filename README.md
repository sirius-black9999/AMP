AMP
===

prototype of aerial mage mod

this mod is far from being anywhere near done, and a lot of items/blocks are currently only usable from creative mode
the mod adds the following mechanics:

multi-color wool(pointless block, done)

bloody stone --> redstone (done)
	crafting smoothstone with 8 rotten flesh gives you a bloody stone
	infusing bloody stone with 64 more rotten flesh will fill it's damage meter
	a fully infused bloody stone can be crafted with 4 iron and 4 leather to give a raw redstone block
	a raw redstone block can be placed and will slowly mature to a redstone block

red cells (done)
	you can craft a glass pane with 8 smoothstone to get stone-banded glass
	you can craft a redstone block with 4 iron to get an iron-banded redstone block
	you can craft an iron-banded redstone block with 8 stone-banded glass to get 1 red cell
	
	red cells activate when receiving a redstone signal
	red cells activate when exactly 1 or 2 neighboring blocks (directly adjacent or with 1 full edge touching) are activated

magnetic blocks(mostly done)
	magnetic blocks can be spawned from the creative menu
	magnetic blocks accumulate energy in the form of "gauss" from directly adjacent red cells
	magnetic blocks transfer magnetic charge in the direction the arrow on the sides faces
	magnetic blocks are used to power machines
	magnetic blocks constantly lose power

magnetic induction furnace(mostly done)
	the magnetic induction furnace can only be spawned from the creative menu
	the magnetic induction furnace does not have a texture yet, since i'm debating whether i should add it in the final version(seems OP)
	the magnetic induction furnace extends magnetic blocks, inheriting all it's behavior other than transmitting power outward
	the magnetic induction furnace instantly smelts items once it has received sufficient power
	the magnetic induction furnace uses power based on the rarity of the object(determined by the worldgen monitor), and will only smelt items that are considered worldgen (iron ore, for example, will smelt, while cobblestone won't)
	
worldgen monitor(needs a lot of testing)
	the worldgen monitor stores the frequency with which items spawn in the world, it saves said information per-biome in a config file called AMP_BlockValues.cfg next to the other config files
	be sure to delete this file when adding/removing mods that alter worldgen to ensure a correct recording of information
	play in a freely generated world for a while to ensure spawn chances are correct(or just create/delete worlds a bunch of times)
	when playing in a "vanilla" map that doesn't contain your mods' worldgen, the worldgen monitor will be able to estimate the "intended" worldgen ores present in your current biome based on the recorded information
	in a future update, the worldgen monitor will store it's data on a per-dimension basis
	the worldgen monitor can accept blockID's and unlocalized names to exclude from worldgen (such as thaumcraft's aura nodes)
	the worldgen monitor may store blocks by metadata in a future update