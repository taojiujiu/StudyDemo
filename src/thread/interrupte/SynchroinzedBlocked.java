package thread.interrupte;

public class SynchroinzedBlocked implements  Runnable{
    public synchronized void f(){
        while(true){
            Thread.yield();
        }
    }

    public SynchroinzedBlocked() {
        new Thread(){
            public void run(){
                f();

            }
        }.start();
    }

    @Override
    public void run() {
        System.out.println("Trying to call f();");
        f();
        System.out.println("Exiting SynchronizedBlocked.run()");
    }
}