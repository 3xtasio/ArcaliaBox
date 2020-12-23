package fr.extasio.arcaliabox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Listeners implements Listener {
	
	public Map<Player, ScoreboardSign> boards = new HashMap<>();
	
	private Main main;
	public Listeners(Main main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
	   Player p = e.getPlayer();
       p.setHealth(20.0);
       p.setFoodLevel(20);
	   p.sendMessage("§7Bienvenue sur le serveur PvPBox !");
	   e.setJoinMessage(null);
	   
	   for(Entry<Player, ScoreboardSign> sign : boards.entrySet()) {
			sign.getValue().setLine(7, "§7Connectés: §a" + Bukkit.getOnlinePlayers().size());
	   
	   }
	   
	   //Scoreboard 
	   ScoreboardSign scoreboard = new ScoreboardSign(p, "§e§lARCALIABOX");
	   scoreboard.create();
	   scoreboard.setLine(1, "§b ");
	   scoreboard.setLine(2, "§7Kills: §c" + p.getStatistic(Statistic.PLAYER_KILLS));
	   scoreboard.setLine(3, "§7Morts: §c" + p.getStatistic(Statistic.DEATHS));
	   scoreboard.setLine(4, "§7Ratio: §c" + ((float) p.getStatistic(Statistic.PLAYER_KILLS)/ (float) p.getStatistic(Statistic.DEATHS)));
	   scoreboard.setLine(5, "§a ");
	   scoreboard.setLine(6, "§a ");
	   scoreboard.setLine(7, "§7Connectés: §a" + Bukkit.getOnlinePlayers().size());
	   scoreboard.setLine(8, "§2 ");  
	   scoreboard.setLine(9, "§e     §6arcalia.lcmc.pro");
	   boards.put(p,scoreboard);
	   
	   //tp spawn
       World w = Bukkit.getServer().getWorld(main.getConfig().getString("spawn.world"));
       double x = main.getConfig().getDouble("spawn.x");
       double y = main.getConfig().getDouble("spawn.y");
       double z = main.getConfig().getDouble("spawn.z");
       p.teleport(new Location(w, x, y, z));
       p.getInventory().clear();
       ItemStack customaxe = new ItemStack(Material.BOOK, 1);
       ItemMeta customM = customaxe.getItemMeta();
       customM.setDisplayName("§aRejoindre §7- §9ArcaliaBox");
       customM.setLore(Arrays.asList(" "));
       customaxe.setItemMeta(customM);
       p.getInventory().setItem(0, customaxe);
       p.updateInventory();
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(null);
		
		for(Entry<Player, ScoreboardSign> sign : boards.entrySet()) {
			sign.getValue().setLine(7, "§7Connectés: §a" + (Bukkit.getOnlinePlayers().size() - 1));
		}
		
		if(boards.containsKey(player)) {
			boards.get(player).destroy();
		}
	}
	
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event){
	event.setFoodLevel(20);
	}
	
	
	@EventHandler
	public void onDeath(PlayerRespawnEvent e) {
		Player victim = e.getPlayer();
		Player attacker = e.getPlayer().getKiller();
		if(boards.containsKey(victim)) {
			boards.get(victim).setLine(3, "§7Morts: §c" + victim.getStatistic(Statistic.DEATHS));
			boards.get(victim).setLine(4, "§7Ratio: §c" + ((float) victim.getStatistic(Statistic.PLAYER_KILLS)/ (float) victim.getStatistic(Statistic.DEATHS)));
		}
		if(boards.containsKey(attacker)) {
			boards.get(attacker).setLine(2, "§7Kills: §c" + attacker.getStatistic(Statistic.PLAYER_KILLS));
			boards.get(attacker).setLine(4, "§7Ratio: §c" + ((float) attacker.getStatistic(Statistic.PLAYER_KILLS)/ (float) attacker.getStatistic(Statistic.DEATHS)));
		}
        World w = Bukkit.getServer().getWorld(main.getConfig().getString("spawn.world"));
        double x = main.getConfig().getDouble("spawn.x");
        double y = main.getConfig().getDouble("spawn.y");
        double z = main.getConfig().getDouble("spawn.z");
        victim.teleport(new Location(w, x, y, z));
        victim.setFoodLevel(20);
        victim.setSaturation(50000);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
    	event.getDrops().clear(); 
        event.setDroppedExp(0);
        				
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
        World w = Bukkit.getServer().getWorld(main.getConfig().getString("spawn.world"));
        double x = main.getConfig().getDouble("spawn.x");
        double y = main.getConfig().getDouble("spawn.y");
        double z = main.getConfig().getDouble("spawn.z");
	    event.setRespawnLocation(new Location(w, x, y, z));
	    p.getInventory().clear();
	    ItemStack customaxe = new ItemStack(Material.BOOK, 1);
	    ItemMeta customM = customaxe.getItemMeta();
	    customM.setDisplayName("§aRejoindre §7- §9ArcaliaBox");
	    customM.setLore(Arrays.asList(" "));
	    customaxe.setItemMeta(customM);
	    p.getInventory().setItem(0, customaxe);
	    p.updateInventory();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		
		Player player = event.getPlayer();
		ItemStack it = event.getItem();
	    
		if(it != null && it.getType() == Material.BOOK){
			
			//stuff
			
			player.getInventory().remove(Material.BOOK);
			ItemStack sword = main.getConfig().getItemStack("stuff.sword");
			player.getInventory().setItem(0, sword);
			ItemStack casque = main.getConfig().getItemStack("stuff.casque");
			player.getInventory().setHelmet(casque);
			ItemStack plastron = main.getConfig().getItemStack("stuff.plastron");
			player.getInventory().setChestplate(plastron);
			ItemStack pantalon = main.getConfig().getItemStack("stuff.pantalon");
			player.getInventory().setLeggings(pantalon);
			ItemStack bottes = main.getConfig().getItemStack("stuff.bottes");
			player.getInventory().setBoots(bottes);
			ItemStack bow = main.getConfig().getItemStack("stuff.bow");
			player.getInventory().setItem(1 ,bow);
			ItemStack arrow = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(35, arrow);
			player.updateInventory();
			
			//tp random
			Random r = new Random();
			int rand = r.nextInt(12);
			int wX = main.getConfig().getInt("Locations." + rand + ".x"),
			wY = main.getConfig().getInt("Locations." + rand + ".y"), 
			wZ = main.getConfig().getInt("Locations." + rand + ".z");
			World w = Bukkit.getServer().getWorld(main.getConfig().getString("spawn.world"));
			player.teleport(new Location (w, wX, wY, wZ));
			System.out.println(wX+" "+wY+" "+wZ);
		    } 
	}
	
}
