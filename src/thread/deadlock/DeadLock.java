package thread.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLock {

    static class Freind {
        Lock lock = new ReentrantLock();

        private final String name;
        public Freind(String name_) {
            this.name = name_;
        }

        public String getName() {
            return this.name;
        }

        public  void bow(Freind bower) throws InterruptedException {
            lock.lockInterruptibly();
            System.out.format("%s : %s " + "has bowed to me!%n",
                    this.name, bower.getName());
            Thread.sleep(2000);
            System.out.println(this.name + " bower.bowBack");
            bower.bowBack(this);

        }

        public  void bowBack(Freind bower) throws InterruptedException {
            lock.lockInterruptibly();
            System.out.format("%s : %s " + "has bowed back to me!%n",
                    this.name, bower.getName());
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) throws InterruptedException {
        // wait for 4 seconds before send a interuption signal
        int patient = 4000;
        int waitingUnit = 1000;
        long start;
        final Freind alphonse = new Freind("Alphonse");
        final Freind gaston = new Freind("Gaston");
        // the first thread
        Thread alphonseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    alphonse.bow(gaston);
                } catch (InterruptedException ex) {
                    System.out.println("Alphnso have been killed");
                }
            }
        });
        // the second thread
        Thread gastonThread = new Thread(new Runnable() {
            @Override
            public void run() {

                    try {
                        gaston.bow(alphonse);
                    } catch (InterruptedException ex) {
                        System.out.println("Gaston have been killed ");
                    }
            }
        });
        // start the tow threads
        start = System.currentTimeMillis();
        alphonseThread.start();
        gastonThread.start();
        TimeUnit.SECONDS.sleep(1);
        alphonseThread.interrupt();
        while (alphonseThread.isAlive() || gastonThread.isAlive()) {
            if (((System.currentTimeMillis() - start) < patient)) {
                if(alphonseThread.isAlive()){
                    System.out.println(System.currentTimeMillis() - start);
                    System.out.println(" alphonseThread still waiting !!");
                    Thread.sleep(waitingUnit);
                }
                if( gastonThread.isAlive()){
                    System.out.println(System.currentTimeMillis() - start);
                    System.out.println(" gastonThread still waiting !!");
                    Thread.sleep(waitingUnit);
                }
            } else {
                if (alphonseThread.isAlive()) {
                    alphonseThread.interrupt();
                    alphonseThread.join();
                }
                if (gastonThread.isAlive()) {
                    gastonThread.interrupt();
                    gastonThread.join();
                }
            }
        }
        System.out.println("finnaly I kill all of them");
    }
}