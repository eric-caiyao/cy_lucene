package index;

import java.util.List;

public class TermDocs {
    private Term term;
    private List<Long> proxs;
    public TermDocs(Term term, List<Long> proxs){
        this.term = term;
        this.proxs = proxs;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public List<Long> getProxs() {
        return proxs;
    }

    public void setProxs(List<Long> proxs) {
        this.proxs = proxs;
    }
}
