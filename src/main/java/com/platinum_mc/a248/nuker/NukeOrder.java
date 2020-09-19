package com.platinum_mc.a248.nuker;

import java.util.Objects;

final class NukeOrder {
	
	private final String world;
	private final int x;
	private final int z;
	
	private final int radius;
	
	NukeOrder(String world, int x, int z, int radius) {
		this.world = Objects.requireNonNull(world, "world");
		this.x = x;
		this.z = z;
		this.radius = radius;
	}
	
	public String getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public int getRadius() {
		return radius;
	}
	
	int getMaxX() {
		return Math.max(getX() + getRadius(), getX() - getRadius());
	}
	
	int getMinX() {
		return Math.min(getX() + getRadius(), getX() - getRadius());
	}
	
	int getMaxZ() {
		return Math.max(getZ() + getRadius(), getZ() - getRadius());
	}
	
	int getMinZ() {
		return Math.min(getZ() + getRadius(), getZ() - getRadius());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + radius;
		result = prime * result + x;
		result = prime * result + z;
		result = prime * result + world.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof NukeOrder)) {
			return false;
		}
		NukeOrder other = (NukeOrder) object;
		return radius == other.radius
				&& x == other.x
				&& z == other.z
				&& world.equals(other.world);
	}

	@Override
	public String toString() {
		return "NukeOrder [world= " + world + ", x=" + x + ", z=" + z + ", radius=" + radius + "]";
	}
	
}
