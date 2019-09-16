package utils;

import index.TermEnum;

public class MergeIndexQueue extends PriorityQueue<TermEnum> {

    public MergeIndexQueue(int size) {
        super(size);
    }

    @Override
    public boolean lessThan(TermEnum left, TermEnum right) {
        if(left.getTerm().getFieldName().equalsIgnoreCase(right.getTerm().getFieldName())){
            return left.getTerm().getTermValue().compareTo(right.getTerm().getTermValue()) < 0;
        }else{
            return left.getTerm().getFieldName().compareTo(right.getTerm().getFieldName()) < 0;
        }
    }
}
