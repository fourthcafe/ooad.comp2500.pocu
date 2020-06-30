package dev.fourthcafe.objectmodeling;

public class FlowerPot {
	private boolean alive = true;
	private final int minDailyWaterInMl;
	private int dailyWaterReceived = 0;

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
		this.dailyWaterReceived += amountInMl;
	}


	public void liveAnoterDay() {
		if (this.dailyWaterReceived < this.minDailyWaterInMl) {
			this.alive = false;
		}

		this.dailyWaterReceived = 0;
	}
}
