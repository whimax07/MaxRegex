package regex;

import storage.Group;
import storage.Symbol;

import java.util.ArrayList;

public class PatternTokens extends ArrayList<Group> {

    private String pattern;

    private ArrayList<Symbol> symbols;

    private Group curGroup;



    public PatternTokens() { }

    public PatternTokens(String pattern) {
        super();
        tokenize(pattern);
    }



    public void tokenize(String pattern) {
        this.pattern = pattern;
        this.clear();

        if (pattern.length() == 0) {
            return;
        }

        getSymbols();

        buildTokens();
    }

    private void getSymbols() {
        symbols = new ArrayList<>();

        char curChar = pattern.charAt(0);

        for (int i = 1; i < pattern.length(); i++) {
            char nextChar = pattern.charAt(i);

            if (curChar != '*') {
                boolean isAstrix = nextChar == '*';
                symbols.add(new Symbol(curChar, isAstrix));
            }

            curChar = nextChar;
        }

        // curChar == nextChar aka the last charter in pattern.
        if (curChar == '*') {
            return;
        }

        symbols.add(new Symbol(curChar, false));
    }

    private void buildTokens() {
        // Init the curGroup to prevent null point errors.
        curGroup = new Group(symbols.get(0));

        for (int i = 1, symbolsSize = symbols.size(); i < symbolsSize; i++) {
            Symbol symbol = symbols.get(i);
            includeSymbol(symbol);
        }

        this.add(curGroup);
    }

    private void includeSymbol(Symbol symbol) {
        if (curGroup.isRepeatable() || symbol.repeated || curGroup.getCharClass() != symbol.charClass) {
            this.add(curGroup);
            curGroup = new Group(symbol);
            return;
        }

        curGroup.appendChar(symbol.c);
    }


//    private void buildTokens() {
//        StringBuilder stringBuilder = new StringBuilder();
//        E_CharClass curSymbolChar = symbols.get(0).charClass;
//        boolean checkSymbolClass = false;
//
//
//        for (int i = 0; i < symbols.size(); i++) {
//            Symbol symbol = symbols.get(i);
//
//            if (checkSymbolClass) {
//                curSymbolChar = symbol.charClass;
//                checkSymbolClass = false;
//            }
//
//            if (symbol.repeated) {
//                if (!stringBuilder.isEmpty()) {
//                    this.add(new Group(stringBuilder.toString(), false, curSymbolChar));
//                }
//                this.add(new Group("" + symbol.c, true, symbol.charClass));
//                stringBuilder = new StringBuilder();
//
//                checkSymbolClass = true;
//                continue;
//            }
//
//            if (symbol.charClass != curSymbolChar) {
//                this.add(new Group(stringBuilder.toString(), false, curSymbolChar));
//                stringBuilder = new StringBuilder();
//                curSymbolChar = symbol.charClass;
//            }
//            stringBuilder.append(symbol.c);
//        }
//
//        if (!stringBuilder.isEmpty()) {
//            this.add(new Group(stringBuilder.toString(), false, curSymbolChar));
//        }
//    }

}
