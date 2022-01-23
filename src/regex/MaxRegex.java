package regex;

public class MaxRegex {

    public static boolean whollyContainedIn(PatternTokens patternTokens, String string) {
        return new WholePatternCheck(patternTokens, string).search();
    }

}
