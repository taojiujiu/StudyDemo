package src.thread.producer.customer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Restaurant {
    static class Meal {
        private final int orederNum;

        public Meal(int orederNum) {
            this.orederNum = orederNum;
        }

        @Override
        public String toString() {
            return "Meal " + this.orederNum;
        }
    }

    static class WaitPerson implements Runnable {
        private Restaurant restaurant;

        public WaitPerson(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (this) {
                        while (restaurant.meal == null) {
                            wait();
                        }
                        System.out.println("Waitperson got " + restaurant.meal);
                        synchronized (restaurant.chef) {
                            restaurant.meal = null;
                            restaurant.chef.notifyAll();
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Waitperson interrupted");
            }
        }
    }

    class Chef implements Runnable {
        private Restaurant restaurant;
        private int count = 0;

        public Chef(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try{
                while (!Thread.interrupted()){
                    synchronized (this){
                        while (restaurant.meal != null){
                            wait();
                        }
                    }
                    if(++count == 10){
                        System.out.println("Out of food, closing");
                        restaurant.exec.shutdownNow();
                    }
                    System.out.println("Order Up");
                    synchronized (restaurant.waitPerson){
                        restaurant.meal = new Meal(count);
                        restaurant.waitPerson.notifyAll();
                    }
                    TimeUnit.MICROSECONDS.sleep(100);
                }

            }catch (InterruptedException e) {
                System.out.println("Chef interrupted");
            }
        }
    }

    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);

    public Restaurant() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }

    public static void main(String[] args){
        new Restaurant();
    }
}
