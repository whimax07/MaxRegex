import java.util.ArrayList;

public class Groups extends ArrayList<Group> {

    public static Groups tokenize(String pattern) {
        if (pattern.length() == 0) {
            return new Groups();
        }

        // Note(Max): This is worse for performance than inlining it with the next for-loop. I don't want to inline it
        // and I think making a generator or a function to do it is likely slower even if it is more memory efficient.
        ArrayList<SmallToken> smallTokens = new ArrayList<>();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '*') {
                continue;
            }
            if (i == pattern.length() - 1) {
                smallTokens.add(new SmallToken(c, false));
                break;
            }

            char d = pattern.charAt(i + 1);
            boolean started = d == '*';
            smallTokens.add(new SmallToken(c, started));
        }

        Groups groups = new Groups();
        StringBuilder s = new StringBuilder();
        E_CharClass curCharClass = smallTokens.get(0).charClass;
        boolean getCurCharClass = false;

        for (int i = 0; i < smallTokens.size(); i++) {
            SmallToken smallToken = smallTokens.get(i);

            if (getCurCharClass) {
                curCharClass = smallToken.charClass;
                getCurCharClass = false;
            }

            if (smallToken.stared) {
                if (!s.isEmpty()) {
                    groups.add(new Group(s.toString(), false, curCharClass));
                }
                groups.add(new Group("" + smallToken.c, true, smallToken.charClass));
                s = new StringBuilder();
                // It's important that the current char isn't added to s as it has already been added to a group and
                // that the group of the current char isn't carried forward.
                getCurCharClass = true;
                continue;
            }

            if (smallToken.charClass != curCharClass) {
                groups.add(new Group(s.toString(), false, curCharClass));
                s = new StringBuilder();
                curCharClass = smallToken.charClass;
            }
            s.append(smallToken.c);
        }

        if (!s.isEmpty()) {
            groups.add(new Group(s.toString(), false, curCharClass));
        }

        return groups;
    }

    public static class SmallToken {
        public final char c;
        public final boolean stared;
        public final E_CharClass charClass;

        public SmallToken(char c, boolean stared) {
            this.c = c;
            this.stared = stared;
            if (c == '.') {
                charClass = E_CharClass.DOT;
            } else {
                charClass = E_CharClass.ALPHANUMERIC;
            }
        }
    }
}
