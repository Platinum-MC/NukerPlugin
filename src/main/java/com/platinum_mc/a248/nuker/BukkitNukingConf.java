package com.platinum_mc.a248.nuker;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.util.Vector;

class BukkitNukingConf extends NukingConf {

	private final Configuration config;
	
	private final Logger logger;
	
	private static final int CONFIG_VERSION = 2;
	
	BukkitNukingConf(Configuration config) {
		this.config = config;
		logger = Logger.getLogger(getClass().getName());
		/*
		 * Both this and log4j2 configuration are required to actually turn on debug logging
		 */
		logger.setLevel(Level.ALL);

		if (config.getInt("config-version", -1) != CONFIG_VERSION) {
			logger.warning("Your configuration is outdated. Regenerate it to get the latest config values.");
		}
	}
	
	@Override
	int amountOfVolleys() {
		return config.getInt("amount-of-volleys", super.amountOfVolleys());
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
		double y = config.getDouble("fireball-velocity", -1.0);
		if (y == -1.0) {
			return super.fireballVelocity();
		}
		return new Vector(0, -y, 0);
	}
	
	@Override
	boolean smallFireball() {
		return config.getBoolean("small-fireballs", super.smallFireball());
	}
	
	@Override
	String warningMessage(int x, int z) {
		return config.getString("warning-message", super.warningMessage(x, z))
				.replace("%X%", Integer.toString(x)).replace("%Z%", Integer.toString(z));
	}
	
	@Override
	int warningBuffer() {
		return config.getInt("warning-buffer", super.warningBuffer());
	}
	
	@Override
	int warningTimeSeconds() {
		return config.getInt("warning-time-seconds", super.warningTimeSeconds());
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
	
	@Override
	int radiusLimit() {
		return config.getInt("radius-limit", super.radiusLimit());
	}
	
	@Override
	String tooLargeRadius(int radius) {
		return config.getString("commands.too-large-radius", super.tooLargeRadius(radius))
				.replace("%RADIUS%", Integer.toString(radius));
	}
	
	@Override
	String negativeRadius() {
		return config.getString("commands.negative-radius", super.negativeRadius());
	}
	
}
