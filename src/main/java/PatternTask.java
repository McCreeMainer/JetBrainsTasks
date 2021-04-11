import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternTask {
    /**
     * This method execute 3 actions:
     *      1. Creates and compiles new {@link Pattern} from user string;
     *      2. Creates {@link Matcher} from pattern and another user string;
     *      3. Checks if the matcher matches its pattern.
     *
     * There was 3 main problems
     *      1. No check for Nullability of params;
     *      2. No check for pattern correctness;
     *      3. No protection from Catastrophic Backtracking.
     *
     * First problem solved by using @NonNull annotation.
     *
     * Second problem solved partially: actually i cant do anything with
     * incorrect users regex, but now method catching all PatternSyntaxException
     * and directs the message to standard output, so it avoids crashes.
     *
     * Last problem solved by using wrapper-class for CharSequence.
     * In method {@link Matcher#matches} each iterations begins with method
     * {@link CharSequence#charAt}. If Matcher can't determine the match with
     * pattern in a {@link TimeoutCharSequence#TIMEOUT_MILLIS} it will cause
     * RuntimeException that will be caught in main method. It helps to avoid
     * Catastrophic Backtracking but this does not guarantee the correct answer.
     */
    public boolean matches(@NotNull String text, @NotNull String regex) {

        boolean result = false;

        try {
            TimeoutCharSequence seq = new TimeoutCharSequence(text);
            result = Pattern.compile(regex).matcher(seq).matches();
        } catch (PatternSyntaxException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Regex Syntax error:\n" + e.getMessage());
        }

        return result;
    }

    /**
     * This class is wrapper for CharSequence.
     *
     * Method {@link #charAt} is used as Callback in {@link Matcher#matches}
     * to throw RuntimeException after {@link #TIMEOUT_MILLIS} have passed
     * since class initialization.
     */
    private static class TimeoutCharSequence implements CharSequence {

        private final long TIMEOUT_MILLIS = 1000L;

        private final CharSequence seq;

        private final long timeoutTime;

        public TimeoutCharSequence(CharSequence text) {
            super();
            seq = text;
            timeoutTime = System.currentTimeMillis() + TIMEOUT_MILLIS;
        }

        public char charAt(int index) {
            if (System.currentTimeMillis() > timeoutTime) {
                throw new RuntimeException("Timeout. Be sure that there is no nested repetition operators in Your regex.");
            }
            return seq.charAt(index);
        }

        public int length() {
            return seq.length();
        }

        public CharSequence subSequence(int start, int end) {
            return new TimeoutCharSequence(seq.subSequence(start, end));
        }

        @NotNull
        @Override
        public String toString() {
            return seq.toString();
        }
    }
}
