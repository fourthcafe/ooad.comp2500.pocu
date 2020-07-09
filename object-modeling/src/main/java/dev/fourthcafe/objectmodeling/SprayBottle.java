package dev.fourthcafe.objectmodeling;

public class SprayBottle {
	private final int capacity;
	private int remainingWater = 0;


	public SprayBottle(int capacity) {
		this.capacity = capacity;
	}


	public int getRemainingWater() {
		return this.remainingWater;
	}


	public int getCapacity() {
		return capacity;
	}


	public void addWater(int amountInMl) {
		this.remainingWater += amountInMl;
		this.remainingWater = Math.min(remainingWater, capacity);
	}


	public void reduceWater(int amountInMl) {
		this.remainingWater -= amountInMl;
		this.remainingWater = Math.max(remainingWater, 0);
	}


	public void fillUp() {
		this.remainingWater = this.capacity;
	}
}
