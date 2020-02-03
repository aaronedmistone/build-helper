package com.edmistone.buildhelper.operations;

import java.util.concurrent.ThreadLocalRandom;

import com.edmistone.buildhelper.helpers.ArrayHelper;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/** Currently only works on vanilla blocks as I am using this in the interim until I figure out a better way
 *  since the 1.13 flattening made this difficult. I can no longer just cycle the properties matching 'color' or 'variant'
 *  @author AE
 */
public class BlockVariant
{
	
	public static Block getRandomVariant(Block input)
	{
		return getVariant(input, ThreadLocalRandom.current().nextInt(100));
	}
	
	public static Block getVariant(Block input, int variantCycles)
	{
		if(variantCycles < 1)
			return input;
		
		String[][] allVariants = new String[][]
		{
			FENCE_VARIANTS,
			GATE_VARIANTS,
			TRAPDOOR_VARIANTS,
			STAIRS_VARIANTS,
			DECO_VARIANTS,
			DOOR_VARIANTS,
			PRESSURE_PLATE_VARIANTS,
			GLAZED_TERRACOTTA_VARIANTS,
			SHULKER_BOX_VARIANTS,
			SIGN_VARIANTS,
			STONE_VARIANTS,
			DIRT_VARIANTS,
			PLANKS_VARIANTS,
			SAPLING_VARIANTS,
			SAND_VARIANTS,
			WOOD_VARIANTS,
			LOG_VARIANTS,
			LEAVES_VARIANTS,
			SPONGE_VARIANTS,
			SANDSTONE_VARIANTS,
			GRASS_VARIANTS,
			WOOL_VARIANTS,
			SMALL_FLOWER_VARIANTS,
			CHEAP_SLAB_VARIANTS,
			EXPENSIVE_SLAB_VARIANTS,
			SMOOTH_VARIANTS,
			INFESTED_STONE_VARIANTS,
			STONE_BRICKS_VARIANTS,
			MUSHROOM_VARIANTS,
			COBBLESTONE_WALL_VARIANTS,
			ANVIL_VARIANTS,
			QUARTZ_VARIANTS,
			TERRACOTTA_VARIANTS,
			CARPET_VARIANTS,
			BIG_FLOWER_VARIANTS,
			STAINED_GLASS_VARIANTS,
			STAINED_GLASS_PANE_VARIANTS,
			PRISMARINE_VARIANTS,
			RED_SANDSTONE_VARIANTS,
			CONCRETE_VARIANTS,
			CONCRETE_POWDER_VARIANTS,
			DYE_VARIANTS,
			BED_VARIANTS,
			EGG_VARIANTS,
			POT_VARIANTS,
			HEAD_VARIANTS,
			WALL_HEAD_VARIANTS,
			BANNER_VARIANTS,
			WALL_BANNER_VARIANTS
		};
		
		String name = input.getRegistryName().getPath();
		for(String[] collection : allVariants)
		{
			int index = ArrayHelper.indexOf(collection, name);
			if(index == -1)
				continue;
			
			int newIndex = (index + variantCycles) % collection.length;
			return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft", collection[newIndex]));
		}
		
		return input;
	}
	
	public static final String[] FENCE_VARIANTS = new String[]
	{
			"andesite_wall",
			"brick_wall",
			"cobblestone_wall",
			"diorite_wall",
			"granite_wall",
			"end_stone_brick_wall",
			"stone_brick_wall",
			"nether_brick_wall",
			"red_nether_brick_wall",
			"red_sandstone_wall",
			"mossy_cobblestone_wall",
			"mossy_stone_brick_wall",
			"sandstone_wall",
			"prismarine_wall",
			"acacia_fence",
			"birch_fence",
			"oak_fence",
			"spruce_fence",
			"jungle_fence",
			"dark_oak_fence",
			"nether_brick_fence"
	};

	public static final String[] GATE_VARIANTS = new String[]
	{
			"acacia_fence_gate",
			"birch_fence_gate",
			"oak_fence_gate",
			"spruce_fence_gate",
			"jungle_fence_gate",
			"dark_oak_fence_gate"
	};
	
	public static final String[] TRAPDOOR_VARIANTS = new String[]
	{
			"acacia_trapdoor",
			"birch_trapdoor",
			"oak_trapdoor",
			"spruce_trapdoor",
			"jungle_trapdoor",
			"dark_oak_trapdoor"
	};
	
