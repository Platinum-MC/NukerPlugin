package com.platinum_mc.a248.nuker;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.util.Vector;

class BukkitNukingConf extends NukingConf {

	private final Configuration config;
	
	private final Logger logger;
	
	BukkitNukingConf(Configuration config) {
		this.config = config;
		logger = Logger.getLogger(getClass().getName());
		/*
		 * Both this and log4j2 configuration are required to actually turn on debug logging
		 */
		logger.setLevel(Level.ALL);
	}
	
	@Override
	int warningTimeSeconds() {
		return config.getInt("warning-time-seconds", super.warningTimeSeconds());
	}
	
	@Override
	long intervalBetweenVolleys() {
		return config.getLong("interval-between-volley", super.intervalBetweenVolleys());
	}
	
	@Override
	int iterationsPerVolley() {
		return config.getInt("iterations-per-volley", super.iterationsPerVolley());
	}
	
	@Override
	Vector fireballVelocity() {
		int y = config.getInt("fireball-velocity", -1);
		if (y == -1) {
			return super.fireballVelocity();
		}
		return new Vector(0, -y, 0);
	}
	
	@Override
	String warningMessage() {
		return config.getString("warning-message", super.warningMessage());
	}
	
	@Override
	String worldDoesNotExist(String world) {
		String msg = config.getString("commands.world-not-exist", super.worldDoesNotExist(world));
		return msg.replace("%WORLD%", world);
	}
	
	@Override
	String noPermission() {
		return config.getString("commands.no-permission", super.noPermission());
	}
	
	@Override
	String usage() {
		return config.getString("commands.usage", super.usage());
	}
	
	@Override
	String notANumber(String input) {
		return config.getString("commands.not-a-number", super.notANumber(input)).replace("%INPUT%", input);
	}
	
}
