import regex.SimpleRegex;

public class SingleTreadTest {

    private static final int testsToRun_million = 50;



    public static void runSingleTreadTests() {
        failedCases();

        for (int j = 0; j < testsToRun_million; j++) {
            for (int i = 0; i < 1_000_000; i++) {
                randomPatternAndInput();
            }
            System.out.println("Passed " + (j + 1) + " million random tests.");
        }
        System.out.println("Finished.");
    }

    private static void failedCases() {
        SimpleRegex.test(".*.*.", "f");
        SimpleRegex.test(".*.*.", "sj");
        SimpleRegex.test("..*", "m");
        SimpleRegex.test(".*", "ntthubh");
    }

    /**
     * An over estimate of the size of the pattern space of meaningfully different strings.
     *
     * pattern1 = "abc", pattern2 = "abd" and input = "abz".
     * Here pattern1 and pattern2 are not meaningfully different as we are happy the algorithm can do char comparisons.
     *
     * Size of pattern space = sum_over(pattern_len)((i * 2 + 2) ^ i)
     * Size of input space = sum_over(inputLen)((i * 2 + 2) ^ i)
     */
    private static void randomPatternAndInput() {
        StringBuilder stringBuilder;

        // Make the input.
        int sLen = (int) (Math.random() * 10) + 1;
        stringBuilder = new StringBuilder();
        for (int i = 0; i < sLen; i++) {
            stringBuilder.append((char) ((Math.random() * 25) + 97));
        }
        String input = stringBuilder.toString();

        // Make the pattern.
        int pLen = (int) (Math.random() * 8) + 1;
        double chanceForStar = Math.random();
        stringBuilder = new StringBuilder();
        for (int i = 0; i < pLen; i++) {
            int num = (int) (Math.random() * 26);
            if (num == 0) {
                stringBuilder.append(".");
            } else {
                stringBuilder.append((char) num + 96);
            }
            if (Math.random() > chanceForStar) {
                stringBuilder.append('*');
            }
        }
        String pattern = stringBuilder.toString();

        SimpleRegex.test(pattern, input);
    }

}
