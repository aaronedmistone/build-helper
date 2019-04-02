package com.edmistone.buildhelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.edmistone.buildhelper.tabs.TabMain;

import net.minecraft.creativetab.CreativeTabs;

/** Information reference class, stores static fields */
public class Info
{
	public static final String MODID = "buildhelper";
	public static final String MODNAME = "Build Helper";
    public static final String VERSION = "1.0.0.0";
    
	public static final String SERVER_PROXY_CLASS = "com.edmistone.buildhelper.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.edmistone.buildhelper.proxy.ClientProxy";
	public static final String GUI_FACTORY = "com.edmistone.buildhelper.config.ConfigGuiFactory";
	public static final String VERSION_CHECKER_URL = "https://raw.githubusercontent.com/AaronEdmistone/BuildHelper/master/update.json";
	
	public static final Logger LOG = LogManager.getLogger(MODID);
    public static final CreativeTabs TAB = new TabMain();
}
