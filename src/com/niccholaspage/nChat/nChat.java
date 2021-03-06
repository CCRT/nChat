package com.niccholaspage.nChat;

import java.io.File;
import java.io.IOException;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class nChat extends JavaPlugin {
	private final nChatPlayerListener playerListener = new nChatPlayerListener(this);
    //Permissions Handler
    public PermissionHandler Permissions;
    //Message Format
    public String messageFormat;
    //Color character
    public String colorCharacter;
    //Timestamp format
    public String timestampFormat;
    //Join Message
    public String joinMessage;
    //Leave Message
    public String leaveMessage;
    //Is Permissions 3.0?
    public boolean permissions30;
    @Override
	public void onDisable() {
		System.out.println("nChat Disabled");
		
	}
    @Override
	public void onEnable() {
		//Create the pluginmanage pm.
		PluginManager pm = getServer().getPluginManager();
		//Register events (like a boss)
	    pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Normal, this);
	    pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener,Event.Priority.Normal, this);
	    pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener,Event.Priority.Normal, this);
       //Get the infomation from the yml file.
        PluginDescriptionFile pdfFile = this.getDescription();
        //Setup Permissions
        setupPermissions();
        readConfig();
        //Print that the plugin has been enabled!
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		
	}
    private void readConfig() {
		new File("plugins/nChat/").mkdir();
		try {
			new File("plugins/nChat/config.yml").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Configuration config = new Configuration(new File("plugins/nChat/config.yml"));
    	config.load();
    	writeNode("nChat", "", config);
    	writeNode("nChat.messageformat", "[+prefix+group+suffix&f] +name: +message", config);
    	writeNode("nChat.colorcharacter", "~", config);
    	writeNode("nChat.timestampformat", "hh:mm:ss", config);
    	writeNode("nChat.joinmessage", "", config);
    	writeNode("nChat.leavemessage", "", config);
    	config.save();
    	// Reading from yml file
    	messageFormat = config.getString("nChat.messageformat");
    	colorCharacter = config.getString("nChat.colorcharacter");
    	timestampFormat = config.getString("nChat.timestampformat");
    	joinMessage = config.getString("nChat.joinmessage");
    	leaveMessage = config.getString("nChat.leavemessage");
        }
    private void setupPermissions() {
        Plugin perm = this.getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (perm != null) {
                Permissions = ((Permissions)perm).getHandler();
                permissions30 = perm.getDescription().getVersion().startsWith("3.0");
            } else {
            	System.out.println("[nChat] Permissions not detected, disabling nChat.");
            	getPluginLoader().disablePlugin(this);
            }
    }
    }

	public String replaceSplit(String str, String[] search, String[] replace) {
		if (search.length != replace.length) return "";
		for (int i = 0; i < search.length; i++){
			String[] split = search[i].split(",");
			for (int j = 0; j < split.length; j++){
				str = str.replace(split[j], replace[i]);
			}
		}
		return str;
	}
	
	private void writeNode(String node,Object value, Configuration config){
		if (config.getProperty(node) == null) config.setProperty(node, value);
	}
}
