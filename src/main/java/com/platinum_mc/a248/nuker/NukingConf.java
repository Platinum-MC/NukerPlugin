package com.platinum_mc.a248.nuker;

import org.bukkit.util.Vector;

public class NukingConf {
	
	int amountOfVolleys() {
		return 0;
	}
	
	long intervalBetweenVolleys() {
		return 500L;
	}
	
	int iterationsPerVolley() {
		return 5;
	}
	
	Vector fireballVelocity() {
		return new Vector(0, -2.5, 0);
	}
	
	boolean smallFireball() {
		return false;
	}
	
	String warningMessage(int x, int z) {
		return "&cWarning: The area around " + x + ", " + z + " will be nuked soon.";
	}
	
	int warningBuffer() {
		return 20;
	}
	
	int warningTimeSeconds() {
		return 5;
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
