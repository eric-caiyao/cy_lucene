package utils;

import index.MergeIndex;

public class MergeIndexQueue extends PriorityQueue<MergeIndex> {

    public MergeIndexQueue(int size) {
        super(size);
    }

    @Override
    public boolean lessThan(MergeIndex left, MergeIndex right) {
        if(left.getTermEnum().getTerm().getFieldName().equalsIgnoreCase(right.getTermEnum().getTerm().getFieldName())){
            return left.getTermEnum().getTerm().getTermValue().compareTo(right.getTermEnum().getTerm().getTermValue()) < 0;
        }else{
            return left.getTermEnum().getTerm().getFieldName().compareTo(right.getTermEnum().getTerm().getFieldName()) < 0;
        }
    }
}
