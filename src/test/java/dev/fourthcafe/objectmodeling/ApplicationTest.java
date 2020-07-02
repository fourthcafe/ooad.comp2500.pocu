package dev.fourthcafe.objectmodeling;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {
	@Test
	public void 분무기를_줄테니_화분이_알아서_뿌려라() {
		final WaterSpray waterSpray = new WaterSpray(100);
		waterSpray.fillUp();

		final FlowerPot pot = new FlowerPot(10);

		for (int i = 0; i < 2; ++i) {
			pot.addWater(waterSpray);
		}

		pot.liveAnotherDay();
		assertThat(pot.isAlive()).isTrue();
	}
}
