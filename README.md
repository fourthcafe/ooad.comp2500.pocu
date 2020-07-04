# COMP2500: 개체지향 프로그래밍 및 설계

# 개체 모델링

만들 시스템: 꽃에 물주기
- 화분에 예쁜 꽃이 펴있음
- 이 꽃은 일정량의 물을 매일 뿌려만 주면 평생 살 수 있음
- 물을 뿌릴 때 사용하는 도구는 분무기
- 하루라도 필요한 물을 못 받으면 죽음
- 그 뒤 아무리 물을 뿌려도 안 살아남

간단해보이지만 고민할 사항이 있고 여러 가능성 중에 선택해야 할 것이 있다. 진행 중에 아닌 것을 깨닫고 돌아와야 할 때도 있다. <br>
사람에게 (이해하기) 쉬운 방식으로 작성하다가도 어느 순간 특정 문제에 의해 사람한테 이해가 어려워도 옳다고 생각하는 방식을 변경하는 경우도 있다.

## 1단계: 분무기부터 모델링
* 클래스명 정하기 -> WaterSpray
* 가장 중요한 상태: **현재 남아있는 물의 양**
    * 처음 분무기가 생성될 때는 0
    * int remainingWaterInMl (단위는 ml)
        > remainingWater, waterAmount 정도로 괜찮지만 단위를 확실히 해야하는 경우들이 있다.
* 생성자 필요
    > 생성자는 비어있는 생성자만 작성한다. <br>
        분무기가 처음 나올 때는 빈 분무기만으로 생성한다는 클래스 제작자의 의도를 담은 설계이다.
* 언제라도 남은 물의 양을 확인할 수 있으면 좋겠다고 생각해서 `getter` 추가한다.
* 물이 떨어지면 다시 채워야 하니 `setter` 도 추가한다.

**그런데 여기서 고민이 하나 생긴다.**

### 물을 추가할 때 어떻게 생각하는가?

1. 물을 200ml 까지 **채운다.**
2. 물을 100ml **추가한다.**

정답이 없는 주관적인 부분이다. 일반적으로 사람들이 생각하는 방식이나 같이 일하는 사람들의 생각, 혹은 비즈니스 로직과 같이 이해에 의해 달라질 수 있다.
<br>
위에서 사용한 `setter` 를 사용한 방식은 1번(채운다)이다. **물을 100ml 더 추가해야지** 로 하고 싶다면 `addWater()` 란 메서드가 더 적합하다. 원한다면 두 메서드를 모두 추가해도 된다. 현재 모델링에서는 `addWater()`를 사용하기로 한다.

여기까지의 설계를 클래스 다이어그램으로 표시하면 다음과 같다.

```text
WaterSpray
==========
- remainingWaterInMl: int = 0
----------
+waterSpray()
+getRemainingWater(): int
+addWater(int)
```

## 2단계: 분무기 동작
앞서 만든 분무기에 동작을 추가한다. 어떤 동작이 필요할까?

### 물 뿌리기
**물 뿌리기** 동작을 추가한다. **메서드 이름**은 무엇으로 해야할까? 역시 여러 가지 선택지가 있다.

1. 동작에 초점을 맞출 경우
    * pull(): 방아쇠를 당기다(pull the trigger)
    * press(): 펌프를 누르다(press the pump)
2. 용도에 초점을 맞출 경우
    * spray(): 뿌리다

2번을 사용하기로 한다. 코드 구현은 다음과 같다.

```java
public void spray() {
   this.remainingWaterInMl -= Math.min(this.remainingWaterInMl, 5);
}
```

**여기서 또 다시 고민이 생긴다.**

### 물을 뿌린 후 분무한 물의 양을 반환해야할까?
위의 `spray()` 메서드는 물을 뿌린 후에 분무한 물의 양을 반환하지 않는다. 이런 경우 다음 단계를 거쳐야 한다.

1. 스프레이를 사용한다.
2. 분무한 물의 양을 찾는다.
3. 2번을 꽃에 준다.

_이 내용을 메서드 내에서 한번에 처리해도 될 것 같은데_ 라는 생각이 들기도 한다.

좀 더 고민해보자.

### void spray() vs int spray()?
1. 분무한 양을 반환하지 않더라도 확인 가능
```java
int beforeMl = waterSpray.getRemainingWater();
waterSpray.spray();
int sprayedMl = beforeMl - waterSpray.getRemainingWater();
```

2. 직접 반환하게 만들 수도 있음
```java
public int spray() {
    int sprayedMl = Math.min(this.remainingWaterInMl, 5);
    this.remainingWaterInMl -= sprayedMl;
    return sprayedMl;
}
```

