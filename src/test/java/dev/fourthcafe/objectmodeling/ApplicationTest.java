package dev.fourthcafe.objectmodeling;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {

	@Test
	public void 화분에_물주기_예1() {
		final WaterSpray waterSpray = new WaterSpray(100);
		waterSpray.fillUp();

		final FlowerPot pot = new FlowerPot(5);

		int water = waterSpray.getRemainingWaterInMl();
		waterSpray.spray();
		water -= waterSpray.getRemainingWaterInMl();

		pot.addWater(water);

		assertThat(pot.isAlive()).isTrue();
	}


	@Test
	public void 화분에_물주기_예2() {
		final WaterSpray waterSpray = new WaterSpray(100);
		waterSpray.fillUp();

		final FlowerPot pot = new FlowerPot(10);

		for (int i = 0; i < 2; ++i) {
			int water = waterSpray.getRemainingWaterInMl();
			waterSpray.spray();
			water -= waterSpray.getRemainingWaterInMl();

			pot.addWater(water);
		}

		assertThat(pot.isAlive()).isTrue(); // 실패
	}


	@Test
	public void 물을_뿌릴_때마다_받은_물_양을_누적() {
		final WaterSpray waterSpray = new WaterSpray(100);
		waterSpray.fillUp();

		final FlowerPot pot = new FlowerPot(10);

		for (int i = 0; i < 2; ++i) {
			int water = waterSpray.getRemainingWaterInMl();
			waterSpray.spray();
			water -= waterSpray.getRemainingWaterInMl();

			pot.addWater(water);
		}

		pot.liveAnoterDay();
		assertThat(pot.isAlive()).isTrue();
	}
}
