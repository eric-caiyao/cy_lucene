import strore.FSOutputStream;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class Test {
    public static void main(String[] args) throws IOException {

        final FSOutputStream outputStream = new FSOutputStream(new File("/Users/caiyao/Desktop/test"));
        outputStream.writeString("hello");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    outputStream.flushBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
