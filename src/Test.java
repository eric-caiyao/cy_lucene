import document.Document;
import document.Field;
import index.IndexWriter;

import java.io.IOException;

/**
 *
 */
public class Test {
    public static void main(String[] args) throws IOException {
        IndexWriter indexWriter = new IndexWriter("/Users/caiyao/Desktop/test_index");
        Document d1 = new Document();
        d1.addField(new Field("content","a b c d e"));
        indexWriter.addDocument(d1);
    }
}
