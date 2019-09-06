package document;

import java.io.Reader;

/**
 *
 */
public class Field {
    String name;
    String stringValue;
    Reader reader;

    public Field(String name, String stringValue){
        this.name = name;
        this.stringValue = stringValue;
    }

    public Field(String name, Reader reader){
        this.name = name;
        this.reader = reader;
    }
}
