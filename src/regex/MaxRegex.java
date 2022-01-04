package regex;

import utils.Groups;

public class MaxRegex {

    public static boolean whollyContainedIn(Groups groups, String string) {
        return new WholePatternCheck(groups, string).search();
    }

}
