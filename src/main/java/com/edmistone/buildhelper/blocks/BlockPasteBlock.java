package com.edmistone.buildhelper.blocks;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.helpers.BlockHelper;
import com.edmistone.buildhelper.helpers.TagHelper;
import com.edmistone.buildhelper.operations.ChestSearch;
import com.edmistone.buildhelper.operations.Chat;
import com.edmistone.buildhelper.operations.Clone;
import com.edmistone.buildhelper.operations.Compare;
import com.edmistone.buildhelper.registry.Items;
import com.edmistone.buildhelper.registry.Sounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/** Paste Block when placed and activated with a build tool will clone the copy region (if set)
 *  to the paste block location in the shown direction (paste blocks visually show the direction)
 *  @author Aaron Edmistone */
public class BlockPasteBlock extends Block
{
	protected static VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 15.0D, 7.0D, 15.0D);//Block.makeCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D);
	
	public BlockPasteBlock(String unlocalizedName)
	{
		super(Properties.create(Material.CLAY).hardnessAndResistance(1, 20));
		this.setRegistryName(new ResourceLocation(Info.MODID, unlocalizedName));
	}
	
	public static void clearPasteBlockData(PlayerEntity player, CompoundNBT playerData)
	{
		playerData.put("PasteBlocks", new ListNBT());
		player.writeUnlessRemoved(playerData);
	}
	
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }
    
    public boolean useVariantClone()
    {
    	return false;
    }
	
    /** Removes this paste block data if assigned to any player*/
	public void handleBlockDestroyed(World world, BlockPos pos)
	{
		
//		if(world.isRemote)
//			return;
		
		for(PlayerEntity player : world.getPlayers())
		{
			CompoundNBT tagEntityData = player.getPersistentData();
			ListNBT pasteBlocks = tagEntityData.getList("PasteBlocks", 10);
			
			if(pasteBlocks == null)
				pasteBlocks = new ListNBT();
			
			for (int i = 0; i < pasteBlocks.toArray().length; i++)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompound(i));
				if(Compare.BlockPosIsEqual(currentBlockPos, pos))
				{
					pasteBlocks.remove(i);
					
					if(!world.isRemote)
						player.sendMessage(new StringTextComponent(TextFormatting.RED + ">Removed paste block"));
					
					break;
				}
			}
			
			tagEntityData.put("PasteBlocks", pasteBlocks);
			player.writeUnlessRemoved(tagEntityData);
		}
	}
	
	
	/** Assigns this paste block to the placer entity and removes old paste block if needed*/
	public void handleBlockAdded(LivingEntity placer, World worldIn, BlockPos pos)
	{
//		if(worldIn.isRemote)
//			return;
		
		CompoundNBT tagEntityData = placer.getPersistentData();
		ListNBT pasteBlocks = tagEntityData.getList("PasteBlocks", 10);
		
		if(pasteBlocks == null)
			pasteBlocks = new ListNBT();
		
		if(pasteBlocks.toArray().length >= 1)
		{
			for (int i = pasteBlocks.toArray().length-1; i >= 0; i--)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompound(i));
				pasteBlocks.remove(i);
				
				if(!worldIn.isRemote)
					BlockHelper.DestroyBlockSilently(currentBlockPos, placer, true);
			}
			
			if(!worldIn.isRemote)
				placer.sendMessage(new StringTextComponent(TextFormatting.RED + ">Destroyed old paste block"));
		}
		
		pasteBlocks.add(TagHelper.BlockPosToCompoundTag(pos));
		
		tagEntityData.put("PasteBlocks", pasteBlocks);
		placer.writeUnlessRemoved(tagEntityData);
	}
	
	/** If the activating player is holding the build tool, pastes the copy region */
	public void handleBlockActivated(PlayerEntity player, Hand hand, BlockPos pos)
	{
		if(player.getHeldItem(hand) == null)
			return;
		
		if(player.getHeldItem(hand).getItem() == null)
			return;
		
		if (player.getHeldItem(hand).getItem() == Items.buildTool)
		{
			player.world.playSound(
					player,
					player.getPosition(),
					Sounds.BUILD_TOOL_USE,
					SoundCategory.PLAYERS,
					0.5F,
					player.world.rand.nextFloat() * 0.1F + 0.9F);
			
			
			
			CompoundNBT tagEntityData = player.getPersistentData();
			
			int symmetryMode = tagEntityData.getInt("SymmetryMode");
			if(symmetryMode != 0)
			{
				if(!player.world.isRemote)
					Chat.send(player, "<Red>Cannot use paste blocks while in `Symmetry Mode`");
				return;
			}
			
			ListNBT copyBlocks = tagEntityData.getList("CopyBlocks", 10);
			ListNBT pasteBlocks = tagEntityData.getList("PasteBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new ListNBT();
			
			if(pasteBlocks == null)
				pasteBlocks = new ListNBT();
			
			if(copyBlocks.toArray().length == 2 && pasteBlocks.toArray().length == 1)
			{
				BlockPos pasteBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompound(0));
				
				if(!ChestSearch.containsItemsForClone(
						true,
						player.world,
						player,
						pasteBlockPos.add(-1,0,0),
						TagHelper.ReadBlockPos(copyBlocks.getCompound(0)),
						TagHelper.ReadBlockPos(copyBlocks.getCompound(1)).add(0,50,0),
						pasteBlockPos))
				{
					if(!player.world.isRemote)
						Chat.send(player, "<Aqua>You do not have the required parts in a chest attached to the paste block... Check your inventory for a <Gold>book of missing parts!");
					
					return;
				}
				
				BlockHelper.DestroyBlockSilently(pasteBlockPos, player, true);
				pasteBlocks.remove(0);
				
				if(!player.world.isRemote)
				{
					Clone.clone(
							TagHelper.ReadBlockPos(copyBlocks.getCompound(0)),
							TagHelper.ReadBlockPos(copyBlocks.getCompound(1)).add(0,50,0),
							pasteBlockPos,
							Clone.CloneMode.FORCE,
							player,
							player.world,
							useVariantClone());
				}
				
				tagEntityData.put("PasteBlocks", pasteBlocks);
				player.writeUnlessRemoved(tagEntityData);
			}
		}
	}
	
	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state)
    {
		handleBlockDestroyed(worldIn.getWorld(), pos);
    }
	
	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
    {
		handleBlockDestroyed(worldIn, pos);
    }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
		handleBlockAdded(placer, worldIn, pos);
    }
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
		handleBlockDestroyed(worldIn, pos);
    }
	
	@Override
	public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
	{
		handleBlockActivated(player, hand, blockPos);
		return ActionResultType.PASS;
	}
}