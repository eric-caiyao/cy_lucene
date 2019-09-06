package index;

import document.Document;
import strore.Directory;
import strore.FSDirectory;

import java.io.File;
import java.io.IOException;

public class IndexWriter {
    private Directory directory;
    public IndexWriter(String path) throws IOException {
        File indexDirectory = new File(path);
        if(!indexDirectory.exists()){
            indexDirectory.createNewFile();
        }
        directory = new FSDirectory(indexDirectory);
    }
    public void addDocument(Document document) throws IOException {
        /**
         * 1. 写属性名称文件
         * 2. 写文档原始数据
         */
        new FieldNameWriter(directory.createFile("index.fnm")).addDocument(document);
    }
}
