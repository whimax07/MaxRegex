package storage;

import java.util.Objects;

/**
 * A chunk is a mix of a token and a semantic object. They are Groups of tokens that have been combined into one search
 * object.
 */
public class Chunk {

    private String string = "";

    private boolean isRepeatable;

    private E_CharClass charClass;



    public Chunk() { }

    public Chunk(String string, boolean isRepeatable, E_CharClass charClass) {
        this.string = string;
        this.isRepeatable = isRepeatable;
        this.charClass = charClass;
    }

    public Chunk(Symbol symbol) {
        this(Character.toString(symbol.c), symbol.repeated, symbol.charClass);
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

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setCharClass(E_CharClass charClass) {
        this.charClass = charClass;
    }

    public E_CharClass getCharClass() {
        return charClass;
    }



    @Override
    public int hashCode() {
        return Objects.hash(string, isRepeatable, charClass);
    }

    @Override
    public String toString() {
        return "Group[" +
                "chars=" + string + ", " +
                "isRepeatable=" + isRepeatable + ", " +
                "charClass=" + charClass + ']';
    }

}
