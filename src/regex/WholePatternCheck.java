package regex;

import storage.*;

/**
 * A non-recursive whole string matcher. A limited implementation of regex.
 */
public class WholePatternCheck {

    private final DequeMap<I_Chunk, RepeatableHistory> repeatablesTracking = new DequeMap<>();

    private final PatternChunks patternChunks;

    private final String input;

    private int inputIndex = 0;

    private int chunkIndex = 0;



    public WholePatternCheck(PatternChunks patternChunks, String input) {
        this.patternChunks = patternChunks;
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
        Chunk chunk = patternChunks.get(chunkIndex);

        // Note(Max): Here is where I would probably put the branch for a search with a greedy group.
        return lazySearchChunk(chunk);
    }

    private E_State lazySearchChunk(I_Chunk chunk) {
        if (newRepeatChunk(chunk)) {
            return check0Repeat();
        }

        SearchResult searchResult = chunk.matchChunk(inputIndex, input);

        if (!searchResult.found()) {
            return chunkNotFound(chunk);
        }

        return chunkMatchFound(chunk, searchResult);
    }

    private boolean newRepeatChunk(I_Chunk chunk) {
        if (chunk.isRepeatable()) {
            RepeatableHistory repeatableHistory = repeatablesTracking.get(chunk);

            // Note(Max): If this is the "first time this group has been seen" staredHistory is null so add it to
            // the tracking. As its stared we can start with a zero length search, aka just increment chunkIndex check
            // the state of the search.
            if (repeatableHistory == null) {
                repeatablesTracking.putFirst(chunk, new RepeatableHistory(chunkIndex, inputIndex));
                return true;
            }
        }
        return false;
    }

    private E_State check0Repeat() {
        chunkIndex++;

        if (chunkIndex < patternChunks.size()) {
            return E_State.SEARCHING;
        }
        // chunkIndex == groups.size()

        // This is the case where we are done and happy, all groups matched to the full string.
        if (inputIndex == input.length()) {
            return E_State.SUCCESS;
        }

        // This is the case where the pattern has been matched, but it isn't the full string.
        chunkIndex--;
        return E_State.SEARCHING;
    }

    private E_State chunkMatchFound(I_Chunk chunk, SearchResult searchResult) {
        inputIndex = searchResult.firstFreeChar();

        if (chunk.isRepeatable()) {
            repeatablesTracking.putFirst(chunk, new RepeatableHistory(chunkIndex, inputIndex));
        }

        chunkIndex++;

        if (chunkIndex < patternChunks.size()) {
            return E_State.SEARCHING;
        }
        // chunkIndex == groups.size()

        // This is the case where we are done and happy, all groups matched to the full string.
        if (inputIndex == input.length()) {
            return E_State.SUCCESS;
        }

        // This is the case where the pattern has been matched but it isn't the full string.
        if (chunk.isRepeatable()) {
            chunkIndex--;
            return E_State.SEARCHING;
        }

        // All the groups have been used, but we can't get any further. So we have to try and rollback. This also
        // means that the last group is not stared. (If it was we could try the next char(s).)
        if (repeatablesTracking.isEmpty()) {
            return E_State.FAILURE;
        }

        return E_State.REQUEST_UNWIND;
    }

    private E_State chunkNotFound(I_Chunk chunk) {
        // At the start of this function tracking is only empty if we haven't seen a repeatable group yet.

        if (chunk.isRepeatable()) {
            // We have gone too far with this current match.
            repeatablesTracking.removeFirst();
        }

        // If tracking is empty then we cannot unwind. This makes the layout of the match up to this point "rigid" and
        // therefore the match has failed.
        if (repeatablesTracking.isEmpty()) {
            return E_State.FAILURE;
        }

        return E_State.REQUEST_UNWIND;
    }

    private void unwind() {
        RepeatableHistory rollbackPoint = repeatablesTracking.getFirst();
        chunkIndex = rollbackPoint.chunkIndex();
        inputIndex = rollbackPoint.inputIndex();
    }

}
