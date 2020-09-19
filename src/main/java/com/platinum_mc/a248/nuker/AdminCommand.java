package com.platinum_mc.a248.nuker;

import java.util.Locale;

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

		if (args.length > 0) {
			switch (args[0].toLowerCase(Locale.ENGLISH)) {
			case "reload":
				core.reload();
				sendMessage(sender, "&aReloaded");
				return;
			default:
				break;
			}
		}
		sendMessage(sender, "&cUnknown admin command.");
		
	}

}
