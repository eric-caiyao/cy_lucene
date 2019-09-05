package strore;

import java.io.IOException;

abstract public class OutputStream {
    /**
     * 缓存大小
     */
    private final int BUFFER_SIZE = 1024;
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
    private int fileStartInBuffer = 0;
    /**
     * 文件指针
     */
    private int filePosition;

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

    public void writeVInt(int i) throws IOException{
        
    }

    public void flush() throws IOException {
        flushBuffer();
        fileStartInBuffer = filePosition;
        bufferPosition = 0;
    }

    public abstract void flushBuffer() throws IOException;
}
