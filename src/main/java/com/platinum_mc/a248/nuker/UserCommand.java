package com.platinum_mc.a248.nuker;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class UserCommand extends NukerCommand {

	UserCommand(PlatinumNuke core) {
		super(core);
	}
	
	@Override
	public void runCmd(CommandSender sender, String[] args) {
		if (!sender.hasPermission("pmcnuker.use")) {
			sendMessage(sender, core.getConfig().noPermission());
			return;
		}
		if (args.length < 3) {
			sendUsage(sender);
			return;
		}
		String world;
		if (args.length == 4) {
			world = args[3];
			if (core.getPlugin().getServer().getWorld(world) == null) {
				sendMessage(sender, core.getConfig().worldDoesNotExist(world));
				return;
			}
		} else {
			if (!(sender instanceof Player)) {
				sendMessage(sender, "&cWhen run from the console, the world argument is required.");
				sendUsage(sender);
				return;
			}
			world = ((Player) sender).getWorld().getName();
		}
		int x;
		try {
			x = Integer.parseInt(args[0]);
		} catch (NumberFormatException notANumber) {
			sendNotANumber(sender, args[0]);
			return;
		}
		int z;
		try {
			z = Integer.parseInt(args[1]);
		} catch (NumberFormatException notANumber) {
			sendNotANumber(sender, args[1]);
			return;
		}
		int radius;
		try {
			radius = Integer.parseInt(args[2]);
		} catch (NumberFormatException notANumber) {
			sendNotANumber(sender, args[2]);
			return;
		}
		NukeOrder order = new NukeOrder(world, x, z, radius);
		core.warnThenNuke(order);
	}
	
	private void sendNotANumber(CommandSender sender, String input) {
		sendMessage(sender, core.getConfig().notANumber(input));
	}
	
	private void sendUsage(CommandSender sender) {
		sendMessage(sender, core.getConfig().usage());
	}

}
