package storage;

public interface I_Chunk {

    boolean isUnion();

    int unionPosition();

    boolean isRepeatable();

    SearchResult matchChunk(int stringIdx, String input);

    /**
     * This defaults to doing nothing.
     */
    default void setMatchedTo() {    }

}
