package strore;

import java.io.IOException;

abstract public class OutputStream {
    /**
     * 缓存大小
     */
    private final int BUFFER_SIZE = 1;
    /**
     * 写缓存
     */
    protected byte[] buffer = new byte[BUFFER_SIZE];
    /**
     * 写缓存指针
     */
    protected int bufferPosition;
    /**
     * 缓存的文件数据开始位置
     */
    private long fileStartInBuffer = 0;
    /**
     * 文件指针
     */
    private long filePosition;

    public OutputStream(){
        filePosition = 0;
        bufferPosition = 0;
    }

    public void writeByte(byte b) throws IOException {
        if(bufferPosition >= BUFFER_SIZE){
            flush();
        }
        buffer[bufferPosition++] = b;
        filePosition++;
    }

    public void writeLong(long l) throws IOException{
        writeByte((byte) l);

        l = l >>> 8;
        writeByte((byte) l);

        l = l >>> 8;
        writeByte((byte) l);

        l = l >>> 8;
        writeByte((byte) l);

        l = l >>> 8;
        writeByte((byte) l);

        l = l >>> 8;
        writeByte((byte) l);

        l = l >>> 8;
        writeByte((byte) l);

        l = l >>> 8;
        writeByte((byte) l);
    }

    public void writeVLong(long l)throws IOException{
        while((l & ~0x7f) != 0){
            writeByte( (byte)((l & 0x7f) | 0x80) );
        }
        writeByte((byte)l);
    }

    public void writeVInt(int i) throws IOException{
        while((i & ~0x7f) != 0){
            writeByte( (byte)((i & 0x7f) | 0x80) );
        }
        writeByte((byte)i);
    }

    public void writeChars(char[] chars) throws IOException{
        for(int i = 0; i < chars.length; i ++){
            writeByte((byte)chars[i]);
            writeByte((byte)(chars[i] >>> 8));
        }
    }

    public void writeString(String str) throws IOException{
        writeVInt(str.length());
        writeChars(str.toCharArray());
    }

    public void flush() throws IOException {
        flushBuffer();
        fileStartInBuffer = filePosition;
        bufferPosition = 0;
    }

    public long getFilePosition() {
        return filePosition;
    }

    public abstract void flushBuffer() throws IOException;
}
