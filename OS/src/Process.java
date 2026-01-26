import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable{
    private boolean quantum;
    private Thread thread;
    private Semaphore availability;
    public Process() {
        quantum = false;
        availability = new Semaphore(0);
        thread = new Thread(this);
        thread.start();
	// implement here
    }

    public void requestStop() {
        quantum = true;
	// implement here
    }

    public abstract void main(); // this is the class your subclasses will implement

    public boolean isStopped() {
	// implement here
        if(availability.availablePermits() == 0) {
            return true;
        }
        else
            return false; // obviously wrong
    }

    public boolean isDone() {
	// implement here
        if(thread.isAlive()){
            return false;
        }
        else
            return true; // obviously wrong
    }

    public void start() {
	// implement here
        availability.release(1);
    }

    public void stop() {
	// implement here
        try {
            availability.acquire(1);
        }
        catch (InterruptedException e) {
            return;
        }
    }

    public void run() { // This is called by the Thread - NEVER CALL THIS!!!
	// implement here
        try {
            availability.acquire(1);
            main();
        }
        catch (InterruptedException e) {
            return;
        }
    }

    public void cooperate() {
	// implement here
        if(quantum) {
            quantum = false;
            OS.switchProcess();
        }
    }
}
