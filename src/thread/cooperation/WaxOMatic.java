package src.thread.cooperation;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WaxOMatic {
    static class Car {
        private boolean waxOn = false;

        public synchronized void waxed() {
            waxOn = true; // Ready to buff
            notifyAll();
        }

        public synchronized void buffed() {
            waxOn = false; // Ready for another coat of wax
            notifyAll();
        }

        public synchronized void waitForWaxing() throws InterruptedException {
            while (!waxOn) {
                wait();
            }
        }

        public synchronized void waitForBuffing() throws InterruptedException {
            while (waxOn) {
                wait();
            }
        }
    }

    static class WaxOn implements Runnable {
        private Car car;

        public WaxOn(Car car) {
            this.car = car;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    System.out.println("Wax on");
                    TimeUnit.MICROSECONDS.sleep(200);
                    car.waxed();
                    car.waitForBuffing();
                }
            } catch (InterruptedException e) {
                System.out.println("Exiting via interrupt");
            }
            System.out.println("Ending Wax On task");
        }
    }

    static class WaxOff implements Runnable {
        private Car car;

        public WaxOff(Car car) {
            this.car = car;
        }

        @Override
        public void run() {

            try {
                while (!Thread.interrupted()) {
                    car.waitForWaxing();
                    System.out.println("Wax off");
                    TimeUnit.MICROSECONDS.sleep(200);
                    car.buffed();
                }
            } catch (InterruptedException e) {
               System.out.println("Exiting Wax off task");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Car car = new Car();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WaxOff(car));
        executorService.execute(new WaxOn(car));
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();

    }
}

