package regex;

import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple limited regex implementation without recursion.
 * Usable syntax:
 * - Alphanumerics and spaces.
 * - Any character or wild card, "." the full stop.
 * - Greedy zero or more of the previous, "*" Asterisk or star.
 */
public class SimpleRegex {

    private final String input;

    private int inputPos = 0;

    private final String pattern;

    private int patternPos = 0;

    private final ArrayDeque<Integer> repeatCounts = new ArrayDeque<>();



    public SimpleRegex(String input, String pattern) {
        this.input = input;
        this.pattern = pattern;
    }



    public boolean match() {
        while (true) {
            boolean requestGoBack = false;

            if (patternPos < pattern.length() - 1 && pattern.charAt(patternPos + 1) == '*') {
                // Can always match at least zero.
                doRepeatSearch();
            } else {
                requestGoBack = !doSingleSearch();
            }

            if (!requestGoBack && patternPos >= pattern.length() && inputPos >= input.length()) {
                // Match found.
                return true;
            }

            if (patternPos >= pattern.length()) {
                requestGoBack = true;
            }

            // This is crazy. Else on different lines is not a good idea.
            else if (inputPos >= input.length()
                    && (patternPos == pattern.length() - 1 || pattern.charAt(patternPos + 1) != '*')) {
                requestGoBack = true;
            }

            if (requestGoBack) {
                boolean whenBack = goBack();
                if (!whenBack) {
                    // No match found.
                    return false;
                }
            }
        }
    }

    private boolean goBack() {
        // While loop rather than recursion.
        while (!repeatCounts.isEmpty()) {
            do {
                patternPos -= 1;
                inputPos -= 1;

                // Note(Max): It is possible that these two checks could be less than 1.
                if (inputPos < 0 || patternPos < 0) {
                    return false;
                }
            } while (pattern.charAt(patternPos) != '*');

            if (repeatCounts.peek() > 0) {
                // 1. We have hit the star and therefore the end of the input the star is matching.
                // 2. Reduce the number of values the star is matching by NOT increasing the inputPos for the next round
                // of searches.
                // 3. Increment the patternPos to move that to the item needing to be matched.
                repeatCounts.push(repeatCounts.pop() - 1);
                patternPos += 1;
                return true;
            }

            // The repeat count was at zero so keep trying to roll back and restore the unneeded decrement.
            inputPos += 1;
            // Move past the character attached to the zero repeat star.
            patternPos -= 1;
            repeatCounts.pop();
        }

        // There are no star patterns to roll back to.
        return false;
    }

    /**
     * This is a greedy star match.
     */
    private void doRepeatSearch() {
        int oldInputPos = inputPos;
        char curChar = pattern.charAt(patternPos);

        if (pattern.charAt(patternPos) == '.') {
            inputPos = input.length();
            repeatCounts.push(inputPos - oldInputPos);
            patternPos += 2;
            return;
        }

        while (inputPos < input.length() && input.charAt(inputPos) == curChar) {
            inputPos += 1;
        }

        repeatCounts.push(inputPos - oldInputPos);
        // Move past both the token and the star.
        patternPos += 2;
    }

    private boolean doSingleSearch() {
        boolean found = pattern.charAt(patternPos) == '.'
                || pattern.charAt(patternPos) == input.charAt(inputPos);
        patternPos += 1;
        inputPos += 1;
        return found;
    }



    public static void test(String pattern, String input) {
        SimpleRegex simpleRegex = new SimpleRegex(input, pattern);
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        boolean matches = m.matches();
        if (matches != simpleRegex.match()) {
            throw new RuntimeException("Regex wrong: pattern: " + pattern + " input: " + input);
        }
    }

}
