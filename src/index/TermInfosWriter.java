package index;

import strore.Directory;
import strore.OutputStream;
import utils.StringUtils;

import java.io.IOException;

public class TermInfosWriter {

    OutputStream tisOutputStream;
    OutputStream tiiOutputStream;
    static final int INDEX_INTERVAL = 10;
    int size = 0;
    int indexSize = 0;
    FieldNames fieldNames;
    public TermInfosWriter(Directory directory,String segmentName,FieldNames fieldNames) throws IOException {
        tisOutputStream = directory.createFile(segmentName + ".tis");
        tiiOutputStream = directory.createFile(segmentName + ".tii");
        tisOutputStream.writeVInt(0); // 预留close时候写
        tisOutputStream.writeVInt(INDEX_INTERVAL);
        tiiOutputStream.writeVInt(0); // 预留close时候写
        this.fieldNames = fieldNames;
    }

    Term currentTerm = new Term("","");
    public void addTerm(TermInfo termInfo) throws IOException {
        int sharedPrefixLength = StringUtils.sharePrefixLength(currentTerm.getTermValue(),termInfo.term.getTermValue());
        tisOutputStream.writeVInt(sharedPrefixLength);
        tisOutputStream.writeString(
                termInfo.term.getTermValue().substring(
                        sharedPrefixLength, termInfo.term.getTermValue().length()
                )
        );

        size ++;
    }

    public void close() throws IOException {
        tisOutputStream.seek(0);
        tisOutputStream.writeVInt(size);
        tisOutputStream.close();
        tiiOutputStream.seek(0);
        tiiOutputStream.writeVInt(indexSize);
        tiiOutputStream.close();
    }
}
