package com.platinum_mc.a248.nuker;

import org.bukkit.util.Vector;

public class NukingConf {

	int warningTimeSeconds() {
		return 5;
	}
	
	long intervalBetweenVolleys() {
		return 500L;
	}
	
	int iterationsPerVolley() {
		return 5;
	}
	
	Vector fireballVelocity() {
		return new Vector(0, -5, 0);
	}
	
	String warningMessage() {
		return "&cWarning: The area will be nuked soon.";
	}
	
	String worldDoesNotExist(String world) {
		return "&cWorld &e" + world + "&c does not exist";
	}
	
	String noPermission() {
		return "&cSorry, you cannot use this";
	}
	
	String usage() {
		return "&cUsage: /nuker nuke &e<x> <z> <radius> [world]";
	}
	
	String notANumber(String input) {
		return "&e" + input + " &c is not a number";
	}
	
}