정답은 없다. 그러나 두번째 방법에는 약간의 문제가 있다. 두번째 방법의 메서드 시그니처를 다시 한번 보자.

```java
public int spray()
```

리턴형이 `int` 형이다. 이 때 반환하는 값은 무엇일까?
1. 뿌린 물의 양
2. 뿌리고 남은 물의 양

이 메서드의 문제는 **메서드 시그니처만으로는 정확히 무엇을 반환하는지** 알기 어렵다는 것이다. 리턴형에는 매개변수와는 달리 이름이 없기 때문에 명백하지 않다.

그리고 메서드가 하는 일이 너무 많다고 보는 부류도 있다. 이 메서드를 보는 개발자는 의견이 갈린다.

- A : 뿌리기만 해야지 왜 남은 물까지 계산해줘야해?
- B : 당연히 뿌리고 나서 남은 물까지 반환해줘야지

이것 역시 정답은 없다.

### 그래서 둘 중에 무엇을 쓸 것인가
뭐든지 명백한 게 좋으니 `void spray()`가 좀 더 좋다고 생각한다. 그렇다고 두번째 방식이 틀린 것은 아니다. 흔히 사용하는 패턴이니 정 필요하면 사용해도 된다. 두번째 방식을 사용하려면 메서드 이름을 `int sprayAndGetUsedAmount()` 처럼 좀 더 명확하게 하는 것도 방법이 될 수 있다. 메서드 이름에 **And** 와 같은 단어가 들어가는 순간 이 메서드는 2개 이상의 일을 하는 것이라 비판하는 부류도 있지만, 메서드 단계에서는 이 정도는 허용 범위이다. 다르게 말하자면 클래스 범위에서는 허용하지 않는다.

### 개체 모델링에서 흔히 저지르는 실수
개체 모델링을 하다보면 **실세계의 상태와 동작을 모두 클래스**에 넣으려는 실수를 하게 될 때가 있다. 그럴 때는 **이게 정말 필요한 정보인가?** 고려해야 한다. 완벽한 코드는 없다.

- 처음부터 올바르게 모델링할 수 있다는 망상을 버려야 한다.
- 사람은 점진적으로 배워가는 동물이다.
- 코드는 필요한 시점에 추가하면 된다.


## 모델링 3: 분무기 용량 추가
분무기의 구성 요소 중 물통 부분을 구현한다. 물통에는 최대 용량이 존재한다. 이것이 없다면 `addWater()` 메서드를 호출할 때마다 물통이 끝없이 커질 것이다. 분무기에 최대 용량을 추가한다. 여기서 두가지 방법이 있다.

### 모든 분무기의 용량이 같은 경우

분무기에 최대 용량에 대한 값을 **상수형 변수**로 추가한다.

```java
public class WaterSpray {
    private static final int CAPACITY = 200;    // 매직 넘버를 쓸 수도 있지만 상수형 변수가 더 좋은 습관이다.

    // 물을 추가한다. 물의 양이 최대치를 넘어서면 최대치로 물의 양을 설정한다.
    public void addWater(int amountInMl) {
        this.remainingWaterInMl += amountInMl;
        this.remainingWaterInMl = Math.min(this.remainingWaterInMl, CAPACITY);
    }
}
```

이랬을 때 클래스 다이어그램은 어떻게 될까? **달라지지 않는다.** 클래스 다이어그램에도 한계가 있다.

클래스 다이어그램이 보여주는 것

- 클래스가 가지고 있는 상태(멤버 변수). 여기에 상수는 포함하지 않는다.
- 클래스에서 실행할 수 있는 동작의 목록
- 클래스간의 관계 (의존 관계나 상속 관계)
- 실제 구현 코드는 보여주지 않는다.

그리고 위의 코드에서 상수를 사용했다. 상수 역시 클래스 다이어그램에 포함하지 않는데, 상수는 상태에 해당하지 않기 때문이다. 상수는 매직 넘버에 의미있는 이름을 달아둔 것일 뿐이다.

### 분무기마다 용량이 달라질 수 있는 경우

분무기마다 생산할 때 최대 용량을 다르게 할 수 있다. 그 후에 용량을 바꿀 수는 없다. 이를 스펙으로 명시하면 다음과 같다.

- 생성자에서 초기화한다. 생성 후에는 변경이 불가능하다.
- `setter` 가 없어야 한다.

클래스 다이어그램으로도 표기해보자.

```text
WaterSpray
==========
-capacity: int
-remainingWaterInMl: int = 0
----------
+WaterSpray(int)
+getCapacity(): int
+getRemainingWater(): int
+addWater(int)
+spray()
```

