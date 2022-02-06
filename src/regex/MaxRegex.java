package regex;

public class MaxRegex {

    /** This method converts a string pattern to an array list of {@link storage.Chunk Chunk} (token likes) that the
     *  regex search can use. It is a convenience method and public interface for {@link PatternChunks}.
     */
    public static PatternChunks tokenize(String pattern) {
        return new PatternChunks(pattern);
    }

    /** This method matches a chunked (tokenized) pattern to the full length of the string to be tested. It is a
     * convenience method and public interface for {@link WholePatternCheck}.
     *
     * @return true if the pattern can match the whole of the string being tested. False is returned otherwise.
     */
    public static boolean whollyContainedIn(PatternChunks patternChunks, String string) {
        return new WholePatternCheck(patternChunks, string).search();
    }

    /** This method matches a pattern to the full length of the string to be tested. It is a convenience
     * method for {@link MaxRegex#tokenize(String)} and {@link MaxRegex#whollyContainedIn(PatternChunks, String)}.
     *
     * @return true if the pattern can match the whole of the string being tested. False is returned otherwise.
     */
    public static boolean whollyContainedIn(String pattern, String string) {
        PatternChunks patternChunks = tokenize(pattern);
        return new WholePatternCheck(patternChunks, string).search();
    }

}