	public static final String[] STAIRS_VARIANTS = new String[]
	{
			"brick_stairs",
			"andesite_stairs",
			"cobblestone_stairs",
			"diorite_stairs",
			"end_stone_brick_stairs",
			"granite_stairs",
			"mossy_cobblestone_stairs",
			"mossy_stone_brick_stairs",
			"nether_brick_stairs",
			"polished_andesite_stairs",
			"polished_diorite_stairs",
			"polished_granite_stairs",
			"dark_prismarine_stairs",
			"prismarine_stairs",
			"prismarine_brick_stairs",
			"quartz_stairs",
			"smooth_quartz_stairs",
			"smooth_red_sandstone_stairs",
			"smooth_sandstone_stairs",
			"purpur_stairs",
			"red_sandstone_stairs",
			"red_nether_brick_stairs",
			"sandstone_stairs",
			"stone_brick_stairs",
			"stone_stairs",
			
			"acacia_stairs",
			"birch_stairs",			
			"oak_stairs",
			"spruce_stairs",
			"jungle_stairs",
			"dark_oak_stairs"
	};
	
	public static final String[] DECO_VARIANTS = new String[]
	{
			"armor_stand",
			"bee_hive",
			"brewing_stand",
			"cauldron",
			"composter",
			"crafting_table",
			"grindstone",
			"loom",
			"pumpkin",
			"smoker"
	};
	
	public static final String[] DOOR_VARIANTS = new String[]
	{
			"acacia_door",
			"birch_door",
			"oak_door",
			"spruce_door",
			"jungle_door",
			"dark_oak_door"
	};
	
	public static final String[] PRESSURE_PLATE_VARIANTS = new String[]
	{
			"acacia_pressure_plate",
			"birch_pressure_plate",
			"oak_pressure_plate",
			"spruce_pressure_plate",
			"jungle_pressure_plate",
			"dark_oak_pressure_plate",
			"stone_pressure_plate"
	};
	
	public static final String[] GLAZED_TERRACOTTA_VARIANTS = new String[]
	{
			"white_glazed_terracotta",
			"orange_glazed_terracotta",
			"magenta_glazed_terracotta",
			"light_blue_glazed_terracotta",
			"yellow_glazed_terracotta",
			"lime_glazed_terracotta",
			"pink_glazed_terracotta",
			"gray_glazed_terracotta",
			"light_gray_glazed_terracotta",
			"cyan_glazed_terracotta",
			"purple_glazed_terracotta",
			"blue_glazed_terracotta",
			"brown_glazed_terracotta",
			"green_glazed_terracotta",
			"red_glazed_terracotta",
			"black_glazed_terracotta"
	};
	
	public static final String[] SHULKER_BOX_VARIANTS = new String[]
	{
			"shulker_box",
			"white_shulker_box",
			"orange_shulker_box",
			"magenta_shulker_box",
			"light_blue_shulker_box",
			"yellow_shulker_box",
			"lime_shulker_box",
			"pink_shulker_box",
			"gray_shulker_box",
			"light_gray_shulker_box",
			"cyan_shulker_box",
			"purple_shulker_box",
			"blue_shulker_box",
			"brown_shulker_box",
			"green_shulker_box",
			"red_shulker_box",
			"black_shulker_box"
	};
	
	public static final String[] SIGN_VARIANTS = new String[]
	{
			"acacia_sign",
			"birch_sign",
			"oak_sign",
			"spruce_sign",
			"jungle_sign",
			"dark_oak_sign"
	};
	
	public static final String[] STONE_VARIANTS = new String[]
	{
			"stone",
			"granite",
			"polished_granite",
			"diorite",
			"polished_diorite",
			"andesite",
			"polished_andesite"
	};
	
	public static final String[] DIRT_VARIANTS = new String[]
	{
			"dirt",
			"coarse_dirt",
			"podzol"
	};
	
	public static final String[] PLANKS_VARIANTS = new String[]
	{
			"oak_planks",
			"spruce_planks",
			"birch_planks",
			"jungle_planks",
			"acacia_planks",
			"dark_oak_planks"
	};
	
	public static final String[] SAPLING_VARIANTS = new String[]
	{
			"oak_sapling",
			"spruce_sapling",
			"birch_sapling",
			"jungle_sapling",
			"acacia_sapling",
			"dark_oak_sapling"
	};
	
