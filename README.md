# zxcv-java-wrapper

Wraps the `zxcvbn` JavaScript implementation for use on the JVM.

This is *not* a port of `zxcvbn`, hence the "wrapper" terminology. It does not re-implement anything in Java, it executes the original JavaScript directly using the [Java Scripting API](https://docs.oracle.com/javase/7/docs/technotes/guides/scripting/programmer_guide/).

## Usage

Java:
```java
Zxcvbn zxcvbn = new Zxcvbn(); // This instance should be stored and reused.
Zxcvbn.Result result = zxcvbn.apply("password");
```

Zxcvbn implements the `java.util.function.Function` interface. It can be used in higher order functions such as the ones found in `java.util.stream`.

## Benchmark

Calling `zxcvbn` takes less than _10 ms_. Creating an instance of `Zxcvbn` however, takes _several seconds_. The instance should therefore be **reused**.

To run the benchmark use the `run` gradle task:

```sh
./gradlew run
```

```

Benched creation Zxcvbn instances
Number of creations in 20 seconds: 5
Average time [ms]: 4000.000

Benched password: password
Result: {guesses=3, guessesLog10=0.47712125471966244, crackTimesSeconds={offline_slow_hashing_1e4_per_second=3.0E-4, offline_fast_hashing_1e10_per_second=3.0E-10, online_throttling_100_per_hour=108.0, online_no_throttling_10_per_second=0.3}, crackTimesDisplay={offline_slow_hashing_1e4_per_second=less than a second, offline_fast_hashing_1e10_per_second=less than a second, online_throttling_100_per_hour=2 minutes, online_no_throttling_10_per_second=less than a second}, score=0, feedbackWarning=This is a top-10 common password, feedbackSuggestions={0=Add another word or two. Uncommon words are better.}, sequence={0=[object Object]}, calcTime=0.0}
Number of executions in 10 seconds: 38828
Average time [ms]: 0.258

Benched password: 8f01071b-cd21-4590-ad96-2139d88bd03c
Result: {guesses=1.0E36, guessesLog10=35.99999999999999, crackTimesSeconds={offline_slow_hashing_1e4_per_second=1.0E32, offline_fast_hashing_1e10_per_second=1.0E26, online_throttling_100_per_hour=3.6000000000000004E37, online_no_throttling_10_per_second=1.0E35}, crackTimesDisplay={offline_slow_hashing_1e4_per_second=centuries, offline_fast_hashing_1e10_per_second=centuries, online_throttling_100_per_hour=centuries, online_no_throttling_10_per_second=centuries}, score=4, feedbackWarning=, feedbackSuggestions={}, sequence={0=[object Object]}, calcTime=6.0}
Number of executions in 10 seconds: 1306
Average time [ms]: 7.657

```
(java version "1.8.0_131", Intel(R) Core(TM) i7-4771 CPU @ 3.50GHz, macOS Sierra 10.12.5)
