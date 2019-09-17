package index;

import document.Document;
import document.Field;
import strore.Directory;
import strore.FSDirectory;
import strore.OutputStream;
import strore.RAMDirectory;
import utils.MergeIndexQueue;
import utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IndexWriter {
    private Directory directory;
    private Directory ramDirectory = new RAMDirectory();
    private SegmentInfos segmentInfos = new SegmentInfos();
    public IndexWriter(String path) throws IOException {
        File indexDirectory = new File(path);
        if(!indexDirectory.exists()){
            indexDirectory.createNewFile();
        }
        directory = new FSDirectory(indexDirectory);
        segmentInfos.read(directory);
    }

    private String newSegmentName(){
        return "_" + (segmentInfos.getSegmentCount() + 1);
    }
    public void addDocument(Document doc) throws IOException {
        String segmentName = newSegmentName();
        /*
         * 写属性名称
         */
        FieldNames newFieldNames = new FieldNames();
        newFieldNames.addDocument(doc);
        newFieldNames.write(ramDirectory,segmentName);

        /*
         * 写文档数据
         */
        FieldsWriter fieldsWriter = new FieldsWriter(ramDirectory,segmentName,newFieldNames);
        fieldsWriter.addDocument(doc);
        /*
        * 写词元字典
         */
        List<TermPositions> termPositions = termPositions(doc);
        TermInfosWriter termInfosWriter = new TermInfosWriter(directory,segmentName,newFieldNames);
        OutputStream frqOutputStream = directory.createFile(segmentName + ".frq");
        OutputStream proxOutputStream = directory.createFile(segmentName + ".prox");

        for(TermPositions item : termPositions){
            TermInfo termInfo = new TermInfo(
                    item.getTerm(),
                    1,
                    frqOutputStream.getFilePosition(),
                    proxOutputStream.getFilePosition()
            );
            termInfosWriter.addTerm(termInfo);

            // 写频率数据
            if(item.getPositions().size() == 1){
                frqOutputStream.writeVInt(1);
            }else{
                frqOutputStream.writeVInt(0);
                frqOutputStream.writeVInt(item.getPositions().size());
            }
            // 写位置数据
            for(Long prox : item.getPositions()){
                proxOutputStream.writeVLong(prox);
            }
        }
        segmentInfos.add(new Segment(segmentName,1,directory));
        maybeMergeSegments();
    }

    FieldNames mergeFieldNames(int minSegment,String segmentName) throws IOException{
        FieldNames newFieldNames = new FieldNames();
        while (minSegment < segmentInfos.getSegmentList().size()) {
            Segment tmpSegment = segmentInfos.getSegmentList().get(minSegment);
            FieldNames tmpFieldNames = new FieldNames(
                    tmpSegment.getDirectory(),
                    tmpSegment.getSegmentName()
            );
            newFieldNames.addFieldNames(tmpFieldNames.fieldNames());
            minSegment ++;
        }
        newFieldNames.write(directory,segmentName);
        return newFieldNames;
    }

    void mergeFieldValues(int minSegment,String segmentName,FieldNames fieldNames) throws IOException {
        FieldsWriter newFieldsWriter = new FieldsWriter(directory,segmentName,fieldNames);
        while(minSegment ++ <= segmentInfos.getSegmentList().size()){
            Segment tmpSegment = segmentInfos.getSegmentList().get(minSegment);
            FieldsReader fieldsReader = new FieldsReader(directory,tmpSegment.getSegmentName(),fieldNames);
            for(int i = 0;i < tmpSegment.getDocCount(); i ++){
                newFieldsWriter.addDocument(fieldsReader.doc(i));
            }
        }
    }
    void mergeSegments(int minSegment) throws IOException {
        int mergeMinSegment = minSegment + 1;
        String newSegmentName = "_" + (segmentInfos.getSegmentCount());
        //  合并属性
        FieldNames fieldNames = mergeFieldNames(mergeMinSegment,newSegmentName);
        //  合并数据
        mergeFieldValues(minSegment,newSegmentName,fieldNames);
        //  合并词元
        /**
         * todo: tis文件中没有写总词元个数，必须将词元全部计算出来以后才能写文件
         */
        frqOutputStream = directory.createFile(newSegmentName + ".frq");
        proxOutputStream = directory.createFile(newSegmentName + ".prx");
        termInfosOutputStream = directory.createFile(newSegmentName + ".tis");
        MergeIndexQueue mergeIndexQueue = new MergeIndexQueue(segmentInfos.getSegmentCount() - mergeMinSegment);
        int base = 0;
        for(int i = mergeMinSegment; i < segmentInfos.getSegmentCount(); i ++){ // 初始化队列
            mergeIndexQueue.push(new MergeIndex(new TermEnum(directory,"_" + i),base));
            base = base + segmentInfos.getSegmentList().get(i).getDocCount();
        }
        while(mergeIndexQueue.size() > 0){
            List<MergeIndex> mergingIndexs = new ArrayList<>();
            mergingIndexs.add(mergeIndexQueue.pop());
            while(mergeIndexQueue.top().equals(mergingIndexs.get(0))){
                mergingIndexs.add(mergeIndexQueue.pop());
            }

            mergeTerms(mergingIndexs,fieldNames);

            for(MergeIndex mergingIndex : mergingIndexs){
                if(mergingIndex.getTermEnum().next()){
                    mergeIndexQueue.push(mergingIndex);
                }
            }
        }
    }

    OutputStream frqOutputStream;
    OutputStream proxOutputStream;
    OutputStream termInfosOutputStream;
    public void maybeMergeSegments() throws IOException {
        int minMergeDocs = 10;
        int maxMergeDocs = 10000;
        int currentMergeDocs = minMergeDocs;
        while(currentMergeDocs <= maxMergeDocs){ // 找到需要合并的段列表
            int minSegment = segmentInfos.getSegmentCount() - 1;
            int mergeDocs = 0;
            while(minSegment >= 0){
                Segment segment = segmentInfos.getSegmentList().get(minSegment);
                if(segment.getDocCount() >= currentMergeDocs){
                    break;
                }
                mergeDocs += segment.getDocCount();
                minSegment --;
            }
            if(mergeDocs >= currentMergeDocs){
                mergeSegments(minSegment);
            }else {
                break;
            }
            currentMergeDocs *= 10;
        }
    }

    Term currentMergeTerm = new Term("","");
    private void mergeTerms(List<MergeIndex> mergingIndexs,FieldNames newFieldNames) throws IOException {
        long frqPointer = frqOutputStream.getFilePosition();
        long prxPointer = proxOutputStream.getFilePosition();

        int docCount = mergeTerm(mergingIndexs);

        Term term = mergingIndexs.get(0).getTermEnum().getTerm();
        int sharePrefixLength = StringUtils.sharePrefixLength(
                currentMergeTerm.getTermValue(),
                term.getTermValue()
        );
        termInfosOutputStream.writeVInt(sharePrefixLength); // 共享前缀长度
        termInfosOutputStream.writeString(term.getTermValue().substring(sharePrefixLength,term.getTermValue().length())); // 后缀
        termInfosOutputStream.writeVInt(newFieldNames.id(term.getFieldName()));// 属性编号
        termInfosOutputStream.writeVInt(docCount); // 文档数
        termInfosOutputStream.writeVInt((int)frqPointer); // 频率文件位置
        termInfosOutputStream.writeVInt((int)prxPointer); // 位置文件位置
    }

    private int mergeTerm(List<MergeIndex> mergingIndexs) throws IOException {
        int docCount = 0;
        for(MergeIndex mergeIndex : mergingIndexs){
            TreeMap<Integer, List<Long>> docFreqMap = mergeIndex.getTermEnum().getDocFreqsMap();
            docCount += docFreqMap.size();
            for(Map.Entry<Integer, List<Long>> entry : docFreqMap.entrySet()){
                int docNo = entry.getKey() + mergeIndex.getDocNoBase();
                int freq = entry.getValue().size();
                if(freq == 1){
                    frqOutputStream.writeVInt(docNo | freq);
                }else{
                    frqOutputStream.writeVInt(docNo);
                    frqOutputStream.writeVInt(freq);
                }
                for(Long prox : entry.getValue()){
                    proxOutputStream.writeVLong(prox);
                }
            }
        }
        return docCount;
    }

    List<TermPositions> termPositions(Document doc){
        SortedMap<Term,List<Long>> termDocMap = new TreeMap<>(
                (left, right) -> {
                    if (!left.getFieldName().equalsIgnoreCase(right.getFieldName())) {
                        return left.getFieldName().compareTo(right.getFieldName());
                    }
                    return left.getTermValue().compareTo(right.getTermValue());
                }
        );
        for(Field field : doc.fields()){
            SimpleTokenizer tokenizer = new SimpleTokenizer(field.getStringValue());
            while(tokenizer.hasNext()){
                Token tmpToken = tokenizer.nextToken();
                Term tmpTerm = new Term(field.getName(),tmpToken.getValue());
                if(!termDocMap.containsKey(tmpTerm)){
                    termDocMap.put(tmpTerm,new ArrayList<>());
                }
                termDocMap.get(tmpTerm).add(Long.valueOf(tmpToken.getPosition()));
            }
        }
        List<TermPositions> termPositionsList = new ArrayList<>();
        for(Map.Entry<Term, List<Long>> entry : termDocMap.entrySet()){
            termPositionsList.add(new TermPositions(entry.getKey(),entry.getValue()));
        }
        return termPositionsList;
    }
}
