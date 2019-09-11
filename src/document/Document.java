package document;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Document {

    private List<Field> fields = new ArrayList<Field>();

    public Document addField(Field field){
        fields.add(field);
        return this;
    }

    public List<Field> fields(){
        return fields;
    }
}
