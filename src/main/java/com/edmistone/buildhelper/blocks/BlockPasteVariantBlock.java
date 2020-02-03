package com.edmistone.buildhelper.blocks;

/** Exactly like the Paste Block only it uses the variant clone
 *  method, cycling variants/colors on paste 
 *  @author Aaron Edmistone */
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