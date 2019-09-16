package strore;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FSInputStream extends InputStream{
    RandomAccessFile inputStream;

    public FSInputStream(File file) throws IOException {
        this.inputStream = new RandomAccessFile(file,"r");
    }

    public byte readByte() throws IOException {
        byte tmpByte = (byte)inputStream.read();
        return tmpByte;
    }

    @Override
    public void seek(long streamPointer) throws IOException {
        inputStream.seek(streamPointer);
    }
}
