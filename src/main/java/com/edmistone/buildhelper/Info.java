package com.edmistone.buildhelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.edmistone.buildhelper.tabs.TabMain;
import net.minecraft.item.ItemGroup;

/** Information reference class, stores static fields 
 *  @author Aaron Edmistone */
public class Info
{
	public static final String MODID = "buildhelper";
	public static final String MODNAME = "Build Helper";
    public static final String VERSION = "1.1.0.0";
    
	public static final String SERVER_PROXY_CLASS = "com.edmistone.buildhelper.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.edmistone.buildhelper.proxy.ClientProxy";
	
	public static final Logger LOG = LogManager.getLogger(MODID);
    public static final ItemGroup TAB = new TabMain();
}
