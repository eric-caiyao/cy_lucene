package index;

import document.Document;
import document.Field;
import strore.Directory;
import strore.FSDirectory;
import strore.OutputStream;
import utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IndexWriter {
    public static final int INDEX_INTERVAL = 10;
    private Directory directory;
    public IndexWriter(String path) throws IOException {
        File indexDirectory = new File(path);
        if(!indexDirectory.exists()){
            indexDirectory.createNewFile();
        }
        directory = new FSDirectory(indexDirectory);
    }

    public void addDocument(Document doc) throws IOException {
        /*
         * 写属性名称
         */
        List<Field> fields = doc.fields();
        Map<String,Integer> nameToId = new HashMap<>();
        for(int i = 0; i < fields.size(); i ++){
            nameToId.put(fields.get(i).getName(),i);
        }
        OutputStream fnmStream = directory.createFile("index.fnm");
        fnmStream.writeVInt(nameToId.size());
        for(Field field : fields){
            fnmStream.writeString(field.getName());
        }
        /*
         * 写文档数据
         */
        OutputStream fdtStream = directory.createFile("index.fdt");
        OutputStream fdxStream = directory.createFile("index.fdx");
        fdxStream.writeLong(fdtStream.getFilePosition());
        fdtStream.writeVInt(fields.size());
        for(Field field : fields){
            fdtStream.writeVInt(nameToId.get(field.getName()));
            fdtStream.writeString(field.getStringValue());
        }
        /*
        写词元字典
         */
        SortedMap<Term,List<Integer>> termDocMap = new TreeMap<>(
                (o1, o2) -> {
                    if (!o1.getFieldName().equalsIgnoreCase(o2.getFieldName())) {
                        return o1.getFieldName().compareTo(o2.getFieldName());
                    }
                    return o1.getTermValue().compareTo(o2.getTermValue());
                }
        );
        for(Field field : fields){
            SimpleTokenizer tokenizer = new SimpleTokenizer(field.getStringValue());
            while(tokenizer.hasNext()){
                Token tmpToken = tokenizer.nextToken();
                Term tmpTerm = new Term(field.getName(),tmpToken.getValue());
                if(termDocMap.containsKey(tmpTerm)){
                    termDocMap.get(tmpTerm).add(tmpToken.getPosition());
                }else{
                    termDocMap.put(tmpTerm,Arrays.asList(tmpToken.getPosition()));
                }
            }
        }
        OutputStream tisStream = directory.createFile("index.tis");
        OutputStream tiiStream = directory.createFile("index.tii");
        OutputStream freqStream = directory.createFile("index.frq");
        OutputStream proxStream = directory.createFile("index.prox");
        tisStream.writeVInt(termDocMap.size());// term size
        tisStream.writeVInt(INDEX_INTERVAL); // indexInterval
        tiiStream.writeVInt(termDocMap.size() / INDEX_INTERVAL); // index term size
        String tmpTermValue = "";
        String tmpTiiTermValue = "";
        int index = 0;
        for(Map.Entry<Term, List<Integer>> termDoc : termDocMap.entrySet()){ // prefixLength,termValueSuffix,fieldNum
            Term term = termDoc.getKey();
            if(index ++ % INDEX_INTERVAL == 0){ // 写索引文件
                int sharePrefixLength = StringUtils.sharePrefixLength(tmpTiiTermValue, term.getTermValue());
                tiiStream.writeVInt(sharePrefixLength); // 共享前缀长度
                tiiStream.writeString(term.getTermValue().substring(sharePrefixLength, term.getTermValue().length() - 1)); // 后缀
                tiiStream.writeVInt(nameToId.get(term.getFieldName())); // 属性编号
                tiiStream.writeVLong(tisStream.getFilePosition()); // 该term在词元文件中的位置
                tiiStream.writeVLong(freqStream.getFilePosition()); // 频率文件位置
                tiiStream.writeLong(proxStream.getFilePosition()); // 位置文件位置
                tmpTiiTermValue = term.getTermValue();
            }
            int sharePrefixLength = StringUtils.sharePrefixLength(tmpTermValue,term.getTermValue());
            tisStream.writeVInt(sharePrefixLength); // 共享前缀长度
            tisStream.writeString(term.getTermValue().substring(sharePrefixLength,term.getTermValue().length() - 1)); // 后缀
            tisStream.writeVInt(nameToId.get(term.getFieldName()));// 属性编号
            tisStream.writeVLong(freqStream.getFilePosition()); // 频率文件位置
            tisStream.writeLong(proxStream.getFilePosition()); // 位置文件位置
            tmpTermValue = term.getTermValue();

            // 写频率数据
            if(termDoc.getValue().size() == 1){
                freqStream.writeVInt(1);
            }else{
                freqStream.writeVInt(0);
                freqStream.writeVInt(termDoc.getValue().size());
            }
            // 写位置数据
            for(Integer prox : termDoc.getValue()){
                proxStream.writeVInt(prox);
            }
        }
    }
}
