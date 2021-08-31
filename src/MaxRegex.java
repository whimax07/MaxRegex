import java.util.ArrayDeque;
import java.util.HashMap;

public class MaxRegex {

    public static boolean whollyContainedIn(Groups groups, String string) {
        // TODO(Max): Replace staredTracking and staredTrackingOrder with a deque of StaredHistory.
        HashMap<Group, StaredHistory> staredTracking = new HashMap<>();
        ArrayDeque<Group> staredTrackingOrder = new ArrayDeque<>();

        // firstFreeChar is the next available charter fro a group to use. groupPos is the current group we are looking for.
        int strPos = 0, groupPos = 0;

        while (groupPos < groups.size()) {
            Group group = groups.get(groupPos);

            if (group.stared) {
                StaredHistory staredHistory = staredTracking.get(group);
                // Note(Max): This is the first time this group has been seen so add it to the tracking. We start with a
                // zero length search.
                if (staredHistory == null) {
                    staredTracking.put(group, new StaredHistory(groupPos, strPos));
                    staredTrackingOrder.add(group);
                    groupPos++;
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
        }

        return false;
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
