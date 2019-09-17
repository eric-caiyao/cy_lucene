package index;

import java.util.List;

public class TermPositions {
    Term term;
    List<Long> positions;

    public TermPositions(Term term, List<Long> positions) {
        this.term = term;
        this.positions = positions;
    }

    public Term getTerm() {
        return term;
    }

    public List<Long> getPositions() {
        return positions;
    }
}
