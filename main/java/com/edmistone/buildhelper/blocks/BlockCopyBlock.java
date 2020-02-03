package com.edmistone.buildhelper.blocks;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.helpers.BlockHelper;
import com.edmistone.buildhelper.helpers.TagHelper;
import com.edmistone.buildhelper.operations.Chat;
import com.edmistone.buildhelper.operations.Compare;
import com.edmistone.buildhelper.operations.Rotate;
import com.edmistone.buildhelper.registry.Items;
import com.edmistone.buildhelper.registry.Sounds;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/** Copy Block can be placed on 2 sides (in a diagonal line) of a section to be copied/rotated.
 *  Sections can be pasted using a paste block or rotated by using build tool on the copy block.
 *  @author Aaron Edmistone */
public class BlockCopyBlock extends Block
{
	public BlockCopyBlock(String unlocalizedName)
	{		
		super(Properties.create(Material.CLAY).hardnessAndResistance(1, 20));
		this.setRegistryName(new ResourceLocation(Info.MODID, unlocalizedName));
	}
	
	public static void clearCopyBlockData(PlayerEntity player, CompoundNBT playerData)
	{
		playerData.put("CopyBlocks", new ListNBT());
		player.writeUnlessRemoved(playerData);
	}
	
	/** Removes this copy block data if assigned to any player*/
	public void handleBlockDestroyed(World world, BlockPos pos)
	{
//		if(world.isRemote)
//			return;
		
		for(PlayerEntity player : world.getPlayers())
		{
			CompoundNBT tagEntityData = player.getPersistentData();
			ListNBT copyBlocks = tagEntityData.getList("CopyBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new ListNBT();
			
			for (int i = 0; i < copyBlocks.toArray().length; i++)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(i));
				if(Compare.BlockPosIsEqual(currentBlockPos, pos))
				{
					copyBlocks.remove(i);
					
					if(!world.isRemote)
						Chat.send(player, "<Red>>Copy block destroyed");
					
					break;
				}
			}
			
			tagEntityData.put("CopyBlocks", copyBlocks);
			player.writeUnlessRemoved(tagEntityData);
		}
	}
	
	
	/** Assigns this copy block to the placer entity and removes old copy blocks if needed*/
	public void handleBlockAdded(LivingEntity placer, World worldIn, BlockPos pos)
	{
//		if(worldIn.isRemote)
//			return;
		
		CompoundNBT tagEntityData = placer.getPersistentData();
		ListNBT copyBlocks = tagEntityData.getList("CopyBlocks", 10);
		
		if(copyBlocks == null)
			copyBlocks = new ListNBT();
		
		if(copyBlocks.toArray().length >= 2)
		{
			for (int i = copyBlocks.toArray().length-1; i >= 0; i--)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(i));
				copyBlocks.remove(i);
				
				if(!worldIn.isRemote)
					BlockHelper.DestroyBlockSilently(currentBlockPos, placer, true);
			}
			if(!worldIn.isRemote)
				Chat.send(placer, "<Red>>Destroyed old copy blocks");
		}
		
		copyBlocks.add(TagHelper.BlockPosToCompoundTag(pos));
		
		if(!worldIn.isRemote)
			Chat.send(placer, "<Blue>Added new copy block [" + copyBlocks.toArray().length + "/2]");
		
		tagEntityData.put("CopyBlocks", copyBlocks);
		placer.writeUnlessRemoved(tagEntityData);
	}
	
	
	/** If the activating player is holding the build tool, rotates the copy region */
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
					Sounds.BUILD_TOOL_ROTATE,
					SoundCategory.PLAYERS,
					0.5F,
					player.world.rand.nextFloat() * 0.1F + 0.9F);
			
			if(player.world.isRemote)
				return;
			
			CompoundNBT tagEntityData = player.getPersistentData();
			ListNBT copyBlocks = tagEntityData.getList("CopyBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new ListNBT();
			
			if(copyBlocks.toArray().length == 2)
			{
				Rotate.rotate(
						TagHelper.ReadBlockPos(copyBlocks.getCompound(0)),
						TagHelper.ReadBlockPos(copyBlocks.getCompound(1)).add(0,50,0),
						player,
						player.world);
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