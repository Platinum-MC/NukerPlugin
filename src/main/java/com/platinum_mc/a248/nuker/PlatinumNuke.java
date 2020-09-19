package com.platinum_mc.a248.nuker;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

class PlatinumNuke {

	private final JavaPlugin plugin;
	private final Path folder;
	
	private NukingConf conf;
	
	PlatinumNuke(JavaPlugin plugin, Path folder) {
		this.plugin = plugin;
		this.folder = folder;
		conf = new NukingConf();
	}
	
	void reload() {
		try {
			reload0();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
	
	void reload0() throws IOException {
		if (!Files.isDirectory(folder)) {
			Files.createDirectories(folder);
		}
		Path configPath = folder.resolve("config.yml");
		if (!Files.exists(configPath)) {
			try (InputStream inputStream = getClass().getResource("/config.yml").openStream();
					ReadableByteChannel rbc = Channels.newChannel(inputStream);
					FileChannel fc = FileChannel.open(configPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
				fc.transferFrom(rbc, 0, Long.MAX_VALUE);
			}
		}
		try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
			conf = new BukkitNukingConf(YamlConfiguration.loadConfiguration(reader));
		}
	}
	
	JavaPlugin getPlugin() {
		return plugin;
	}
	
	NukingConf getConfig() {
		return conf;
	}
	
	void nukeLater(NukeOrder order) {
		runLater(() -> {
			nuke(order);
		}, Duration.ofSeconds(conf.warningTimeSeconds()));
	}
	
	void nuke(NukeOrder order) {
		String worldName = order.getWorld();
		World world = getPlugin().getServer().getWorld(worldName);
		if (world == null) {
			throw new IllegalArgumentException("World " + worldName + " does not exist");
		}
		runRepeating(() -> {
			ThreadLocalRandom tlr = ThreadLocalRandom.current();
			for (int n = 0; n < conf.iterationsPerVolley(); n++) {
				int x = tlr.nextInt(order.getMinX(), order.getMaxX() + 1);
				int z = tlr.nextInt(order.getMinZ(), order.getMaxZ() + 1);

				if (tlr.nextBoolean()) {
					strikeLightning(world.getHighestBlockAt(x, z).getLocation());
				} else {
					spawnFireball(new Location(world, x, 255, z));
				}
			}
		}, Duration.ofMillis(conf.intervalBetweenVolleys()));
	}
	
	private void strikeLightning(Location loc) {
		loc.getWorld().strikeLightning(loc);
	}
	
	private void spawnFireball(Location loc) {
		spawnEntityWithVelocity(loc, EntityType.FIREBALL, conf.fireballVelocity());
	}
	
	private void spawnEntityWithVelocity(Location loc, EntityType type, Vector velocity) {
		Entity entity = loc.getWorld().spawnEntity(loc, type);
		if (entity == null) {
			throw new IllegalStateException(
					"Bukkit refused to spawn entity of type " + type + " at " + loc);
		}
		entity.setVelocity(velocity);
	}
	
	void runLater(Runnable command, Duration delay) {
		getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), command, toTicks(delay));
	}
	
	private void runRepeating(Runnable command, Duration interval) {
		long ticks = toTicks(interval);
		getPlugin().getServer().getScheduler().runTaskTimer(getPlugin(), command, ticks, ticks);
	}
	
	private long toTicks(Duration duration) {
		return duration.toMillis() / 50L;
	}

	public void close() {
	}
	
}
