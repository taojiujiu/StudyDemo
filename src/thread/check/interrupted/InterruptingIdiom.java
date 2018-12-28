package thread.check.interrupted;

import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

public class InterruptingIdiom {
    static class NeedsCleanup{
        private final int id;

        public NeedsCleanup(int id) {
            this.id = id;
            System.out.println("NeedCleanup "+id);
        }
        public void cleanup(){
            System.out.println("Cleaning up " +id);
        }
    }

    static class Blocked3 implements Runnable{
        private volatile double d = 0.0;
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                NeedsCleanup n1 = new NeedsCleanup(1);
                try {
                    System.out.println("Sleeping ");
                    TimeUnit.SECONDS.sleep(1);
                    NeedsCleanup n2 = new NeedsCleanup(2);
                    try {
                        System.out.println("Calcaulating ");
                        for (int i = 0; i < 25000000; i++)
                            d = d = (Math.PI + Math.E) / d;
                        System.out.println("Finished time-consuming operation ");
                    } finally {
                        n2.cleanup();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Exiting via interruptedException");
                } finally {
                    n1.cleanup();
                }
            }
            System.out.println("Exiting via while() test");
        }
    }

    public  static void main(String[] args) throws InterruptedException {
        if(args.length!=1){
            System.out.println("usage : java InterruptingIdiom delay-in-mS");
            System.exit(1);
        }
        Thread thread = new Thread(new Blocked3());
        thread.start();
        TimeUnit.MICROSECONDS.sleep(new Integer(args[0]));
        thread.interrupt();
    }

}
