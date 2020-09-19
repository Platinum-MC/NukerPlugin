package com.platinum_mc.a248.nuker;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

abstract class NukerCommand implements BetterCommandExecutor {

	final PlatinumNuke core;
	
	NukerCommand(PlatinumNuke core) {
		this.core = core;
	}
	
	void sendMessage(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
}
