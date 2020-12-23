package fr.extasio.arcaliabox;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {


	@Override
    public void onEnable(){
		saveDefaultConfig();
		getCommand("setspawnbox").setExecutor(new Commands(this));
		getCommand("spawnbox").setExecutor(new Commands(this));
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), this);
    }
	
	@Override
    public void onDisable(){
    }
	
}
