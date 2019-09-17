package index;

import document.Document;
import document.Field;
import strore.Directory;
import strore.OutputStream;

import java.io.IOException;

public class FieldsWriter {

    FieldNames fieldNames;
    OutputStream fdtOutputStream;
    OutputStream fdxOutputStream;

    public FieldsWriter(Directory directory,String segmentName,FieldNames fieldNames) throws IOException {
        this.fieldNames = fieldNames;
        fdtOutputStream = directory.createFile(segmentName + ".fdt");
        fdxOutputStream = directory.createFile(segmentName + ".fdx");
    }

    public void addDocument(Document document) throws IOException {
        fdxOutputStream.writeVLong(fdtOutputStream.getFilePosition());
        fdtOutputStream.writeVInt(document.fields().size());
        for(Field field : document.fields()){
            fdtOutputStream.writeVInt(fieldNames.id(field.getName()));
            fdtOutputStream.writeString(field.getStringValue());
        }
    }

    public void close() throws IOException {
        fdtOutputStream.close();
        fdxOutputStream.close();
    }
}
