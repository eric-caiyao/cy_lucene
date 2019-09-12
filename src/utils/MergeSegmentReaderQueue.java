package utils;

import index.Segment;

public class MergeSegmentReaderQueue extends PriorityQueue<Segment>{

    public MergeSegmentReaderQueue(int size) {
        super(size);
    }

    @Override
    public boolean lessThan(Segment left, Segment right) {
        return false;
    }
}
