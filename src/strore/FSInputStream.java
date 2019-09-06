package strore;

import java.io.FileInputStream;
import java.io.IOException;

public class FSInputStream extends InputStream{
    FileInputStream inputStream;
    public FSInputStream(FileInputStream inputStream){
        this.inputStream = inputStream;
    }
    public byte readByte() throws IOException {
        return (byte)inputStream.read();
    }
}
