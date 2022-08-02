package regex;

import storage.*;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class WholePatternCheckTemp {

    private final ArrayDeque<UnionHistory> unionHistories = new ArrayDeque<>();

    private final ArrayList<Union> unions;

    private final String input;

    private int inputIndex = 0;

    private int unionIndex = 0;

    private int terminatorIndex = 0;

    private int unionSize;

    private Union union;

    private Terminator terminator;



    public WholePatternCheckTemp(ArrayList<Union> unions, String input) {
        this.unions = unions;
        this.input = input;
    }



    public boolean search() {
        while (true) {
            E_State searchState = searchLoop();

            switch (searchState) {
                case REQUEST_UNWIND: {
                    if (!unwind()) {
                        return false;
                    }
                    break;
                }
                case SUCCESS: return true;
                case FAILURE: return false;
                case NEXT_UNION: break;
            }
        }
    }

    private E_State searchLoop() {
        union = unions.get(unionIndex);
        unionSize = union.size();

        while (terminatorIndex < unionSize) {
            terminator = union.get(terminatorIndex);
            E_State state = terminatorSearch();
            if (state != E_State.NEXT_TERM) {
                return state;
            }
            terminatorIndex++;
        }

        return E_State.REQUEST_UNWIND;
    }

    private E_State terminatorSearch() {
        // Search for the terminator.
        SearchResult searchResult = terminator.match(input, inputIndex);

        // Deal with a match.
        if (searchResult.found()) {
            return terminatorMatched(searchResult);
        }

        // Deal without a match.
        return terminatorNotMatched(searchResult);
    }

    private E_State terminatorMatched(SearchResult searchResult) {

        boolean lastUnion = isLastUnion();

        if (lastUnion && isEndOfString(searchResult)) {
            return E_State.SUCCESS;
        }

        if (isEndOfString(searchResult)) {
            return E_State.REQUEST_UNWIND;
        }

        if (lastUnion && terminator.isRepeated()) {
            return E_State.REPEAT_TERM;
        }

        if (lastUnion) {
            return E_State.NEXT_TERM;
        }

        unionHistories.push(new UnionHistory(unionIndex, terminatorIndex, inputIndex));
        return E_State.NEXT_UNION;
    }

    private E_State terminatorNotMatched(SearchResult searchResult) {
        if (isLastTerminator()) {
            return E_State.REQUEST_UNWIND;
        }

        if (terminator.isRepeated()) {

        }

        return E_State.NEXT_TERM;
    }

    private boolean unwind() {
        if (unionHistories.size() == 0) {
            return false;
        }

        UnionHistory history = unionHistories.pop();
        unionIndex = history.unionIndex();
        union = unions.get(unionIndex);
        terminatorIndex = history.terminatorIndex();
        terminator = union.get(terminatorIndex);

        if (terminator.isRepeated()) {
            return true;
        }

        terminatorIndex++;
        if (terminatorIndex < union.size()) {
            terminator = union.get(terminatorIndex);
            return true;
        }

        return unwind();
    }


//
//    /*private void checkUnionTracking() {
//        if (unionHistories.size() <= unionIndex) {
//            unionHistories.push(new UnionHistory(0, inputIndex));
//        }
//
//        UnionHistory unionHistory = unionHistories.peek();
//        assert unionHistory != null;
//        terminatorIndex = unionHistory.terminatorIndex();
//    }*/
//
//    private E_State terminatorSearch(Union union, Terminator terminator) {
//        if (currentTerminator == terminatorIndex && terminator.isRepeated()) {
//            return zeroRepeat();
//        }
//
//        SearchResult searchResult = terminator.match(inputIndex, input);
//
//        if (!searchResult.found()) {
//            return terminatorNotFound(terminator);
//        }
//
//        return terminatorMatchFound(terminator, searchResult);
//    }
//
//    private E_State zeroRepeat() {
//
//    }
//
//    private E_State terminatorNotFound(Terminator terminator) {
//        if (terminatorIndex < unionSize - 1) {
//            return E_State.SEARCHING;
//        }
//
//        // At this point tracking is only empty if we haven't seen a repeatable group yet.
//        if (terminator.isRepeated()) {
//            // We have gone too far with this current match.
//            unionHistories.pop();
//        }
//
//        // If tracking is empty then we cannot unwind. This makes the layout of the match up to this point "rigid" and
//        // therefore the match has failed.
//        if (unionHistories.isEmpty()) {
//            return E_State.FAILURE;
//        }
//
//        return E_State.REQUEST_UNWIND;
//    }
//
//    private E_State terminatorMatchFound(Terminator terminator, SearchResult searchResult) {
//        int oldInputIndex = inputIndex;
//        inputIndex = searchResult.firstFreeChar();
//
//        if (terminator.isRepeated() || unions.get(unionIndex).size() > 1) {
//            unionHistories.push(new UnionHistory(unionIndex, terminatorIndex, inputIndex));
//        }
//
//        unionIndex++;
//
//        if (unionIndex < unions.size()) {
//            return E_State.SEARCHING;
//        }
//        // unionIndex == unions.size()
//
//        // This is the case where we are done and happy, all groups matched to the full string.
//        if (inputIndex == input.length()) {
//            return E_State.SUCCESS;
//        }
//
//        // This is the case where the pattern has been matched, but it isn't the full string.
//        if (terminator.isRepeated()) {
//            // Undo the earlier increment in this function.
//            unionIndex--;
//            return E_State.SEARCHING;
//        }
//
//        if (terminatorIndex < unionSize - 1) {
//            inputIndex = oldInputIndex;
//            unionHistories.pop();
//            return E_State.SEARCHING;
//        }
//
//        // All the unions have been used, but we can't get any further. So we have to try and rollback.
//        if (unionHistories.isEmpty()) {
//            return E_State.FAILURE;
//        }
//
//        return E_State.REQUEST_UNWIND;
//    }
//
//    private void unwind() {
//        UnionHistory unionHistory = unionHistories.pop();
//        inputIndex = unionHistory.inputIndex();
//        unionIndex = unionHistory.unionIndex();
//        terminatorIndex = unionHistory.terminatorIndex();
//    }


    private boolean isLastUnion() {
        return unions.size() == unionIndex + 1;
    }

    private boolean isLastTerminator() {
        return union.size() == terminatorIndex + 1;
    }

    private boolean isEndOfString(SearchResult searchResult) {
        return input.length() == searchResult.firstFreeChar();
    }

}
