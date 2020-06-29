package dev.fourthcafe.objectmodeling;

public class Application {

	public static void main(String[] args) {
		final WaterSpray waterSpray = new WaterSpray(100);
		waterSpray.fillUp();

		final FlowerPot pot = new FlowerPot(5);

		int water = waterSpray.getRemainingWaterInMl();
		waterSpray.spray();
		water -= waterSpray.getRemainingWaterInMl();

		pot.addWater(water);

		System.out.printf("pot alive? %s", pot.isAlive());
	}
}