	public static final String[] SAND_VARIANTS = new String[]
	{
			"sand",
			"red_sand"
	};
	
	public static final String[] WOOD_VARIANTS = new String[]
	{
			"oak_wood",
			"spruce_wood",
			"birch_wood",
			"jungle_wood",
			"acacia_wood",
			"dark_oak_wood"
			
	};
	
	public static final String[] LOG_VARIANTS = new String[]
	{
			"oak_log",
			"spruce_log",
			"birch_log",
			"jungle_log",
			"acacia_log",
			"dark_oak_log"
	};
	
	public static final String[] LEAVES_VARIANTS = new String[]
	{
			"oak_leaves",
			"spruce_leaves",
			"birch_leaves",
			"jungle_leaves",
			"acacia_leaves",
			"dark_oak_leaves"
	};
	
	public static final String[] SPONGE_VARIANTS = new String[]
	{
			"sponge",
			"wet_sponge"
	};
	
	public static final String[] SANDSTONE_VARIANTS = new String[]
	{
			"sandstone",
			"chiseled_sandstone",
			"cut_sandstone"
	};
	
	public static final String[] GRASS_VARIANTS = new String[]
	{
			"dead_bush",
			"grass",
			"fern"
	};
	
	public static final String[] WOOL_VARIANTS = new String[]
	{
			"white_wool",
			"orange_wool",
			"magenta_wool",
			"light_blue_wool",
			"yellow_wool",
			"lime_wool",
			"pink_wool",
			"gray_wool",
			"light_gray_wool",
			"cyan_wool",
			"purple_wool",
			"blue_wool",
			"brown_wool",
			"green_wool",
			"red_wool",
			"black_wool"
	};
	
	public static final String[] SMALL_FLOWER_VARIANTS = new String[]
	{
			"poppy",
			"blue_orchid",
			"allium",
			"azure_bluet",
			"red_tulip",
			"orange_tulip",
			"white_tulip",
			"pink_tulip",
			"oxeye_daisy",
			"lily_of_the_valley"
	};
	
	public static final String[] CHEAP_SLAB_VARIANTS = new String[]
	{
			"oak_slab",
			"spruce_slab",
			"birch_slab",
			"wooden_slab",
			"jungle_slab",
			"acacia_slab",
			"dark_oak_slab",
			"stone_slab",
			"sandstone_slab",
			"petrified_oak_slab",
			"cobblestone_slab",
			"brick_slab"
	};
	
	public static final String[] EXPENSIVE_SLAB_VARIANTS = new String[]
	{
			"stone_brick_slab",
			"nether_brick_slab",
			"quartz_slab",
			"red_sandstone_slab"
	};
	
	public static final String[] SMOOTH_VARIANTS = new String[]
	{
			"smooth_stone",
			"smooth_sandstone",
			"smooth_quartz",
			"smooth_red_sandstone"
	};
	
	public static final String[] INFESTED_STONE_VARIANTS = new String[]
	{
			"infested_stone",
			"infested_cobblestone",
			"infested_stone_bricks",
			"infested_mossy_stone_bricks",
			"infested_cracked_stone_bricks",
			"infested_chiseled_stone_bricks"
	};
	
	public static final String[] STONE_BRICKS_VARIANTS = new String[]
	{
			"bricks",
			"stone_bricks",
			"mossy_stone_bricks",
			"cracked_stone_bricks",
			"chiseled_stone_bricks"
	};
	
	public static final String[] MUSHROOM_VARIANTS = new String[]
	{
			"brown_mushroom_block",
			"red_mushroom_block"
	};
	
	public static final String[] COBBLESTONE_WALL_VARIANTS = new String[]
	{
			"cobblestone_wall",
			"mossy_cobblestone_wall"
	};
	
	public static final String[] ANVIL_VARIANTS = new String[]
	{
			"anvil",
			"chipped_anvil",
			"damaged_anvil"
	};
	
	public static final String[] QUARTZ_VARIANTS = new String[]
	{
			"quartz_block",
			"chiseled_quartz_block",
			"quartz_pillar"
	};
	
