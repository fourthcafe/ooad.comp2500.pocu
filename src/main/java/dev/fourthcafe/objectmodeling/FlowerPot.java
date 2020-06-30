package dev.fourthcafe.objectmodeling;

public class FlowerPot {
	private boolean alive = true;
	private final int minDailyWaterInMl;

	public FlowerPot(int minDailyWaterInMl) {
		this.minDailyWaterInMl = minDailyWaterInMl;
	}

	public boolean isAlive() {
		return alive;
	}

	public int getMinDailyWaterInMl() {
		return minDailyWaterInMl;
	}


	public void addWater(int amountInMl) {
		if (amountInMl < minDailyWaterInMl) {
			alive = false;
		}
	}
}
