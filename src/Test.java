import document.Document;
import document.Field;
import index.IndexWriter;
import index.TermEnum;
import strore.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        IndexWriter indexWriter = new IndexWriter("/Users/caiyao/Desktop/test_index");
//        Document d1 = new Document();
//        d1.addField(new Field("content","a b a d e"));
//        d1.addField(new Field("content2","b a c a d"));
//        indexWriter.addDocument(d1);
        TermEnum termEnum = new TermEnum(new FSDirectory(new File("/Users/caiyao/Desktop/test_index")),"_1");
        while(termEnum.next()){
            System.out.println(termEnum.getTerm().getFieldName() + ":" + termEnum.getTerm().getTermValue());
            for(Map.Entry<Integer, List<Long>> entry : termEnum.getDocFreqsMap().entrySet()){
                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            }
            System.out.println("---------------------");
        }
    }
}
