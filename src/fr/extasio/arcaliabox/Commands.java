package fr.extasio.arcaliabox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	private Main main;
	public Commands(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(sender instanceof Player) {
			Player player = (Player)sender;
			
			if(cmd.getName().equalsIgnoreCase("stats")){
				player.sendMessage(main.getConfig().getString("messages.stats"));
				return true;
			}
				
			if (cmd.getName().equalsIgnoreCase("setspawnbox")){
				main.getConfig().set("spawn.world", player.getLocation().getWorld().getName());
				main.getConfig().set("spawn.x", player.getLocation().getX());
				main.getConfig().set("spawn.y", player.getLocation().getY());
				main.getConfig().set("spawn.z", player.getLocation().getZ());
				main.saveConfig();
                player.sendMessage("§cSpawn défini!");
                return true;
                
			}
            if (cmd.getName().equalsIgnoreCase("spawnbox")) {
                if (main.getConfig().getConfigurationSection("spawn") == null) {
                    player.sendMessage(ChatColor.RED + "Le spawn n'est pas défini.");
                    return true;
                }
                World w = Bukkit.getServer().getWorld(main.getConfig().getString("spawn.world"));
                double x = main.getConfig().getDouble("spawn.x");
                double y = main.getConfig().getDouble("spawn.y");
                double z = main.getConfig().getDouble("spawn.z");
                player.teleport(new Location(w, x, y, z));
          
            }

		}
	return true;

	}
}