### 물 추가
물을 추가할 때도 두가지 방식을 생각할 수 있다.

1. 100ml 더 추가해야지
2. 가득 채워야지

여기서는 두번째 방식을 택한다. 그렇다면 **가득 채워야겠다** 동작은 어떻게 추가해야 할까. 메서드의 이름은 가득 채운다는 의미에서 **fillup()** 으로 한다. 매개변수는 필요없다. 해당 동작을 수행하는데 필요한 정보는 클래스 안에 이미 있다. 멤버 변수 **capacity** 를 **remainingWaterInIm**에 대입한다.

```java
public class WaterSpray {
    // 남은 물의 양을 최대 용량으로 변경한다.
    public void fillUp() {
        this.remainingWaterInMl = capacity;
    }
}
```

이렇듯 상태를 추가하면 새로운 메서드가 필요한 경우가 생긴다. 필요한 상태만 추가하는 게 좋은 또 다른 이유이다.


## 모델링 4: 수도꼭지
물은 어떻게 채울까? 수도꼭지가 필요하다. 그렇다면 그 수도꼭지에서 물은 자동으로 나오나? 수도꼭지에 연결된 파이프, 파이프에 연결되어 있는 송수관, 정화시설, 강... 끝이 없어진다.

**필요한 것만 만든다** 어디선가 선을 그어야 한다.

## 모델링 5: 화분

### 화분 만들기
클래스 이름은 PlowerPot 으로 한다. 화분의 상태를 나타내는 부분은 하루에 뿌린 물의 양에 따른 화분의 생사유무(alive) 이다. 살았는지 죽었느지 기억할 변수가 필요하다. alive 의 기본값은 true 이다. 개체를 생성할 때 설정해준다. 그리고 **alive 의 setter 는 없다.** 중요한 부분이다. 한번 죽은 꽃은 되살릴 수 없다. setter 를 허용하면 값을 마음대로 변경할 수 있게 된다.

```text
FlowerPot
==========
-alive: boolean = true
-minDailyWaterInMl: int
----------
+FlowerPot(int)
+inAlive(): boolean
+getMinDailyWater(): int
```

- minDailyWaterInMl: 매일 필요한 최소 물의 양(ml). 생성자에서 값을 설정한다.
- 꽃마다 다른 양이 필요하니 생성자를 통해 초기화한다.
- getter 는 추가해도 큰 문제없으니 추가한다.
- setter는 역시 추가하지 않는다.

```java
public class FlowerPot {
	private boolean alive = true;
	private final int minDailyWaterInMl;

	public FlowerPot(int minDailyWaterInMl) {
		this.minDailyWaterInMl = minDailyWaterInMl;
	}


	public int getMinDailyWaterInMl() {
		return minDailyWaterInMl;
	}
}
```


### 화분에 필요한 동작
화분에는 물을 줄 수 있어야 한다. `addWater(int)` 메서드를 추가한다. 코드를 작성해 돌려본다.

- 물이 최대 100ml 들어가는 WaterSpray를 만들고 물을 가득 채운다.
- 하루에 살려면 5ml 물이 필요한 화분을 만든다.
- 화분에 물을 준다.
- 화분의 생존을 확인한다.


**화분에_물주기_예1**

```java
final WaterSpray waterSpray = new WaterSpray(100);
waterSpray.fillUp();

final FlowerPot pot = new FlowerPot(5);

int water = waterSpray.getRemainingWaterInMl();
waterSpray.spray();
water -= waterSpray.getRemainingWaterInMl();

pot.addWater(water);

assertThat(pot.isAlive()).isTrue();
```

정상적˚으로 수행된다. 그러면 다음 코드는 어떨까?

- 물이 최대 100ml 들어가는 WaterSpray를 만들고 물을 가득 채운다.
- 하루에 살려면 10ml 물이 필요한 화분을 만든다.
- 이하 위와 동일

1번과 달라진 것은 화분이 필요한 물의 양이다. 어떻게 해결할까? 물을 2번 주자!


**화분에_물주기_예2**

```java
final WaterSpray waterSpray = new WaterSpray(100);
waterSpray.fillUp();

final FlowerPot pot = new FlowerPot(10);

for (int i = 0; i < 2; ++i) {
    int water = waterSpray.getRemainingWaterInMl();
    waterSpray.spray();
    water -= waterSpray.getRemainingWaterInMl();

    pot.addWater(water);
}

assertThat(pot.isAlive()).isTrue();     // 실패
```

예상과는 달리 화분은 죽었다. 왜? `FlowerPot.addWater(int)` 코드를 보자.

