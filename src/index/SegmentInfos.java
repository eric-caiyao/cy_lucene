package index;

import strore.Directory;

import java.util.ArrayList;
import java.util.List;

public class SegmentInfos {

    private List<Segment> segmentList = new ArrayList<>();

    public void read(Directory directory){

    }

    public int getSegmentCount() {
        return segmentList.size();
    }

    public void add(Segment segment){
        segmentList.add(segment);
    }

    public List<Segment> getSegmentList(){
        return segmentList;
    }
}
