package strore;

import java.io.IOException;

public interface Directory {

    OutputStream createFile(String fileName) throws IOException;
}
