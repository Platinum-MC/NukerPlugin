package com.platinum_mc.a248.nuker;

import org.bukkit.command.CommandSender;

class AdminCommand extends NukerCommand {

	AdminCommand(PlatinumNuke core) {
		super(core);
	}

	@Override
	public void runCmd(CommandSender sender, String[] args) {
		if (!sender.hasPermission("pmcnuker.admin")) {
			sendMessage(sender, core.getConfig().noPermission());
			return;
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			core.reload();
			sendMessage(sender, "&aReloaded");
			return;
		}
		sendMessage(sender, "&cUnknown admin command.");
	}

}
