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
	public void 하루에_10ml_필요한_화분에_5ml씩_두번주기() {
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

		pot.liveAnotherDay();
		assertThat(pot.isAlive()).isTrue();
	}


	@Test
	public void 분무기를_화분에_대고_뿌린다() {
		final WaterSpray waterSpray = new WaterSpray(100);
		waterSpray.fillUp();

		final FlowerPot pot = new FlowerPot(10);

		for (int i = 0; i < 2; ++i) {
			waterSpray.sprayTo(pot);
		}

		pot.liveAnotherDay();
		assertThat(pot.isAlive()).isTrue();
	}


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
