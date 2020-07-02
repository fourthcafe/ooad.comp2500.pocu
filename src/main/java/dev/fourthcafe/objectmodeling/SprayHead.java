package dev.fourthcafe.objectmodeling;

public class SprayHead {
	private int sprayAmount;

	public SprayHead(int sprayAmount) {
		this.sprayAmount = sprayAmount;
	}


	public void sprayFrom(SprayBottle body) {
		body.reduceWater(sprayAmount);
	}
}
