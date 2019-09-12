package index;

import strore.Directory;

public class Segment {
    private String segmentName;
    private Integer docCount;
    private Directory directory;
    public Segment(){}

    public Segment(String segmentName, Integer docCount,Directory directory){
        this.segmentName = segmentName;
        this.docCount = docCount;
        this.directory = directory;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }
}
