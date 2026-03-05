import java.util.concurrent.Semaphore;

// This is basically the programs that will be running.
public abstract class Process implements Runnable{

    // A time period for a process to run, a flag to indicate if the time period for the process has expired or not.
    private boolean quantum;

    // An instance of the Thread class, allows for multiple things to run at the same time.
    private Thread thread;

    // An instance of the Semaphore class, a counter like integers. Acts as a lock.
    private Semaphore availability;

    /**
     * This is the constructor which sets the quantum, the Semaphore instance, the Thread instance, and starts the Thread instance.
     */
    public Process() {
        quantum = false;
        availability = new Semaphore(0);
        thread = new Thread(this);
        thread.start();
    }

    /**
     * This function sets the quantum to a boolean value of true. Which indicates that the time period for the process has expired and the process has to stop.
     *
     */
    public void requestStop() {
        quantum = true;
    }

    /**
     * This function will be implemented by classes derived from the Process class.
     */
    public abstract void main(); // this is the class your subclasses will implement

    /**
     * This function returns a boolean value of whether the process has stopped running or not with the use of the Semaphore instance. Indicates if the Semaphore
     * is 0, return true and otherwise returns false.
     *
     * @return The boolean value of whether the process has stopped or not.
     */
    public boolean isStopped() {
	// implement here
        if(availability.availablePermits() == 0) {
            return true;
        }
        else
            return false;
    }

    /**
     * This function returns a boolean value of whether the process has finished or not with the use of the Thread instance. This returns true if the Thread is
     * alive and otherwise returns false.
     *
     * @return The boolean value of whether the process has finished or not.
     */
    public boolean isDone() {
	// implement here
        if(thread.isAlive()){
            return false;
        }
        else
            return true;
    }

    /**
     * This function starts the process by releasing (increments) the Semaphore, which allows the Thread to run.
     *
     */
    public void start() {
	// implement here
        availability.release(1);
    }

    /**
     * This function stops the process by acquiring (decrements) the Semaphore, which stops the Thread from running.
     */
    public void stop() {
	// implement here
        try {
            availability.acquire(1);
        }
        catch (InterruptedException e) {
            return;
        }
    }

    /**
     * This function runs the process by acquiring the Semaphore and then calling main().
     */
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

    /**
     * This function is for cooperative multitasking, that if the quantum is true, then it is set to false and then switches process.
     *
     */
    public void cooperate() {
	// implement here
        if(quantum) {
            quantum = false;
            OS.switchProcess();
       }
    }
}
