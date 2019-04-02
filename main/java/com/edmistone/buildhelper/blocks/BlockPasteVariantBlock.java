package com.edmistone.buildhelper.blocks;

/** Exactly like the Paste Block only it uses the variant clone
 *  method, cycling variants/colors on paste */
public class BlockPasteVariantBlock extends BlockPasteBlock
{
	public BlockPasteVariantBlock(String unlocalizedName)
	{
		super(unlocalizedName);
	}
	
	@Override
    public boolean useVariantClone()
	{
        return true;
    }
}