```java
public void addWater(int amountInMl) {
    if (amountInMl < minDailyWaterInMl) {
        alive = false;
    }
}
```

물을 주는 즉시 화분의 생존을 확인한다. 5ml 씩 두번 뿌리는 것을 허용하지 않겠다는 뜻이다. 다른 말로 하면 **한번 뿌리면 하루가 지났다고 가정**하는 것이다. 한번 뿌리면 하루다. 즉, 함수 호출 횟수와 날짜를 동일화 시킨 것이다. 의도치 않은 사항이다. 내가 원하던 것이 아니다. 코드를 어떻게 수정해야할까?

### 하루에 여러 번 뿌릴 수 있게 다시 모델링
하루가 지났다는 것을 어떻게 표현해야할까? 타이머를 붙여야 하나? 여기서는 간단하게 해결해보자. flowerPot에서 할 수 있어야 하는 일을 정리하자.

1. 하루에 받은 총 분무량을 기억한다.
2. 하루가 끝났음을 알아야 한다.

**분무했다**는 게 하루가 지났다는 개념이 아니다. 분무라는 행동과 이를 분리해야 한다.

- `dailyWaterReceived` 필드를 추가한다. 하루동안 받은 물이 양을 기억하는 변수다.
- `addWater(int)` 메서드는 받은 양의 물을 더하는 일만 하도록 수정한다. 생존을 판단하는 코드를 제거한다.
- `liveAnotherDay()` 라는 메서드를 새로 구현한다. 하루가 지났을 때 이 메서드를 호출하는 것으로 약속하고 이 메서드에서 생존을 확인한다.

변경한 코드는 다음과 같다.

```java
public class FlowerPot {
    private int dailyWaterReceived = 0;


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
```

클래스 다이어그램으로 표시해보자.

```text
FlowerPot
==========
-live: boolean = true
-minDailyWaterInMl: int
-dailyWaterReceived: int = 0
----------
+FlowerPot(int)
+isAlive(): boolean
+getMinDailyWaterInMl(): int
+addWater(int)
+liveAnotherDay()
```

그리고 하나 짚고 넘어가야 할 부분이 있다. `liveAnotherDay()` 메서드는 `setter`없이 인스턴스의 상태 `alive`를 변경한다. 이처럼 개체 스스로 자기가 필요한 판단을 할 수 있어야 한다.


## 모델링 6: OO적 상호작용
작동하는 코드를 완성했다. 잘 돌아간다! 그럼 이제 개발 완료일까? 물을 뿌리는 부분의 코드를 다시 보자.

```java
int water = waterSpray.getRemainingWaterInMl();
waterSpray.spray();
water -= waterSpray.getRemainingWaterInMl();

pot.addWater(water);
```

이 코드의 위치는 어디인가? **application** 이다. 호출자가 다음 일을 직접 수행하고 있다.

1. 분무기에서 물을 분사한다.
2. 몇 ml 가 분사되었는지 확인한다.
3. 화분에 분사된 물의 양을 추가한다.

즉 waterSpray 나 FlowerPot 을 그냥 **데이터 저장소**로만 쓴 거나 다름 없다. 구조체 사고방식에서 아직 벗어나지 못한 것이다. 데이터를 바꾸는 메서드 몇 개 있는 게 전부일 뿐이다. **두 개체 사이에는 상호작용이나 의존 관계가 전혀 없다.** 프로그래머, 혹은 application.java 클래스가 두 클래스 사이에서 중재를 해주고 있다. 개체지향 프로그래밍은, 프로그램은 여러 개체들의 상호작용으로 본다.

### 두 개체가 직접 상호작용하도록 바꿔보자
나는 분무기를 사용해서 화분에 물을 주길 원한다. 문제는 다음 두 메서드이다.

- WaterSpray.spray()
- FlowerPot.addWater(int)

두 메서드 모두 범용적 자료형인 int를 사용한다. int 형 데이터를 받아서 서로 호출하고 있어서 누구나 이 메서드를 마음대로 호출할 수 있다. 개체들끼리 상호작용을 하게 할 수 있게 클래스 설계를 바꿔보자.


### 방법 1: 분무기를 화분에 대고 뿌린다
`WaterSpray.sprayTo(FlowerPot)`

```java
public class WaterSpray {
    ...

    public void sprayTo(FlowerPot pot) {
		final int amountWater = Math.min(this.remainingWaterInMl, 5);
		pot.addWater(amountWater);

		this.remainingWaterInMl -= amountWater;
	}
}
```

