package com.edmistone.buildhelper.operations;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.edmistone.buildhelper.helpers.BlockHelper;
import com.edmistone.buildhelper.helpers.MutableBoundingBoxHelper;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;

/** Contains methods for cloning blocks, entities and other things 
 *  @author Aaron Edmistone */
public class Clone
{
	public enum CloneMode
	{
		NORMAL,
		FORCE,
		MOVE
	}
	
	/** Clones region of source to destination. Modified version of the clone command method. */
	@SuppressWarnings("deprecation")
	public static void clone(BlockPos sourceBlockPosStart, BlockPos sourceBlockPosEnd, BlockPos destinationBlockPos, CloneMode cloneMode, PlayerEntity player, World world, boolean useVariants)
	{
		MutableBoundingBox sourceBoundingBox = MutableBoundingBoxHelper.reduceBoundingBox(new MutableBoundingBox(sourceBlockPosStart, sourceBlockPosEnd));
		MutableBoundingBox destinationBoundingBox = new MutableBoundingBox(destinationBlockPos, destinationBlockPos.add(sourceBoundingBox.getLength()));
        
        int totalBlocks = sourceBoundingBox.getXSize() * sourceBoundingBox.getYSize() * sourceBoundingBox.getZSize();

        if (totalBlocks > 32768)
        {
        	Chat.send(player, "<Red>BUILD TOOL: Build area too large (> 32768 blocks)");
        	return;
        }

        if (cloneMode != CloneMode.FORCE && cloneMode!= CloneMode.MOVE && sourceBoundingBox.intersectsWith(destinationBoundingBox))
        {
        	Chat.send(player, "<Red>BUILD TOOL: The destination area is occupied");
        	return;
        }
        	
    	if (sourceBoundingBox.minY < 0 || sourceBoundingBox.maxY >= 256 || destinationBoundingBox.minY < 0 || destinationBoundingBox.maxY >= 256
    			|| !world.isAreaLoaded(sourceBlockPosStart, sourceBlockPosEnd) || !world.isAreaLoaded(destinationBlockPos,  destinationBlockPos.add(sourceBoundingBox.getLength())))
        {
    		Chat.send(player, "<Red>BUILD TOOL: Out of bounds");
    		return;
        }

        List<StaticCloneData> fullBlocks = Lists.<StaticCloneData>newArrayList();
        List<StaticCloneData> tileEntityBlocks = Lists.<StaticCloneData>newArrayList();
        List<StaticCloneData> partialBlocks = Lists.<StaticCloneData>newArrayList();
        Deque<BlockPos> oldSourceBlocks = Lists.<BlockPos>newLinkedList();
        
        BlockPos sourceDestinationDifference = new BlockPos(
        				destinationBoundingBox.minX - sourceBoundingBox.minX,
        				destinationBoundingBox.minY - sourceBoundingBox.minY,
        				destinationBoundingBox.minZ - sourceBoundingBox.minZ);

        for (int z = sourceBoundingBox.minZ; z <= sourceBoundingBox.maxZ; ++z)
        {
            for (int y = sourceBoundingBox.minY; y <= sourceBoundingBox.maxY; ++y)
            {
                for (int x = sourceBoundingBox.minX; x <= sourceBoundingBox.maxX; ++x)
                {
                    BlockPos currentSourceBlock = new BlockPos(x, y, z);
                    BlockPos currentDestinationBlock = currentSourceBlock.add(sourceDestinationDifference);
                    BlockState currentBlockState = world.getBlockState(currentSourceBlock);

                    if (currentBlockState.getBlock() != net.minecraft.block.Blocks.AIR)
                    {
                        TileEntity tileentity = world.getTileEntity(currentSourceBlock);
                        if (tileentity != null)
                        {                        	
                        	CompoundNBT nbttagcompound = tileentity.write(new CompoundNBT());
                            tileEntityBlocks.add(new StaticCloneData(currentDestinationBlock, currentBlockState, nbttagcompound));
                            oldSourceBlocks.addLast(currentSourceBlock);
                        }
                        else if (!currentBlockState.isSolid())
                        {
                            partialBlocks.add(new StaticCloneData(currentDestinationBlock, currentBlockState, (CompoundNBT)null));
                            oldSourceBlocks.addFirst(currentSourceBlock);
                        }
                        else
                        {
                            fullBlocks.add(new StaticCloneData(currentDestinationBlock, currentBlockState, (CompoundNBT)null));
                            oldSourceBlocks.addLast(currentSourceBlock);
                        }
                    }
                }
            }
        }

        if (cloneMode == CloneMode.MOVE)
        {
            for (BlockPos oldSourceBlock : oldSourceBlocks)
            {
                TileEntity oldSourceTileEntity = world.getTileEntity(oldSourceBlock);

                if (oldSourceTileEntity instanceof IInventory)
                {
                    ((IInventory)oldSourceTileEntity).clear();
                }

                world.setBlockState(oldSourceBlock, net.minecraft.block.Blocks.BARRIER.getDefaultState(), 2);
            }

            for (BlockPos oldSourceBlock : oldSourceBlocks)
            {
                world.setBlockState(oldSourceBlock, net.minecraft.block.Blocks.AIR.getDefaultState(), 3);
            }
        }
        
        List<StaticCloneData> blocksToClone = Lists.<StaticCloneData>newArrayList();
        blocksToClone.addAll(fullBlocks);
        blocksToClone.addAll(tileEntityBlocks);
        blocksToClone.addAll(partialBlocks);
        List<StaticCloneData> allSourceBlocksReversed = Lists.<StaticCloneData>reverse(blocksToClone);

        for (StaticCloneData sourceBlock : allSourceBlocksReversed)
        {
            TileEntity sourceTileEntity = world.getTileEntity(sourceBlock.pos);

            if (sourceTileEntity instanceof IInventory)
            {
                ((IInventory)sourceTileEntity).clear();
            }

            world.setBlockState(sourceBlock.pos, net.minecraft.block.Blocks.BARRIER.getDefaultState(), 2);
        }

        int pastedBlocks = 0;
        int variantCycles = ThreadLocalRandom.current().nextInt(10);
        
        for (StaticCloneData nextBlock : blocksToClone)
        {
        	if(useVariants)
        	{
        		if(BlockHelper.setBlockStateWithVariantCycle(world, nextBlock.pos, nextBlock.blockState, variantCycles))
        			++pastedBlocks;
        	}
        	else
            {
        		if(world.setBlockState(nextBlock.pos, nextBlock.blockState, 2))
        			++pastedBlocks;
            }
        }

        for (StaticCloneData nextTileEntityBlock : tileEntityBlocks)
        {
            TileEntity nextTileEntity = world.getTileEntity(nextTileEntityBlock.pos);

            if (nextTileEntityBlock.nbt != null && nextTileEntity != null)
            {
                nextTileEntityBlock.nbt.putInt("x", nextTileEntityBlock.pos.getX());
                nextTileEntityBlock.nbt.putInt("y", nextTileEntityBlock.pos.getY());
                nextTileEntityBlock.nbt.putInt("z", nextTileEntityBlock.pos.getZ());
                nextTileEntity.read(nextTileEntityBlock.nbt);
                nextTileEntity.markDirty();
            }
            
            if(useVariants)
        	{
        		if(BlockHelper.setBlockStateWithVariantCycle(world, nextTileEntityBlock.pos, nextTileEntityBlock.blockState, variantCycles))
        			++pastedBlocks;
        	}
        	else
            {
        		if(world.setBlockState(nextTileEntityBlock.pos, nextTileEntityBlock.blockState, 2))
        			++pastedBlocks;
            }
        }
        
        for (StaticCloneData nextBlock : allSourceBlocksReversed)
        {
            world.notifyNeighbors(nextBlock.pos, nextBlock.blockState.getBlock());
        }

        Chat.send(player, (totalBlocks > 0 || pastedBlocks == totalBlocks) ? "<Green>Success" : "<Red>Failure");
	}
	
	static class StaticCloneData
    {
        public final BlockPos pos;
        public final BlockState blockState;
        public final CompoundNBT nbt;

        public StaticCloneData(BlockPos posIn, BlockState stateIn, CompoundNBT compoundIn)
        {
            this.pos = posIn;
            this.blockState = stateIn;
            this.nbt = compoundIn;
        }
    }
}
