package com.edmistone.buildhelper.helpers;

import java.lang.reflect.Field;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

public class BlockStateHelper
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> BlockState copyProperties(BlockState source, BlockState target) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		 
		Field sourcePropsField = source.getClass().getSuperclass().getDeclaredField("properties");
		sourcePropsField.setAccessible(true);
		ImmutableMap<IProperty<?>, Comparable<?>> sourceProps = (ImmutableMap<IProperty<?>, Comparable<?>>) sourcePropsField.get(source);
		
		Field targetPropToStateMapField = target.getClass().getSuperclass().getDeclaredField("propertyToStateMap");
		targetPropToStateMapField.setAccessible(true);
		Table<IProperty<?>, Comparable<?>, T> targetPropsToStateMap = (Table<IProperty<?>, Comparable<?>, T>) targetPropToStateMapField.get(target);
		
		
		for(IProperty<?> prop : sourceProps.keySet())
		{
			Set<IProperty<?>> rows = targetPropsToStateMap.rowKeySet();
			
			for(IProperty<?> row : rows)
			{
				if(!row.getName().equals(prop.getName()))
					continue;
				
				Comparable value = sourceProps.get(prop);
				if(value == null)
					continue;
				
				if(!row.getAllowedValues().contains(value))
				{
					System.out.format("\n%s does not allow value `%s` for prop `%s`\n", target.getBlock().getRegistryName().getPath(), value.toString(), row.getName());
					break;
				}
				
				target = target.with((IProperty)row, (Comparable)value);
				break;
			}
		}
		
		
		
		
		return target;
	}
}
