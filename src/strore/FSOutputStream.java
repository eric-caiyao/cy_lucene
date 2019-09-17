package strore;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FSOutputStream extends OutputStream{

    private RandomAccessFile fileOutputStream;

    public FSOutputStream(File file) throws IOException {
        fileOutputStream = new RandomAccessFile(file,"w");
    }

    public void flushBuffer() throws IOException {
        fileOutputStream.write(buffer,0,bufferPosition);
    }

    @Override
    public void close() throws IOException {
        fileOutputStream.close();
    }

    @Override
    public void seek(long position) throws IOException {
        fileOutputStream.seek(position);
    }
}
