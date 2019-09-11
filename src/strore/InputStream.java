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

    public char readChar() throws IOException{
        char c;
        c = (char)readByte();
        c = (char)(readByte() << 8 | c);
        return c;
    }

    public String readString() throws IOException{
        int length = readVInt();
        char[] charArray = new char[length];
        for(int i = 0; i < length; i ++){
            charArray[i] = readChar();
        }
        return new String(charArray);
    }
}
