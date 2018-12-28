package thread.blocked.moute.by.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Interrupting {
    static class BlockedMutex{
        private Lock lock = new ReentrantLock();

        public BlockedMutex() {
            lock.lock();
        }
        public void f(){
            try {
                lock.lockInterruptibly();
                System.out.println("lock acquired in f()");
            } catch (InterruptedException e) {
                System.out.println("Interrupted from lock acquisition in f()");
            }
        }

    }

    static class Blocked implements Runnable{
        BlockedMutex blocked = new BlockedMutex();
        @Override
        public void run() {
            System.out.println("Waiting for f() in BlockedMutex()");
            blocked.f();
            System.out.println("Broken out if blocked call");
        }
        public static void main(String[] args) throws InterruptedException {
            Thread thread1 = new Thread(new Blocked());
            thread1.start();
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Issuing thread.interrupt()");
            thread1.interrupt();
        }
    }

}
