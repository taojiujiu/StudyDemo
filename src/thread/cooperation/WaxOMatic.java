package thread.cooperation;

import java.util.concurrent.TimeUnit;

public class WaxOMatic {
    static class Car {
        private boolean waxOn = false;

        public synchronized void waxed() {
            waxOn = true; // Ready to buff
            notifyAll();
        }

        public synchronized void buffed() {
            waxOn = false; // Ready for another cocat of wax
            notifyAll();
        }

        public synchronized void waitForWaxing() throws InterruptedException {
            while (waxOn == false) {
                wait();
            }
        }

        public synchronized void waitForBuffing() throws InterruptedException {
            while (waxOn == true) {
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
            }catch(InterruptedException e){
                System.out.println("Exiting via interrupt");
            }
            System.out.println("Ending Wax On task");
        }
    }

    static class WaxOff implements Runnable{
        private  Car car;

        public WaxOff(Car car) {
            this.car = car;
        }

        @Override
        public void run() {

        }
    }
}

