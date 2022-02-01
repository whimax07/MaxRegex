package regex;

public class MaxRegex {

    public static boolean whollyContainedIn(PatternChunks patternChunks, String string) {
        return new WholePatternCheck(patternChunks, string).search();
    }

}
