package dev.fourthcafe.objectmodeling;

public class WaterSpray {
	private SprayHead head;
	private SprayBottle body;

	public WaterSpray(SprayHead head, SprayBottle body) {
		this.head = head;
		this.body = body;
	}


	public WaterSpray(SprayHeadSpeed speed, BottleSize size) {
		switch (speed) {
			case SLOW:
				this.head = new SprayHead(1);
				break;
			case MEDIUM:
				this.head = new SprayHead(5);
				break;
			case FAST:
				this.head = new SprayHead(50);
				break;
			default:
				assert (false) : "Unrecognized " + speed.getClass().getName() + ": " + speed;
				break;
		}


		switch (size) {
			case SMALL:
				this.body = new SprayBottle(10);
				break;
			case MEDIUM:
				this.body = new SprayBottle(50);
				break;
			case LARGE:
				this.body = new SprayBottle(150);
				break;
			default:
				assert (false) : "Unrecognized " + size.getClass().getName() + ": " + size;
				break;
		}
	}

	public SprayHead getHead() {
		return head;
	}

	public void setHead(SprayHead head) {
		this.head = head;
	}

	public SprayBottle getBody() {
		return body;
	}

	public void setBody(SprayBottle body) {
		this.body = body;
	}


	public int getCapacity() {
		return this.body.getCapacity();
	}


	public int getRemainingWater() {
		return this.body.getRemainingWater();
	}


	public void addWater(int amountInMl) {
		this.body.addWater(amountInMl);
	}


	public void fillUp() {
		this.body.fillUp();
	}


	public void spray() {
		this.head.sprayFrom(this.body);
	}

}
