package document;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Document {

    List<Field> fields = new ArrayList<Field>();

    public Document addField(Field field){
        fields.add(field);
        return this;
    }
}
