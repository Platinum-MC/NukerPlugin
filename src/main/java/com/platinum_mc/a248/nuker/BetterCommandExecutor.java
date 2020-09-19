package com.platinum_mc.a248.nuker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface BetterCommandExecutor extends CommandExecutor {

	@Override
	default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		runCmd(sender, args);
		return true;
	}
	
	void runCmd(CommandSender sender, String[] args);
	
}
