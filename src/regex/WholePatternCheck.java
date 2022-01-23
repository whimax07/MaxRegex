package regex;

import storage.*;

public class WholePatternCheck {

    private final DequeMap<Group, RepeatableHistory> repeatablesTracking = new DequeMap<>();

    private final PatternTokens patternTokens;

    private final String input;

    private int strPos = 0;

    private int groupPos = 0;



    public WholePatternCheck(PatternTokens patternTokens, String input) {
        this.patternTokens = patternTokens;
        this.input = input;
    }



    public boolean search() {
        while (true) {
            E_State searchState = searchLoop();
            switch (searchState) {
                case SEARCHING: break;
                case REQUEST_UNWIND: unwind(); break;
                case SUCCESS: return true;
                case FAILURE: return false;
                default: throw new RuntimeException("The searchLoop has returned an ambitious state.");
            }
        }
    }

    private E_State searchLoop() {
        Group group = patternTokens.get(groupPos);

        // Note(Max): Here is where I would probably put the branch for a search with a greedy group.
        return lazySearchGroup(group);
    }

    private E_State lazySearchGroup(Group group) {
        if (newRepeatGroup(group)) {
            return check0Repeat();
        }

        SearchResult searchResult = matchGroup(group, strPos, input);

        if (searchResult.found()) {
            return groupMatchFound(group, searchResult);
        } else {
            return groupNotFound(group);
        }
    }

    private boolean newRepeatGroup(Group group) {
        if (group.isRepeatable()) {
            RepeatableHistory repeatableHistory = repeatablesTracking.get(group);

            // Note(Max): If this is the "first time this group has been seen" staredHistory is null so add it to
            // the tracking. As its stared we can start with a zero length search, aka just increment groupPos check
            // the state of the search.
            if (repeatableHistory == null) {
                repeatablesTracking.putFirst(group, new RepeatableHistory(groupPos, strPos));
                return true;
            }
        }
        return false;
    }

    private E_State check0Repeat() {
        groupPos++;

        if (groupPos < patternTokens.size()) {
            return E_State.SEARCHING;
        }
        // groupPos == groups.size()

        // This is the case where we are done and happy, all groups matched to the full string.
        if (strPos == input.length()) {
            return E_State.SUCCESS;
        }

        // This is the case where the pattern has been matched, but it isn't the full string.
        groupPos--;
        return E_State.SEARCHING;
    }

    private static SearchResult matchGroup(Group group, int strPos, String string) {
        if (group.getString().length() + strPos > string.length()) {
            return new SearchResult(-1, false);
        }

        if (group.getCharClass() == E_CharClass.DOT) {
            return new SearchResult(group.getString().length() + strPos, true);
        }

        // Note(Max): The dot case and the below have been separated as I don't want to have subString being called
        // unless it needed.
        String subString = string.substring(strPos, strPos + group.getString().length());
        if (subString.equals(group.getString())) {
            return new SearchResult(group.getString().length() + strPos, true);
        }

        return new SearchResult(-1, false);
    }

    private E_State groupMatchFound(Group group, SearchResult searchResult) {
        strPos = searchResult.firstFreeChar();

        if (group.isRepeatable()) {
            repeatablesTracking.putFirst(group, new RepeatableHistory(groupPos, strPos));
        }

        groupPos++;

        if (groupPos < patternTokens.size()) {
            return E_State.SEARCHING;
        }
        // groupPos == groups.size()

        // This is the case where we are done and happy, all groups matched to the full string.
        if (strPos == input.length()) {
            return E_State.SUCCESS;
        }

        // This is the case where the pattern has been matched but it isn't the full string.
        if (group.isRepeatable()) {
            groupPos--;
            return E_State.SEARCHING;
        }

        // All the groups have been used, but we can't get any further. So we have to try and rollback. This also
        // means that the last group is not stared. (If it was we could try the next char(s).)
        if (repeatablesTracking.isEmpty()) {
            return E_State.FAILURE;
        }

        return E_State.REQUEST_UNWIND;
    }

    private E_State groupNotFound(Group group) {
        // At the start of this function tracking is only empty if we haven't seen a repeatable group yet.

        if (group.isRepeatable()) {
            // We have gone too far with this current match.
            repeatablesTracking.removeFirst();
        }

        // If tracking is empty then we cannot unwind. This makes the layout of the match up to this point "rigid" and
        // therefore the match has failed.
        if (repeatablesTracking.isEmpty()) {
            return E_State.FAILURE;
        }

        return E_State.REQUEST_UNWIND;
    }

    private void unwind() {
        RepeatableHistory rollbackPoint = repeatablesTracking.getFirst();
        groupPos = rollbackPoint.groupPos();
        strPos = rollbackPoint.strPos();
    }

}
