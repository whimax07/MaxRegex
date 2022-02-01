package regex;

import storage.Chunk;
import storage.Symbol;

import java.util.ArrayList;

public class PatternChunks extends ArrayList<Chunk> {

    private String pattern;

    private ArrayList<Symbol> symbols;

    private Chunk curChunk;



    public PatternChunks() { }

    public PatternChunks(String pattern) {
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
        curChunk = new Chunk(symbols.get(0));

        for (int i = 1, symbolsSize = symbols.size(); i < symbolsSize; i++) {
            Symbol symbol = symbols.get(i);
            includeSymbol(symbol);
        }

        this.add(curChunk);
    }

    private void includeSymbol(Symbol symbol) {
        if (curChunk.isRepeatable() || symbol.repeated || curChunk.getCharClass() != symbol.charClass) {
            this.add(curChunk);
            curChunk = new Chunk(symbol);
            return;
        }

        curChunk.appendChar(symbol.c);
    }

}
