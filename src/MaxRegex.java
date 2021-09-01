import java.util.ArrayDeque;
import java.util.HashMap;

public class MaxRegex {

    public static boolean whollyContainedIn(Groups groups, String string) {
        // TODO(Max): Replace staredTracking and staredTrackingOrder with a deque of StaredHistory.
        HashMap<Group, StaredHistory> staredTracking = new HashMap<>();
        ArrayDeque<Group> staredTrackingOrder = new ArrayDeque<>();

        // firstFreeChar is the next available charter fro a group to use. groupPos is the current group we are looking for.
        int strPos = 0, groupPos = 0;

        while (true) {
            Group group = groups.get(groupPos);

            if (group.stared) {
                StaredHistory staredHistory = staredTracking.get(group);
                // Note(Max): If this is the "first time this group has been seen" staredHistory is null so add it to
                // the tracking. As its stared we can start with a zero length search, aka just increment groupPos check
                // the state of the search.
                if (staredHistory == null) {
                    staredTracking.put(group, new StaredHistory(groupPos, strPos));
                    staredTrackingOrder.addFirst(group);

                    groupPos++;

                    // TODO(Max): The below is the same as the end of the function so remove duplicate code.
                    if (groupPos < groups.size()) {
                        continue;
                    }
                    // groupPos == groups.size()

                    // This is the case where we are done and happy, all groups matched to the full string.
                    if (strPos == string.length()) {
                        return true;
                    }

                    // This is the case where the pattern has been matched but it isn't the full string.
                    groupPos--;
                    continue;
                }
            }

            // Check for group.
            SearchResult searchResult = findGroup(group, strPos, string);

            if (!searchResult.found) {
                if (group.stared) {
                    if (staredTrackingOrder.isEmpty()) {
                        return false;
                    }
                    Group popped = staredTrackingOrder.removeFirst();
                    staredTracking.remove(popped);
                }
                if (staredTrackingOrder.isEmpty()) {
                    return false;
                }
                StaredHistory rollbackPoint = staredTracking.get(staredTrackingOrder.peekFirst());
                groupPos = rollbackPoint.groupPos;
                strPos = rollbackPoint.strPos;
                continue;
            }

            // We have found a match for this current group!
            strPos = searchResult.firstFreeChar;
            if (group.stared) {
                staredTracking.put(group, new StaredHistory(groupPos, strPos));
            }

            groupPos++;

            if (groupPos < groups.size()) {
                continue;
            }
            // groupPos == groups.size()

            // This is the case where we are done and happy, all groups matched to the full string.
            if (strPos == string.length()) {
                return true;
            }

            // This is the case where the pattern has been matched but it isn't the full string.
            if (group.stared) {
                groupPos--;
                continue;
            }

            // All of the groups have been used but we can't get any further. So we have to try and rollback. This also
            // means that the last group is not stared. (If it was we could try the next char.)
            if (staredTrackingOrder.isEmpty()) {
                return false;
            }
            StaredHistory rollbackPoint = staredTracking.get(staredTrackingOrder.peekFirst());
            groupPos = rollbackPoint.groupPos;
            strPos = rollbackPoint.strPos;
        }
    }

    private static SearchResult findGroup(Group group, int strPos, String string) {
        if (group.s.length() + strPos > string.length()) {
            return new SearchResult(-1, false);
        }

        if (group.charClass == E_CharClass.DOT) {
            //noinspection DuplicateExpressions
            return new SearchResult(group.s.length() + strPos, true);
        }

        // Note(Max): The dot case and the below have been separated as I don't want to have subString being called
        // unless it needed.
        String subString = string.substring(strPos, strPos + group.s.length());
        if (subString.equals(group.s)) {
            //noinspection DuplicateExpressions
            return new SearchResult(group.s.length() + strPos, true);
        }

        return new SearchResult(-1, false);
    }

    private static class StaredHistory {
        public int groupPos;
        public int strPos;

        public StaredHistory(int groupPos, int strPos) {
            this.groupPos = groupPos;
            this.strPos = strPos;
        }
    }

    private record SearchResult(int firstFreeChar, boolean found) {}
}
