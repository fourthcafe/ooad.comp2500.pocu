package dev.fourthcafe.objectmodeling;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {
	@Test
	public void 분무기를_머리와_몸통으로_분리하여_구현() {
		SprayHead head = new SprayHead(5);
		SprayBottle body = new SprayBottle(100);
		body.fillUp();

		WaterSpray waterSpray = new WaterSpray(head, body);

		final FlowerPot pot = new FlowerPot(10);

		for (int i = 0; i < 2; ++i) {
			pot.addWater(waterSpray);
		}

		pot.liveAnotherDay();
		assertThat(pot.isAlive()).isTrue();
	}
}
