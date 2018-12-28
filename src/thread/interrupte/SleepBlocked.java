package thread.interrupte;

import java.util.concurrent.TimeUnit;

public class SleepBlocked  implements Runnable{

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException  " + Thread.currentThread().isInterrupted());
        }
        System.out.println("Exiting SleepBlocked.run()");

    }
}