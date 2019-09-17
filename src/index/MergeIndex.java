package index;

public class MergeIndex {
    private TermEnum termEnum;
    private int docNoBase;

    public MergeIndex(TermEnum termEnum, int docNoBase) {
        this.termEnum = termEnum;
        this.docNoBase = docNoBase;
    }

    public TermEnum getTermEnum() {
        return termEnum;
    }

    public int getDocNoBase() {
        return docNoBase;
    }
}
