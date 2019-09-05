package strore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FSOutputStream extends OutputStream{

    private FileOutputStream fileOutputStream;

    public FSOutputStream(File file) throws FileNotFoundException {
        fileOutputStream = new FileOutputStream(file);
    }

    public void flushBuffer() throws IOException {
        fileOutputStream.write(buffer,0,bufferPosition);
    }
}
