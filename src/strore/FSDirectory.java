package strore;

import java.io.File;
import java.io.IOException;

public class FSDirectory implements Directory{

    private File directory;

    public FSDirectory(File directory){
        this.directory = directory;
    }

    public OutputStream createFile(String fileName) throws IOException {
        return new FSOutputStream(new File(directory,fileName));
    }

    @Override
    public InputStream openFile(String fileName) throws IOException {
        return new FSInputStream(new File(directory,fileName));
    }
}
