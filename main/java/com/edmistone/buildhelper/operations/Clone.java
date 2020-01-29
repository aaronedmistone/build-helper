package com.edmistone.buildhelper.operations;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.edmistone.buildhelper.helpers.BlockHelper;
import com.edmistone.buildhelper.helpers.MutableBoundingBoxHelper;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
	public static void clone(BlockPos sourceBlockPosStart, BlockPos sourceBlockPosEnd, BlockPos destinationBlockPos, CloneMode cloneMode, EntityPlayer player, World world, boolean variant)
	{
		MutableBoundingBox sourceBoundingBox = MutableBoundingBoxHelper.reduceBoundingBox(new MutableBoundingBox(sourceBlockPosStart, sourceBlockPosEnd));
		MutableBoundingBox destinationBoundingBox = new MutableBoundingBox(destinationBlockPos, destinationBlockPos.add(sourceBoundingBox.getLength()));
        
        int totalBlocks = sourceBoundingBox.getXSize() * sourceBoundingBox.getYSize() * sourceBoundingBox.getZSize();

        if (totalBlocks > 32768)
        {
        	player.sendMessage(new TextComponentString(TextFormatting.RED + "BUILD TOOL: Build area too large (> 32768 blocks)"));
        	return;
        }

        if (cloneMode != CloneMode.FORCE && cloneMode!= CloneMode.MOVE && sourceBoundingBox.intersectsWith(destinationBoundingBox))
        {
        	player.sendMessage(new TextComponentString(TextFormatting.RED + "BUILD TOOL: The destination area is occupied"));
        	return;
        }
        	
    	if (sourceBoundingBox.minY < 0 || sourceBoundingBox.maxY >= 256 || destinationBoundingBox.minY < 0 || destinationBoundingBox.maxY >= 256
    			|| !world.isAreaLoaded(sourceBoundingBox) || !world.isAreaLoaded(destinationBoundingBox))
        {
    		player.sendMessage(new TextComponentString(TextFormatting.RED + "BUILD TOOL: Out of bounds"));
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
                    IBlockState currentBlockState = world.getBlockState(currentSourceBlock);

                    if (currentBlockState.getBlock() != Blocks.AIR)
                    {
                        TileEntity tileentity = world.getTileEntity(currentSourceBlock);

                        if (tileentity != null)
                        {
                            NBTTagCompound nbttagcompound = tileentity.write(new NBTTagCompound());
                            tileEntityBlocks.add(new StaticCloneData(currentDestinationBlock, currentBlockState, nbttagcompound));
                            oldSourceBlocks.addLast(currentSourceBlock);
                        }
                        else if (!currentBlockState.isFullCube())
                        {
                            partialBlocks.add(new StaticCloneData(currentDestinationBlock, currentBlockState, (NBTTagCompound)null));
                            oldSourceBlocks.addFirst(currentSourceBlock);
                        }
                        else
                        {
                            fullBlocks.add(new StaticCloneData(currentDestinationBlock, currentBlockState, (NBTTagCompound)null));
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

                world.setBlockState(oldSourceBlock, Blocks.BARRIER.getDefaultState(), 2);
            }

            for (BlockPos oldSourceBlock : oldSourceBlocks)
            {
                world.setBlockState(oldSourceBlock, Blocks.AIR.getDefaultState(), 3);
            }
        }
        
        List<StaticCloneData> allSourceBlocks = Lists.<StaticCloneData>newArrayList();
        allSourceBlocks.addAll(fullBlocks);
        allSourceBlocks.addAll(tileEntityBlocks);
        allSourceBlocks.addAll(partialBlocks);
        List<StaticCloneData> allSourceBlocksReversed = Lists.<StaticCloneData>reverse(allSourceBlocks);

        for (StaticCloneData sourceBlock : allSourceBlocksReversed)
        {
            TileEntity sourceTileEntity = world.getTileEntity(sourceBlock.pos);

            if (sourceTileEntity instanceof IInventory)
            {
                ((IInventory)sourceTileEntity).clear();
            }

            world.setBlockState(sourceBlock.pos, Blocks.BARRIER.getDefaultState(), 2);
        }

        totalBlocks = 0;
        
        int randomCycleA = ThreadLocalRandom.current().nextInt(0, 10);
		int randomCycleB = ThreadLocalRandom.current().nextInt(0, 10);
        
        for (StaticCloneData sourceBlock : allSourceBlocks)
        {
            if (world.setBlockState(sourceBlock.pos, sourceBlock.blockState, 2))
            {
            	if(variant)
            		BlockHelper.cycleBlockVariant(world, sourceBlock.pos, randomCycleA, randomCycleB);
            	
                ++totalBlocks;
            }
        }

        for (StaticCloneData sourceTileEntities : tileEntityBlocks)
        {
            TileEntity currentSourceTileEntity = world.getTileEntity(sourceTileEntities.pos);

            if (sourceTileEntities.nbt != null && currentSourceTileEntity != null)
            {
                sourceTileEntities.nbt.setInt("x", sourceTileEntities.pos.getX());
                sourceTileEntities.nbt.setInt("y", sourceTileEntities.pos.getY());
                sourceTileEntities.nbt.setInt("z", sourceTileEntities.pos.getZ());
                currentSourceTileEntity.read(sourceTileEntities.nbt);
                currentSourceTileEntity.markDirty();
            }

            world.setBlockState(sourceTileEntities.pos, sourceTileEntities.blockState, 2);
        }

        for (StaticCloneData currentSourceBlock : allSourceBlocksReversed)
        {
            world.notifyNeighbors(currentSourceBlock.pos, currentSourceBlock.blockState.getBlock());
        }

//        List<NextTickListEntry> sourceBlocksPendingUpdate = world.getPendingBlockUpdates(sourceBoundingBox, false);
//
//        if (sourceBlocksPendingUpdate != null)
//        {
//            for (NextTickListEntry currentBlockPendingUpdate : sourceBlocksPendingUpdate)
//            {
//                if (sourceBoundingBox.isVecInside(currentBlockPendingUpdate.position))
//                {
//                    BlockPos destinationBlockPosition = currentBlockPendingUpdate.position.add(sourceDestinationDifference);
//                    world.scheduleBlockUpdate(destinationBlockPosition, currentBlockPendingUpdate.getBlock(), (int)(currentBlockPendingUpdate.scheduledTime - world.getWorldInfo().getWorldTotalTime()), currentBlockPendingUpdate.priority);
//                }
//            }
//        }
        
        player.sendMessage(new TextComponentString(
        		(totalBlocks > 0 ? TextFormatting.GREEN + "BUILD TOOL: Success" : TextFormatting.RED + "BUILD TOOL: Failed") +
        		", cloned " + totalBlocks + " blocks"));
	}
	
	static class StaticCloneData
    {
        public final BlockPos pos;
        public final IBlockState blockState;
        public final NBTTagCompound nbt;

        public StaticCloneData(BlockPos posIn, IBlockState stateIn, NBTTagCompound compoundIn)
        {
            this.pos = posIn;
            this.blockState = stateIn;
            this.nbt = compoundIn;
        }
    }
}
