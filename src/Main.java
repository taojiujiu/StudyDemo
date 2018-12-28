import java.util.concurrent.atomic.AtomicInteger;

public class Main implements Runnable {
    volatile private  AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        Main main = new Main();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(main);
            thread.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main result: " + main.count);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            count.addAndGet(1);
            Thread.yield();
        }
        System.out.println("result: " + count);

    }

}