	public static final String[] TERRACOTTA_VARIANTS = new String[]
	{
			"white_terracotta",
			"orange_terracotta",
			"magenta_terracotta",
			"light_blue_terracotta",
			"yellow_terracotta",
			"lime_terracotta",
			"pink_terracotta",
			"gray_terracotta",
			"light_gray_terracotta",
			"cyan_terracotta",
			"purple_terracotta",
			"blue_terracotta",
			"brown_terracotta",
			"green_terracotta",
			"red_terracotta",
			"black_terracotta"
	};
	
	public static final String[] CARPET_VARIANTS = new String[]
	{
			"white_carpet",
			"orange_carpet",
			"magenta_carpet",
			"light_blue_carpet",
			"yellow_carpet",
			"lime_carpet",
			"pink_carpet",
			"gray_carpet",
			"light_gray_carpet",
			"cyan_carpet",
			"purple_carpet",
			"blue_carpet",
			"brown_carpet",
			"green_carpet",
			"red_carpet",
			"black_carpet"
	};
	
	public static final String[] BIG_FLOWER_VARIANTS = new String[]
	{
			"sunflower",
			"lilac",
			"tall_grass",
			"large_fern",
			"rose_bush",
			"peony"
	};
	
	public static final String[] STAINED_GLASS_VARIANTS = new String[]
	{
			"white_stained_glass",
			"orange_stained_glass",
			"magenta_stained_glass",
			"light_blue_stained_glass",
			"yellow_stained_glass",
			"lime_stained_glass",
			"pink_stained_glass",
			"gray_stained_glass",
			"light_gray_stained_glass",
			"cyan_stained_glass",
			"purple_stained_glass",
			"blue_stained_glass",
			"brown_stained_glass",
			"green_stained_glass",
			"red_stained_glass",
			"black_stained_glass"
	};
	
	public static final String[] STAINED_GLASS_PANE_VARIANTS = new String[]
	{
			"white_stained_glass_pane",
			"orange_stained_glass_pane",
			"magenta_stained_glass_pane",
			"light_blue_stained_glass_pane",
			"yellow_stained_glass_pane",
			"lime_stained_glass_pane",
			"pink_stained_glass_pane",
			"gray_stained_glass_pane",
			"light_gray_stained_glass_pane",
			"cyan_stained_glass_pane",
			"purple_stained_glass_pane",
			"blue_stained_glass_pane",
			"brown_stained_glass_pane",
			"green_stained_glass_pane",
			"red_stained_glass_pane",
			"black_stained_glass_pane"
	};
	
	public static final String[] PRISMARINE_VARIANTS = new String[]
	{
			"prismarine",
			"prismarine_bricks",
			"dark_prismarine"
	};
	
	public static final String[] RED_SANDSTONE_VARIANTS = new String[]
	{
			"red_sandstone",
			"chiseled_red_sandstone",
			"cut_red_sandstone"
	};
	
	public static final String[] CONCRETE_VARIANTS = new String[]
	{
			"white_concrete",
			"orange_concrete",
			"magenta_concrete",
			"light_blue_concrete",
			"yellow_concrete",
			"lime_concrete",
			"pink_concrete",
			"gray_concrete",
			"light_gray_concrete",
			"cyan_concrete",
			"purple_concrete",
			"blue_concrete",
			"brown_concrete",
			"green_concrete",
			"red_concrete",
			"black_concrete"
	};
	
	public static final String[] CONCRETE_POWDER_VARIANTS = new String[]
	{
			"white_concrete_powder",
			"orange_concrete_powder",
			"magenta_concrete_powder",
			"light_blue_concrete_powder",
			"yellow_concrete_powder",
			"lime_concrete_powder",
			"pink_concrete_powder",
			"gray_concrete_powder",
			"light_gray_concrete_powder",
			"cyan_concrete_powder",
			"purple_concrete_powder",
			"blue_concrete_powder",
			"brown_concrete_powder",
			"green_concrete_powder",
			"red_concrete_powder",
			"black_concrete_powder"
	};
	
	public static final String[] DYE_VARIANTS = new String[]
	{
			"bone_meal",
			"orange_dye",
			"magenta_dye",
			"light_blue_dye",
			"dandelion_yellow",
			"lime_dye",
			"pink_dye",
			"gray_dye",
			"light_gray_dye",
			"cyan_dye",
			"purple_dye",
			"lapis_lazuli",
			"cocoa_beans",
			"cactus_green",
			"rose_red",
			"ink_sac"
	};
	
