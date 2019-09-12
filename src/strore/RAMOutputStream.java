package strore;

import java.io.IOException;
import java.util.List;

public class RAMOutputStream extends OutputStream{

    private List<Byte> fileContent;

    public RAMOutputStream(List<Byte> fileContent){
        this.fileContent = fileContent;
    }

    @Override
    public void flushBuffer() throws IOException {
        for(int i = 0; i < bufferPosition; i ++){
            fileContent.add(buffer[i]);
        }
    }
}
