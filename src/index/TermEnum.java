package index;

import strore.Directory;
import strore.InputStream;

import java.io.IOException;
import java.util.*;

public class TermEnum {
    private Term term = new Term("","");
    private TreeMap<Integer,List<Long>> docFreqsMap = new TreeMap<>();
    private InputStream termInfosInputStream;
    private Map<Integer,String> fieldNoToNameMap = new HashMap<>();
    private int termCount;
    private int currentIndex = 0;
    private InputStream frqInputStream;
    private InputStream proxInputStream;
    public TermEnum(Directory directory, String segmentName) throws IOException {
        InputStream fnmInputStream = directory.openFile(segmentName + ".fnm");
        int fieldNameCount = fnmInputStream.readVInt();
        for(int i = 0; i < fieldNameCount; i ++){
            fieldNoToNameMap.put(i,fnmInputStream.readString());
        }
        termInfosInputStream = directory.openFile(segmentName + ".tis");
        termCount = termInfosInputStream.readVInt(); // term count
        int indexInterval = termInfosInputStream.readVInt(); // index interval

        frqInputStream = directory.openFile(segmentName + ".frq");
        proxInputStream = directory.openFile(segmentName + ".prox");
    }

    public boolean next() throws IOException {
        if(currentIndex >= termCount){
            return false;
        }
        read();
        currentIndex ++;
        return true;
    }

    public void read() throws IOException{
        int sharedPrefixLength = termInfosInputStream.readVInt(); // 共享前缀长度
        String suffix = termInfosInputStream.readString(); // 后缀
        int fieldNo = termInfosInputStream.readVInt(); // 属性编号
        int docs = termInfosInputStream.readVInt(); // 文档数
        term = new Term(fieldNoToNameMap.get(fieldNo),term.getTermValue().substring(0,sharedPrefixLength) + suffix);
        int frqPointer = termInfosInputStream.readVInt(); // 频率数据位置
        int proxPointer = termInfosInputStream.readVInt(); // 位置文件位置
        frqInputStream.seek(frqPointer);
        for(int i = 0; i < docs; i ++){
            int docFreq = frqInputStream.readVInt();
            int doc = docFreq >>> 1;
            docFreqsMap.put(doc,new ArrayList<>());
            int freq;
            if((docFreq & 1) == 1){
                freq = 1;
            }else{
                freq = frqInputStream.readVInt();
            }

            proxInputStream.seek(proxPointer);
            for(int j = 0; j < freq; j ++){
                long tmpProx = proxInputStream.readVLong();
                docFreqsMap.get(doc).add(tmpProx);
            }
        }
    }

    public Term getTerm() {
        return term;
    }

    public TreeMap<Integer, List<Long>> getDocFreqsMap() {
        return docFreqsMap;
    }

    public static void main(String[] args){
        // TermEnum termEnum = new TermEnum(new FSDirectory(""));
    }
}
