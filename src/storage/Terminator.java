package storage;

public class Terminator {

    private I_Terminate terminate;

    private boolean isRepeated;

    private boolean isLazy = false;



    public Terminator(I_Terminate terminate, boolean isRepeated, boolean isLazy) {
        this.terminate = terminate;
        this.isRepeated = isRepeated;
        this.isLazy = isLazy;
    }



    public SearchResult match(String input, int startIndex) {

    }



    public I_Terminate getTerminate() {
        return terminate;
    }

    public void setTerminate(I_Terminate terminate) {
        this.terminate = terminate;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

}
