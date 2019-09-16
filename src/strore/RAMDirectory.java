package strore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RAMDirectory implements Directory{

    private Map<String,List<Byte>> files = new HashMap<>();

    @Override
    public OutputStream createFile(String fileName) throws IOException {
        files.put(fileName,new ArrayList<>());
        return new RAMOutputStream(files.get(fileName));
    }

    @Override
    public InputStream openFile(String fileName) throws IOException {
        /**
         * todo:
         */
        return null;
    }
}
