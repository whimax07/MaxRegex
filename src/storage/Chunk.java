package storage;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A chunk is a mix of a token and a semantic object. They are Groups of tokens that have been combined into one search
 * object.
 */
public class Chunk {

    private final ArrayList<Terminator> terminators = new ArrayList<>();

    private boolean isLeaf;

    private final ArrayList<Chunk> chunks = new ArrayList<>();



    public SearchResult matchChunk(int stringIdx, String input) {

    }



    public void appendTerminator(Terminator terminator) {
        terminators.add(terminator);
    }

    public ArrayList<Terminator> getTerminators() {
        return terminators;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void appendChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    public ArrayList<Chunk> getChunks() {
        return chunks;
    }



    @Override
    public int hashCode() {
        return Objects.hash(terminators);
    }

}
