package index;

import document.Document;
import document.Field;
import strore.Directory;
import strore.InputStream;

import java.io.IOException;

public class FieldsReader {

    FieldNames fieldNames;
    InputStream fdtInputStream;
    InputStream fdxInputStream;

    public FieldsReader(Directory directory, String segmentName,FieldNames fieldNames) throws IOException {
        this.fieldNames = fieldNames;
        fdtInputStream = directory.openFile(segmentName + ".fdt");
        fdxInputStream = directory.openFile(segmentName + ".fdx");
    }

    Document doc(Integer id) throws IOException {
        fdxInputStream.seek(8 * id);
        long fdtPointer = fdxInputStream.readVLong();
        fdtInputStream.seek(fdtPointer);
        int fieldCount = fdtInputStream.readVInt();
        Document document = new Document();
        for(int i = 0; i < fieldCount; i ++){
            int fieldNum = fdtInputStream.readVInt();
            String fieldValue = fdtInputStream.readString();
            document.addField(new Field(fieldNames.name(fieldNum),fieldValue));
        }
        return document;
    }
}
