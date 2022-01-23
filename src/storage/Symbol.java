package storage;

public class Symbol {

    public final char c;

    public final boolean repeated;

    public final E_CharClass charClass;



    public Symbol(char c, boolean repeated) {
        this.c = c;
        this.repeated = repeated;
        if (c == '.') {
            charClass = E_CharClass.DOT;
        } else {
            charClass = E_CharClass.ALPHANUMERIC;
        }
    }

}
