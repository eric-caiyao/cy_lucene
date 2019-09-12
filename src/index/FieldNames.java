package index;

import strore.Directory;
import strore.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldNames {

    private Map<String,Integer> nameToNoMap = new HashMap<>();

    OutputStream outputStream;

    public FieldNames(Directory directory,String segmentName) throws IOException {
        /**
         * todo: 从文件中将属性信息加载到nameToNoMap
         */
        outputStream = directory.createFile(segmentName);
    }

    public void write(){
        /**
         * 将nameToNoMap中的数据写入到outputStream指定的数据中
         */
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
}
