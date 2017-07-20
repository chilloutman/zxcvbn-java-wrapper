import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ZxcvbnBench {

  /** Benchmarking will take 3x this */
  private static final long BENCH_TIME_LIMIT_NANOS = SECONDS.toNanos(10);

  public static void main(String[] args) {
    benchCreation();
    benchPasswords();
  }

  public static void benchCreation() {
    long counter = 0;
    long startTime = System.nanoTime();
    long timeLimit = BENCH_TIME_LIMIT_NANOS * 2;

    do {
      new Zxcvbn();
      counter++;
    } while ((System.nanoTime() - startTime) < timeLimit);

    System.out.format(
      "\n" +
      "Benched creation Zxcvbn instances \n" +
      "Number of creations in %d seconds: %d\n" +
      "Average time [ms]: %.3f" +
      "\n",
      NANOSECONDS.toSeconds(timeLimit),
      counter,
      (double)NANOSECONDS.toMillis(timeLimit) / counter);
  }

  public static void benchPasswords() {
    Zxcvbn zxcvbn = new Zxcvbn();
    bench(zxcvbn, "password");
    bench(zxcvbn, java.util.UUID.randomUUID().toString());
  }

  private static void bench(Zxcvbn zxcvbn, String password) {
    Zxcvbn.Result result = null;
    long counter = 0;
    long startTime = System.nanoTime();

    do {
      result = zxcvbn.apply(password).get();
      counter++;
    } while ((System.nanoTime() - startTime) < BENCH_TIME_LIMIT_NANOS);

    System.out.format(
      "\n" +
      "Benched password: %s\n" +
      "Result: %s \n" +
      "Number of executions in %d seconds: %d\n" +
      "Average time [ms]: %.3f"+
      "\n",
      password,
      result,
      NANOSECONDS.toSeconds(BENCH_TIME_LIMIT_NANOS),
      counter,
      (double)NANOSECONDS.toMillis(BENCH_TIME_LIMIT_NANOS) / counter);
  }

}