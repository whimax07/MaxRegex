package storage;

import java.util.ArrayList;

public class Range implements I_Terminate {

    private final ArrayList<Character> charters = new ArrayList<>();


    @Override
    public SearchResult match(int stringIdx, String input) {
        if (stringIdx + 1 > input.length()) {
            return new SearchResult(-1, false);
        }

        char inputChar = input.charAt(stringIdx);
        for (char charter : charters) {
            if (inputChar == charter) {
                return new SearchResult(stringIdx + 1, true);
            }
        }

        return new SearchResult(-1, false);
    }



    public void addCharter(char charter) {
        charters.add(charter);
    }

    public ArrayList<Character> getCharters() {
        return charters;
    }


    @Override
    public String toString() {
        return "RangeChunk{" +
                "strings=" + charters +
                '}';
    }

}
