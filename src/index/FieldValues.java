package index;

import document.Document;
import strore.Directory;

import java.io.InputStream;
import java.io.OutputStream;

public class FieldValues {

    private InputStream fdxInputStream;
    private InputStream fdtInputStream;
    private OutputStream fdxOutputStream;
    private OutputStream fdtOutputStream;

    public FieldValues(Directory directory, String segmentName){

    }

    public Document doucument(int docId){
        /**
         * todo:
         */
        return null;
    }

    public void addDocument(Document document){
        /**
         * 要根据Document里的属性名获取属性编号
         */
        /**
         * todo:
         */
    }

    public int doucumentNum(){
        /**
         * todo:
         */
        return 0;
    }
}
