package document;

import java.io.Reader;

/**
 *
 */
public class Field {
    private String name;
    private String stringValue;
    private Reader reader;

    public Field(String name, String stringValue){
        this.name = name;
        this.stringValue = stringValue;
    }

    public Field(String name, Reader reader){
        this.name = name;
        this.reader = reader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
