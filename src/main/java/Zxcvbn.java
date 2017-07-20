import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Zxcvbn implements Function<String, Optional<Zxcvbn.Result>> {

  private final ScriptEngineManager factory = new ScriptEngineManager();

  private final ScriptEngine engine = factory.getEngineByName("JavaScript");

  public Zxcvbn() {
    Reader script = new InputStreamReader(getClass().getResourceAsStream("zxcvbn.js"));
    try {
      engine.eval(script);
    } catch (ScriptException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Optional<Result> apply(String password) {
    try {
      engine.put("password", password);
      Object result = engine.eval("zxcvbn(password)");
      return Optional.of(new Result(result));
    } catch (ScriptException e) {
      System.err.println(e);
      return Optional.empty();
    }
  }

  @SuppressWarnings("unchecked")
  public static class Result {

    /** Original zxcvbn result including all keys. */
    public final Map<String, Object> raw;

    public Number guesses() {
      return (Number)raw.get("guesses");
    }

    public Number guessesLog10() {
      return (Number)raw.get("guesses_log10");
    }

    public Map<String, Integer> crackTimesSeconds() {
      return new HashMap<>((Map<String, Integer>)raw.get("crack_times_seconds"));
    }

    public Map<String, Integer> crackTimesDisplay() {
      return new HashMap<>((Map<String, Integer>)raw.get("crack_times_display"));
    }

    public Number score() {
      return (Number)raw.get("score");
    }

    public String feedbackWarning() {
      return ((Map<String, String>)raw.get("feedback")).get("warning");
    }

    public Map<String, String> feedbackSuggestions() {
      return new HashMap<>(((Map<String, Map<String, String>>)raw.get("feedback")).get("suggestions"));
    }

    public Map<String, String> sequence() {
      return new HashMap<>((Map<String, String>)raw.get("sequence"));
    }

    public Number calcTime() {
      return (Number)raw.get("calc_time");
    }

    private Result(Object scriptResult) {
      if (!(scriptResult instanceof Map)) {
        throw new IllegalStateException("Script result does not implement java.util.Map."
          + " Expected: jdk.nashorn.api.scripting.ScriptObjectMirror, Actual: "
          + scriptResult.getClass().getName());
      }

      raw = (Map<String, Object>)scriptResult;
    }

    @Override
    public String toString() {
      String[] properties = new String[] {
        "guesses=" + guesses(),
        "guessesLog10=" + guessesLog10(),
        "crackTimesSeconds=" + crackTimesSeconds(),
        "crackTimesDisplay=" + crackTimesDisplay(),
        "score=" + score(),
        "feedbackWarning=" + feedbackWarning(),
        "feedbackSuggestions=" + feedbackSuggestions(),
        "sequence=" + sequence(),
        "calcTime=" + calcTime()
      };
      return "{" + String.join(", ", properties) + "}";
    }
  }

}
