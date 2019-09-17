package index;

public class TermInfo {
    Term term;
    int docCount;
    long frqPointer;
    long proxPointer;

    public TermInfo(Term term, int docCount, long frqPointer, long proxPointer) {
        this.term = term;
        this.docCount = docCount;
        this.frqPointer = frqPointer;
        this.proxPointer = proxPointer;
    }
}