아래는 호출자 코드이다. `sprayTo(FlowerPot)` 메서드를 사용하면 모든 계산이 **메서드 내부**에서 끝나게 된다. 따라서 외부에서는 이 메서드를 호출만 하면 되니 호출자 코드가 간단해진다. 데이터를 숨겼으니 캡슐화가 잘되었고 내부 구현을 숨겼으니 추상화도 잘되었다. 추가로 `spray()` 메서드는 사용할 곳이 없어졌으니 사용자의 선택에 의해 삭제할 수도 있다.

```java
// 변경 전
for (int i = 0; i < 2; ++i) {∆
    int water = waterSpray.getRemainingWaterInMl();
    waterSpray.spray();
    water -= waterSpray.getRemainingWate∆rInMl();

    pot.addWater(water);
}


// 변경 후
for (int i = 0; i < 2; ++i) {
    waterSpray.sprayTo(pot);
}
```


### 방법 2: 분무기를 줄 테니 화분이 알아서 뿌려라
`FlowerPot.addWater(WaterSpray spray)`

인자로 WaterSpray 를 받아와서 자신이 알아서 스스로에게 물을 주는 방식이다. 다음 코드는 이전에 호출자(application.java)에서 봤었던 코드와 유사하다. 하지만 호출자보다는, FlowerPot 이 **자기 스스로 WaterSpray 를 어떻게 사용해야 하는지** 알고 있다는 점에서 더 개체지향적이다.

```java
public class FlowerPot {
    ...

	public void addWater(WaterSpray waterSpray) {
		int water = waterSpray.getRemainingWaterInMl();
		waterSpray.spray();
		water -= waterSpray.getRemainingWaterInMl();

		this.dailyWaterReceived += water;
	}
}
```

다음은 호출자 코드를 보자. 방법 1과 마찬가지로 호출자 코드가 간단해졌다.

```java
// 변경 전
for (int i = 0; i < 2; ++i) {
    int water = waterSpray.getRemainingWaterInMl();
    waterSpray.spray();
    water -= waterSpray.getRemainingWaterInMl();

    pot.addWater(water);
}


// 변경 후
for (int i = 0; i < 2; ++i) {
    pot.addWater(waterSpray);
}
```


### 두 방법 중 뭐가 좋은가?
1. 분무기를 화분에 주고 뿌린다.
2. 분무기를 줄테니 화분이 알아서 뿌려라.

정답이 없는 문제이고 우리에게 익숙한 생각은 1번이다. 2번은 뭔가 어색하다. 화분이 무슨 수로 분무기를 가져다 샤워을 해? **하지만!**

**2번이 좀 더 개체지향적인 생각에 가깝다.**

### 2번을 선택했을 시의 장점
- FlowerPot.addWater(int) 를 제거할 수 있다.
- 분무기만 화분에 물을 줄 수 있다.

`addWater(int)` 메서드가 있었던 이유는 결과적으로 몇 ml를 뿌리는지 알아야했기 때문이다. `addWater(WaterSpray)` 를 사용하여 WaterSpray를 받아오면 스스로가 알아서 뿌리고 끝나기 때문에 몇 ml를 더한다는 로직 자체가 메서드 안에 숨게 된다. 그래서 WaterSpray를 주지 않거나 WaterSpray에 충분히 물이 들어있지 않으면 물을 주지 못하게 된다. 스스로를 책임질 수 있어서 좋고, 어디로부터 물을 공급받는지 특정지을 수 있어서 좋다.

OO(Object Oriented)는 약간의 사고방식 변화가 필요하다. 위의 구현대로라면 마치 화분이 스스로 살아 움직이는 생명체같이 느껴져 뭔가 어색하다는 생각이 든다. 실세계의 물체는 완벽히 수동적인 존재로서 누군가 조작해 줘야 한다. OO 세계에서는 그렇지 않다. 자기 주관을 가지고 스스로를 책임질 수 있는 주체라고 생각해야 한다. **WaterSpray를 줄테니 알아서 뿌려** 라고 할 수 있는 것까지는 해야된다고 사고방식을 변화하도록 노력하자.


### 한번 더 짚어보기
- 나름 복잡한 계산 로직을 클래스 안에 숨겼다
    - FlowerPot.addWater(WaterSpray)
- addWater(int) 함수를 제거했다
    - 외부에서 자기 마음대로 많은 양의 물을 붓거나 WaterSpray에서 양을 줄이지 않고 붓는 행위를 할 수 없게 제약했다.