	public static final String[] BED_VARIANTS = new String[]
	{
			"white_bed",
			"orange_bed",
			"magenta_bed",
			"light_blue_bed",
			"yellow_bed",
			"lime_bed",
			"pink_bed",
			"gray_bed",
			"light_gray_bed",
			"cyan_bed",
			"purple_bed",
			"blue_bed",
			"brown_bed",
			"green_bed",
			"red_bed",
			"black_bed"
	};
	
	public static final String[] EGG_VARIANTS = new String[]
	{
			"bat_spawn_egg",
			"blaze_spawn_egg",
			"cave_spider_spawn_egg",
			"chicken_spawn_egg",
			"cow_spawn_egg",
			"creeper_spawn_egg",
			"donkey_spawn_egg",
			"elder_guardian_spawn_egg",
			"enderman_spawn_egg",
			"endermite_spawn_egg",
			"evoker_spawn_egg",
			"ghast_spawn_egg",
			"guardian_spawn_egg",
			"horse_spawn_egg",
			"husk_spawn_egg",
			"llama_spawn_egg",
			"magma_cube_spawn_egg",
			"mooshroom_spawn_egg",
			"mule_spawn_egg",
			"ocelot_spawn_egg",
			"parrot_spawn_egg",
			"pig_spawn_egg",
			"polar_bear_spawn_egg",
			"rabbit_spawn_egg",
			"sheep_spawn_egg",
			"shulker_spawn_egg",
			"silverfish_spawn_egg",
			"skeleton_spawn_egg",
			"skeleton_horse_spawn_egg",
			"slime_spawn_egg",
			"spider_spawn_egg",
			"squid_spawn_egg",
			"stray_spawn_egg",
			"vex_spawn_egg",
			"villager_spawn_egg",
			"vindicator_spawn_egg",
			"witch_spawn_egg",
			"wither_skeleton_spawn_egg",
			"wolf_spawn_egg",
			"zombie_spawn_egg",
			"zombie_horse_spawn_egg",
			"zombie_pigman_spawn_egg",
			"zombie_villager_spawn_egg"
	};
	
	public static final String[] POT_VARIANTS = new String[]
	{
			"flower_pot",
			"potted_poppy",
			"potted_dandelion",
			"potted_oak_sapling",
			"potted_spruce_sapling",
			"potted_birch_sapling",
			"potted_jungle_sapling",
			"potted_red_mushroom",
			"potted_brown_mushroom",
			"potted_cactus",
			"potted_dead_bush",
			"potted_fern",
			"potted_acacia_sapling",
			"potted_dark_oak_sapling",
			"potted_blue_orchid",
			"potted_allium",
			"potted_azure_bluet",
			"potted_red_tulip",
			"potted_orange_tulip",
			"potted_white_tulip",
			"potted_pink_tulip",
			"potted_oxeye_daisy"
	};
	
	public static final String[] HEAD_VARIANTS = new String[]
	{
			"skeleton_skull",
			"wither_skeleton_skull",
			"zombie_head",
			"player_head",
			"creeper_head",
			"dragon_head"
	};
	
	public static final String[] WALL_HEAD_VARIANTS = new String[]
	{
			"skeleton_wall_skull",
			"wither_skeleton_wall_skull",
			"zombie_wall_head",
			"player_wall_head",
			"creeper_wall_head",
			"dragon_wall_head"
	};
	
	public static final String[] BANNER_VARIANTS = new String[]
	{
			"white_banner",
			"orange_banner",
			"magenta_banner",
			"light_blue_banner",
			"yellow_banner",
			"lime_banner",
			"pink_banner",
			"gray_banner",
			"light_gray_banner",
			"cyan_banner",
			"purple_banner",
			"blue_banner",
			"brown_banner",
			"green_banner",
			"red_banner",
			"black_banner"
	};
	
	public static final String[] WALL_BANNER_VARIANTS = new String[]
	{
			"white_wall_banner",
			"orange_wall_banner",
			"magenta_wall_banner",
			"light_blue_wall_banner",
			"yellow_wall_banner",
			"lime_wall_banner",
			"pink_wall_banner",
			"gray_wall_banner",
			"light_gray_wall_banner",
			"cyan_wall_banner",
			"purple_wall_banner",
			"blue_wall_banner",
			"brown_wall_banner",
			"green_wall_banner",
			"red_wall_banner",
			"black_wall_banner"
	};	
}
