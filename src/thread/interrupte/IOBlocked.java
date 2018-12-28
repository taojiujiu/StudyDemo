package thread.interrupte;

import java.io.IOException;
import java.io.InputStream;

public class  IOBlocked implements Runnable{
    private InputStream inputStream ;

    public IOBlocked(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for read();");
            inputStream.read();
        } catch (IOException e) {
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Interrupted from blocked I/O");
            }else {
                throw  new RuntimeException();
//                System.out.println(e);
            }
        }
        System.out.println("Exiting IOBlocked.run()");
    }
}