package thread.interrupte;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class InterruptStudy {


        private static ExecutorService executorService = Executors.newCachedThreadPool();
        static void test(Runnable r) throws InterruptedException {
            Future<?> f = executorService.submit(r);
            TimeUnit.MICROSECONDS.sleep(100);
            System.out.println("Interrupting " + r.getClass().getName());
            f.cancel(true);
            System.out.println("Interrupt sent to "+ r.getClass().getName());
        }

        public static void main(String[] arg) throws InterruptedException {
            test(new SleepBlocked());
            test(new IOBlocked(System.in));
            test(new SynchroinzedBlocked());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("Aborting with System.exit(0)");
            System.exit(0);
        }



}