## 모델링 7: 부품으로 분리해보기
현재의 코드는 유연성이 떨어진다. 화분에 물을 주는 행위는 WaterSpray가 아닌 컵을 통해서도 줄 수 있다. 하지만 지금 구조에서는 불가능하다. 이전의 `int`를 사용한 방식은 굉장히 유연하고 범용적인 방식이었고 지금의 코드는 굉장히 특화된 방식이다. 추가하고 싶으면 `addWater(Cup)` 처럼 다른 메서드를 추가하거나 이 후 배우는 추상 클래스와 다형성을 통해서도 해결하는 방법을 익힐 수 있다.

### 재활용성과 유연성
**재활용**과 **유연성** 은 이 경우에 유사한 개념이다. 유연성이 떨어진다는 것은 재활용하기가 어렵다는 뜻이고, 여기저기에서 사용하기 어렵다는 것이다. 분무기는 머리와 몸통, 2개의 부품으로 분리할 수 있다. 머리와 몸통을 따로 떼어 각기 다른 곳에서 사용할 수 있게 코드를 개선하여 재활용성과 유연성을 높여보자. 분무기를 두 부품으로 분리한 후에 각자의 스펙을 생각해보자.

- 머리
    - 손잡이
    - 호스
    - 손잡이를 누르면 정해진 만큼의 물이 나간다.
    - ON/OFF나 분사 양 조절 기능이 있을수 있다.
    - 손잡이 모양도 달라질 수 있다.

- 몸통
    - 물을 저장한다. (용량이 있음)
    - 물통을 큰 거나 작은 걸로 바꿀수 있다.

하지만 이건 너무 나갔다. ON/OFF 나 손잡이의 모양은 지금 구현하고 있는 코드에서 고려할 필요가 없는 내용이다. 머리와 몸통을 바꿔 낄 수 있는 내용만을 스펙에 추가하자.


### 상태에 따라 분리한 버전
- 한 곳에 있던 상태를 머리와 몸통으로 나눈다.
    - 머리: SprayHead
    - 몸통: SprayBottle
- 관련 메서드도 각 클래스로 옮긴다.

- 특이사항
    - SprayHead에는 `sprayAmount` 멤버 변수가 생겼다. 얼마만큼의 물을 뿌릴 것인지 기존에는 5ml로 하드코딩 되어 있던 부분을 변수화했다.
    - SprayBottle에는 물을 채우는 메서드를 `addWater(int)` 로 구현했다. 기존의 코드로 롤백했다.

```text
WaterSpray
==========
-head: SprayHead
-body: SprayBottle
----------
+WaterSpray(SprayHead, SprayBottle)
+gettHead(): SprayHead
+getBody(): SprayBottle
+setHead(SprayHead)
+setBody(SprayBottle)


SprayHead
==========
-sprayAmount: int
----------
+SprayHead(int)
+spray()


SprayBottle
==========
-capacity: int
-remainingWater: int = 0
----------
+SprayBottle(int)
+getCapacity(): int
+getREmainingWater(): int
+addWater(int)
+fillUp()
```

각자의 개체로 인정하면 **집합(aggregation)** 이라 한다. WaterSpray는 머리와 몸통을 합쳐 놓은 **집합**이다. 컴포지션이 아니라 집합이라는 얘기는 이 둘을 따로 분리해서 사용할 수 있다는 얘기다. 분리할 수 있고 따로 생존할 수 있다면 따로 `getter/setter` 를 만들어 주는 게 맞는 것 같다. 집합이 아니라 WaterSpray와 같이 살고 죽는 컴포지션의 개념으로 봤다면 만들지 않았을 가능성이 매우 크다.

```java
SprayHead head = new SprayHead(5);
SprayBottle body = new SprayBottle(100);
body.fillUp();
WaterSpray waterSpray = new WaterSpray(head, body);
```

이 방식은 여러 종류의 머리와 몸통을 조합할 수 있다는 장점이 있다.

```java
// 한번에 물이 적게 나가지만 물통이 큼
SprayHead headSlow = new SprayHead(1);
SprayBottle bottleXl = new SprayBottle(150);

// 한번에 많은 물이 나가고 물통이 작음
SprayHead headFast = new SprayHead(50);
SprayBottle bottleL = new SprayBottle(50);
```

그럼 이 코드를 FlowerPot에 사용해보자

```java
public void addWater(WaterSpray waterSpray) {
    final WaterHead head = waterSpray.getHead();
    final WaterBottle body = waterSpray.getBody();

    int water = body.getRemainingWater();
    head.spray();
    water -= body.getRemainingWater();

    this.dailyWaterReceived += water;
}
```

- 물 용량 확인
    - 분무기에서 몸통을 가져온다.
    - 거기서 남은 용량을 확인
- 분무
    - 분무기에서 머리를 가져온다.
    - spray()를 호출한다.

