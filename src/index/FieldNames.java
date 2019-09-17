package index;

import document.Document;
import document.Field;
import strore.Directory;
import strore.InputStream;
import strore.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldNames {

    private Map<String,Integer> nameToNoMap = new HashMap<>();

    public FieldNames(){

    }

    public FieldNames(Directory directory,String segmentName) throws IOException {
        InputStream inputStream = directory.openFile(segmentName + ".fnm");
        int fieldCount = inputStream.readVInt(); // field count
        for(int i = 0; i < fieldCount; i ++){
            addFieldName(inputStream.readString());
        }
    }

    public void write(Directory directory, String segmentName) throws IOException {
        OutputStream outputStream = directory.createFile(segmentName + ".fnm");
        outputStream.writeVInt(nameToNoMap.size());
        for(Map.Entry<String, Integer> field : nameToNoMap.entrySet()){
            outputStream.writeString(field.getKey());
        }
        outputStream.close();
    }

    public void addFieldName(String fieldName){
        if(!nameToNoMap.containsKey(fieldName)){
            int size = nameToNoMap.size();
            nameToNoMap.put(fieldName,size);
        }
    }

    public List<String> fieldNames(){
        return new ArrayList<>(nameToNoMap.keySet());
    }

    public void addFieldNames(List<String> fieldNames){
        for(String fieldName : fieldNames){
            addFieldName(fieldName);
        }
    }

    public Integer id(String name){
        return nameToNoMap.get(name);
    }

    public String name(int fieldNum) {
        Map<Integer,String> inverseMap = new HashMap<>();
        for(Map.Entry<String, Integer> entry : nameToNoMap.entrySet()){
            inverseMap.put(entry.getValue(),entry.getKey());
        }
        return inverseMap.get(fieldNum);
    }

    public void addDocument(Document document){
        for(Field field : document.fields()){
            addFieldName(field.getName());
        }
    }
}
