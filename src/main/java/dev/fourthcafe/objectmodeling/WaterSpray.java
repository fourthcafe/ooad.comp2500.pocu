package dev.fourthcafe.objectmodeling;

public class WaterSpray {
	private final int capacity;
	private int remainingWaterInMl = 0;

	public WaterSpray(int capacity) {
		this.capacity = capacity;
	}


	public int getCapacity() {
		return capacity;
	}


	public int getRemainingWaterInMl() {
		return remainingWaterInMl;
	}


	void addWater(int amountInMl) {
		this.remainingWaterInMl += amountInMl;
		this.remainingWaterInMl = Math.min(this.remainingWaterInMl, capacity);
	}


	void spray() {
		this.remainingWaterInMl -= Math.min(this.remainingWaterInMl, 5);
	}


	void fillUp() {
		this.remainingWaterInMl = capacity;
	}


	public void sprayTo(FlowerPot pot) {
		final int amountWater = Math.min(this.remainingWaterInMl, 5);
		pot.addWater(amountWater);

		this.remainingWaterInMl -= amountWater;
	}
}
