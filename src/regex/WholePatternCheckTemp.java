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



    public WholePatternCheckTemp(ArrayList<Union> unions, String input) {
        this.unions = unions;
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
        Union union = unions.get(unionIndex);

        checkUnionTracking();

        for (int i = terminatorIndex; i < union.size(); i++) {
            Terminator terminator = union.get(i);
            E_State searchState = terminatorSearch(terminator, i);
            if (searchState != E_State.SEARCHING) {
                return searchState;
            }
        }

        return E_State.REQUEST_UNWIND;
    }

    private void checkUnionTracking() {
        if (unionHistories.size() <= unionIndex) {
            unionHistories.push(new UnionHistory(0, inputIndex));
        }

        UnionHistory unionHistory = unionHistories.peek();
        assert unionHistory != null;
        terminatorIndex = unionHistory.terminatorIndex();
    }

    private E_State terminatorSearch(Terminator terminator, int currentTerminator) {
        if (currentTerminator == terminatorIndex && terminator.isRepeated()) {
            return zeroRepeat();
        }

        SearchResult searchResult = terminator.getTerminate().match(inputIndex, input);

        if (!searchResult.found()) {
            return terminatorNotFound(terminator);
        }

        return terminatorMatchFound(terminator, searchResult);
    }






    private void unwind() {

    }


}