위의 코드는 `spray()` 메서드에 오류가 있어 컴파일되지 않는다. 물을 뿌리려고 '머리에 물을 뿌리라' 메시지를 보내지만 실제 물은 몸통에 있다.

결과적으로 spray() 메서드는 몸통을 알아야만 한다. 코드를 다시 한번 수정하자.

- SprayBottle
    - reduceWater() 메서드 추가
SprayHead
    - spray() 메서드 삭제
    - sprayFrom(SprayBottle) 메서드 추가

## 유연성 높은 설계가 최고가 아니다!
머리와 몸통을 따로 구성해서 재사용성과 유연성을 높였다. 그런데 필요 이상으로 복잡한 느낌이 든다. 머리랑 몸통을 각각 만들어야 하고 물을 뿌릴 때도 머리랑 몸통을 각각 호출해야 한다. `FlowerPot.addWater(WaterSpay)` 메서드의 구현부를 보면 화분이 분무기 머리와 몸통을 다 알아야 한다는 것도 이상하다. 이런 간단한 예에서도 이러는데 이보다 훨씬 복잡한 프로그램에서는 어떻게 될까. What the...

### 유연성 높은 설계가 최고다?
유연성을 조금만 늘려도 이런 문제가 발생한다. 재사용성이 있어서 좋은 경우도 있겠지만 모든 경우가 그런 것은 아니다. 자동차의 신형 엔진을 개발하여 10% 크기가 작아지고 힘은 200% 증가했다 하자. 크기가 작아진 만큼 수납 공간 같은 추가 공간이 생길 것을 기대할 수 있다. 하지만 유연성을 발휘하여 기존 엔진과 신형 엔진을 모두 탑재할 수 있게 엔진룸의 크기는 그대로 두기로 했다. 재사용성을 포기하고 신형 엔진만을 위해 만들어서 10% 의 수납 공간을 늘리는 게 더 좋을 수도 있다. 실제 생활에서도 그렇고 프로그래밍에서도 마찬가지다.

### 재사용할 수 있어서 좋은 경우
- 자주 사용하는 수식들을 함수로 만들어서 사용하는 경우

```java
static double abs(double a);
static float abs(float a);
static int abs(int a);
```

### 너무 쪼개져서 읽기 힘든 경우
재사용을 고려해 너무 쪼갤 경우, 여러 파일을 넘나 들어야 한다. 사람은 기본적으로 위에서 아래로 읽는 것을 제일 편해하고 파일을 넘나들며 코드를 읽는 것을 좋아하지 않는다. 아래의 코드처럼 메서드 하나에서 순서대로 쭉 읽어 내려갈 수 있고 거기에 단계별로 무엇을 하는지 적혀 있는 게 읽기 편하다. 사람의 사고방식을 무시하지 말자.

```java
void update() {
// 프로필 관련 코드(수십 줄)
...
// HP 게이지 관련 코드(수십 줄)
...
// MP 게이지 관련 코드(수십 줄)
...
// 버프 아이콘 관련 코드(수십 줄)
...
}
```

### 코드 유연성은 양날의 검
유연성이 높으면 다음 효과를 얻는다.
- 성능 하락
- 가독성 하락
- 재사용성 상승

유연성이 낮으면 다음 효과를 얻는다.
- 성능 상승
- 가독성 상승
- 재사용성 하락


### 유연성의 두 얼굴
업계가 수십년 동안 실전에 OOP를 사용하고 깨달은 것은

1. 안 유연한 클래스를 만드는 경우가 더 빈번하며 그래도 상관없음
2. 절대 반지는 없음
3. 어느 정도의 유연성이 필요한지는 각 프로젝트마다 다름

말하고 싶은 요지는 **필요에 따라 유연성을 유연하게 조정**하라는 것이다. 일반적으로는 재사용성을 포기하고 성능과 가독성을 높이는 경우가 더 많다. 재사용성이 높은 게 최고가 아니다.


## 기본기의 중요성
개체 모델링 시 유연성을 보기 전에 한가지 더 신경써야 할 것을 보자.

### OOP 공부 가이드라인
기본기 없이 유연성만 고려한 프로그래밍은 결국엔 무너진다. 복잡한 것 때문에 사람들이 이해할 수 없는 코드가 만들어지게 되고 결국엔 무너진다.

