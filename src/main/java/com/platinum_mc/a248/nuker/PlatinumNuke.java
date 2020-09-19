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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

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
	
	void warnThenNuke(NukeOrder order) {
		String worldName = order.getWorld();
		World world = getPlugin().getServer().getWorld(worldName);
		if (world == null) {
			throw new IllegalArgumentException("World " + worldName + " does not exist");
		}
		Location targetLocation = new Location(world, order.getX(), 60, order.getZ());
		String message = ChatColor.translateAlternateColorCodes('&',
				conf.warningMessage(order.getX(), order.getZ()));

		double distanceSquared = order.getRadius() + conf.warningBuffer();
		distanceSquared = distanceSquared * distanceSquared;

		for (Player player : getPlugin().getServer().getOnlinePlayers()) {
			Location playerLoc = player.getLocation();
			if (!playerLoc.getWorld().getName().equalsIgnoreCase(worldName)) {
				continue;
			}
			if (playerLoc.distanceSquared(targetLocation) > distanceSquared) {
				continue;
			}
			player.sendMessage(message);
		}
		runLater(() -> {
			nuke(order, world);
		}, Duration.ofSeconds(conf.warningTimeSeconds()));
	}
	
	private void nuke(NukeOrder order, World world) {
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
		}, Duration.ofMillis(conf.intervalBetweenVolleys()), conf.amountOfVolleys());
	}
	
	private void strikeLightning(Location loc) {
		loc.getWorld().strikeLightning(loc);
	}
	
	private void spawnFireball(Location loc) {
		spawnEntityWithVelocity(loc, EntityType.FIREBALL, conf.fireballVelocity());
	}
	
	private void spawnEntityWithVelocity(Location loc, EntityType type, Vector velocity) {
		Entity entity = loc.getWorld().spawnEntity(loc.setDirection(velocity), type);
		if (entity == null) {
			throw new IllegalStateException(
					"Bukkit refused to spawn entity of type " + type + " at " + loc);
		}
		entity.setVelocity(velocity);
	}
	
	void runLater(Runnable command, Duration delay) {
		getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), command, toTicks(delay));
	}
	
	private void runRepeating(Runnable command, Duration interval, int repeatCount) {
		long ticks = toTicks(interval);
		new BukkitRunnable() {
			
			private int count;
			@Override
			public void run() {
				count++;
				if (count > repeatCount) {
					cancel();
					return;
				}
				command.run();
			}
		}.runTaskTimer(getPlugin(), ticks, ticks);
	}
	
	private long toTicks(Duration duration) {
		return duration.toMillis() / 50L;
	}

	public void close() {
	}
	
}
