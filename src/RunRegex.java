import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunRegex {

    private long testCount = 0;
    private boolean randomTestFailed = false;

    public static void main(String[] args) {
        RunRegex runRegex = new RunRegex();
        runRegex.checkMaxRegex("aa", "a", false);
        runRegex.checkMaxRegex("aa", "a*", true);
        runRegex.checkMaxRegex("ab", ".*", true);
        runRegex.checkMaxRegex("aab", "c*a*b", true);
        runRegex.checkMaxRegex("mississippi", "mis*is*p*.", false);
        runRegex.checkMaxRegex("abcd", ".....", false);

        System.out.println("\n");
        runRegex.checkMaxRegex("t", "o*t*", true);
        runRegex.checkMaxRegex("o", "o*t*", true);
        runRegex.checkMaxRegex("ot", "o*t*", true);
        runRegex.checkMaxRegex("ott", "o*t*", true);
        runRegex.checkMaxRegex("ot", "o*t*a*", true);
        runRegex.checkMaxRegex("ota", "o*t*a*", true);
        runRegex.checkMaxRegex("otta", "o*t*a*", true);

        System.out.println("\n");
        runRegex.checkMaxRegex("o", "o*t", false);
        runRegex.checkMaxRegex("ot", "o*t", true);
        runRegex.checkMaxRegex("ott", "o*t", false);
        runRegex.checkMaxRegex("ot", "o*t*a", false);
        runRegex.checkMaxRegex("oa", "o*t*a", true);
        runRegex.checkMaxRegex("ota", "o*t*a", true);
        runRegex.checkMaxRegex("otta", "o*t*a", true);

        System.out.println("\n");
        System.out.println("Starting the randomised tests.");
        for (long i = 0; i < 10_000_000; i++) {
            runRegex.randomTest();
            if (i % 100_000 == 0) {
                System.out.println("Completed " + i + ".");
            }
        }
        if (!runRegex.randomTestFailed) {
            System.out.println("No tests were failed.");
        }
    }

    public void checkMaxRegex(String string, String pattern, boolean truth) {
        testCount++;
        Groups groups = Groups.tokenize(pattern);
        if (MaxRegex.whollyContainedIn(groups, string) == truth) {
            System.out.println("Test " + testCount + " passed.");
        } else {
            System.out.println("Test " + testCount + " failed.");
        }
    }

    public void randomTest() {
        StringBuilder stringBuilder;

        // Make the string.
        int sLen = (int) (Math.random() * 20) + 1;
        stringBuilder = new StringBuilder();
        for (int i = 0; i < sLen; i++) {
            stringBuilder.append((char) ((Math.random() * 25) + 97));
        }
        String string = stringBuilder.toString();

        // Make the pattern.
        int pLen = (int) (Math.random() * 30) + 1;
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

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(string);

        Groups groups = Groups.tokenize(pattern);
        if (m.matches() != MaxRegex.whollyContainedIn(groups, string)) {
            System.out.println("String: " + string + " Pattern: " + pattern);
            randomTestFailed = true;
        }
    }

}