1. 프로그래머의 기본 자세를 확실히 잡자.
	1. 읽기 명확한 코드 만들기
		- 이게 가장 우선이다. 이게 된 이 후에 유연성이 필요한 부분에 도입하는 거다.
	2. 실수를 저지르기 어려운 코드 만들기
		- 유연성이 많아질수록 추상적(일반적)이기 때문에 실수가 많아질 수 밖에 없다. 어떤 문제가 있을 시 그 문제를 해결하기 위한 코드를 만드는 것이 더 중요하다.
	3. 문제를 해결하는 코드 만들기
		- 우선 유연성 없이 문제를 해결한 후에 유연성이 없어서 문제가 발생하면 그 때 도입하자.
	4. 문제가 생기면 디버깅
		- 문제가 생기면 디버깅을 하자. 디버깅 없이 '품질이 좋아지는 방법' 이라는 다른 것들을 하게 되면 품질이 좋아지는 게 아니라 코드가 읽기 어려워져 아무도 손을 못 대는 코드, 버그가 있어도 있는지조차 모르는 코드가 생길 수 있다.

'생성자 만들지 말아라, setter 만들지 말아라' 와 같이 기초 OOP 개념들이 이런 자세를 잡는데 도움이 된다.

2. 필요에 따라 점점 유연성을 키우는 법을 배워 나가기
- 클래스를 만들 때마다 '이 부분은 재활용하면 좋겠다' 라고 생각하게 되는 부분이 보이면 그런 곳에서부터 유연성을 키워 나가자. 특히 유연성이 중요해지는 것은 **다형성** 부분이다. 지금 말하고 있는 유연성과 다른 유연성인데 그 부분을 더 신경써야 하지 단순히 클래스를 작게 만드는 유연성을 큰 의미가 없을 수도 있다.

공든 탑은 쉽게 무너지지 않는다. 기본기부터 제대로 쌓아서 개체지향 모델링 실력을 올리자. 그래야 유연성이 없으면 작동하기 어려운 문제를 마주했을 때 쉽게 풀면서도 유연성을 높이느라 가독성이 떨어진 코드를 쉽게 읽을 수 있다.

**유연성은 필요하면 만드는 것**이다. 필요하지 않다면 만들지 않는 게 좋다.

## 모델링 8: 다시 사용성 높이기
다시 화분 문제 돌아가자. 그렇다면 머리랑 몸통을 없애고 다시 하나 가야하나? 좀 복잡하지만 이것 저것 섞어 쓸 수 있어서 그 부분은 좋았었는데 말이다. 재사용성을 유지하면서 해결하는 법을 알아보자.

### 두 마리 토끼를 다 잡아보자
아까 봤던 문제를 정리하고 하나씩 해결해보자.

- 머리와 몸통을 따로 만들어야 한다.
- 분무기에서 곧바로 분무를 못한다.

### 머리와 몸통을 따로 만들어야 한다.
- 티셔츠 크기의 개념을 차용해보자. S, M, L 처럼 규격을 정해두고 사용하는 방식이다. 물을 분사하는 속도(양)이 다른 head 와 물을 담고 있는 양이 다른 bottle 을 구현한다.

```java
public enum SprayHeadSpeed {
    SLOW,
    MEDIUM,
    FAST
}


public enum BottleSize {
    SMALL,
    MEDIUM,
    LARGE
}
```

WaterSpray 에 새로운 생성자를 추가한다. 이를 사용해서 미리 준비된 사이즈의 WaterSpray 를 생성할 수 있다. 첫번째 생성자를 사용해서 기존 방식대로 WaterSpray 를 생성할 수도 있게 하여 재사용성도 유지했다.

```text
WaterSpray
==========
-head: SprayHead
-body: SprayBottle
----------
+WaterSpray(SprayHead, SprayBottle)
+WaterSpray(SprayHeadSpeed, BottleSize)
```

이를 통해 호출자에서는 다음처럼 WaterSpray 를 생성할 수 있다. 이제 물통 용량이 정확히 몇 ml인지 몰라도 된다.

```java
WaterSpray smallAndFast = new WaterSpray(SprayHeadSpeed.FAST, BottleSize.SMALL);
WaterSpray largeAndSlow = new WaterSpray(SprayHeadSpeed.SLOW, BottleSize.LARGE);
```

### 분무기에서 곧바로 분무를 못한다.
머리와 물통을 가져와서 분무를 해야하는, 이상한 방식을 고쳐보자. WaterSpray 에 메시지를 주면 내부에서 head 와 body 이 메서드를 릴레이로 호출하는 방식으로 변경한다.

```java
public int getRemainingWater() {
    return this.body.getRemainingWater();
}


public void addWater(int milliliter) {
    this.body.addWater(milliliter);
}
```

이렇게 고치면 `spray()` 메서드가 다음처럼 간단해진다.

```java
public void spray() {
    this.head.sprayFrom(this.body);
}
```

FlowerPot 의 addWater() 도 간단해진다.
