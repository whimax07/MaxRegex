package storage;

import java.util.Objects;

public class BaseString implements I_Terminate {

    private String string = "";

    private E_CharClass charClass;



    public BaseString() { }

    public BaseString(String string, E_CharClass charClass) {
        this.string = string;
        this.charClass = charClass;
    }

    public BaseString(Symbol symbol) {
        this(Character.toString(symbol.c), symbol.charClass);
    }



    public SearchResult match(int stringIdx, String input) {
        if (getString().length() + stringIdx > input.length()) {
            return new SearchResult(-1, false);
        }

        if (getCharClass() == E_CharClass.DOT) {
            return new SearchResult(getString().length() + stringIdx, true);
        }

        // Note(Max): The dot case does not need charter checking so we do that before getting a substring.
        String subString = input.substring(stringIdx, stringIdx + getString().length());
        if (subString.equals(getString())) {
            return new SearchResult(getString().length() + stringIdx, true);
        }

        return new SearchResult(-1, false);
    }



    public void setString(String string) {
        this.string = string;
    }

    public void appendChar(char charter) {
        this.string += Character.toString(charter);
    }

    public String getString() {
        return string;
    }

    public void setCharClass(E_CharClass charClass) {
        this.charClass = charClass;
    }

    public E_CharClass getCharClass() {
        return charClass;
    }



    @Override
    public int hashCode() {
        return Objects.hash(string, charClass);
    }

    @Override
    public String toString() {
        return "Group[" +
                "chars=" + string + ", " +
                "charClass=" + charClass + ']';
    }

}
