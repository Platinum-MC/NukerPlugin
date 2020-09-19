package com.platinum_mc.a248.nuker;

import org.bukkit.plugin.java.JavaPlugin;

public class PlatinumNukePlugin extends JavaPlugin {

	private PlatinumNuke core;
	
	@Override
	public synchronized void onEnable() {
		if (core != null) {
			throw new IllegalStateException("Already enabled");
		}
		core = new PlatinumNuke(this, getDataFolder().toPath());
		core.reload();
		getCommand("nuke").setExecutor(new UserCommand(core));
		getCommand("nukeradmin").setExecutor(new AdminCommand(core));
		getLogger().info("Enabled and ready");
	}
	
	@Override
	public synchronized void onDisable() {
		if (core == null) {
			getLogger().warning("Not shutting down: Nothing to shut down");
			return;
		}
		core.close();
	}
	
}
