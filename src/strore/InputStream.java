package strore;

import java.io.IOException;

abstract public class InputStream {

    abstract public byte readByte() throws IOException;

    public int readVInt() throws IOException{
        int i = 0;
        byte b;
        b = readByte();
        i = i | b;
        if((b & ~0x80) == 0){
            return i;
        }
        do{
            b = readByte();
            i = i | (b << 7);
        }while ((b & ~0x80) != 0);
        return i;
    }
}
