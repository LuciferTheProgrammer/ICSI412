import java.util.List;
import java.util.LinkedList;

// This is is the PCB (Process Control Block), which manages the process from the Kernel's perspective and is not visible from userland.
public class PCB { // Process Control Block

    // The next process id, which is set to 1 by default.
    private static int nextPid = 1;

    // This is the process id.
    public int pid;

    // This is the priority type of the process.
    private OS.PriorityType priority;

    // An instance of UserlandProcess.
    private UserlandProcess up;

    // The timeout streak.
    public int consecutiveTimeout;

    // Array of device ids of size 10.
    public int[] devIDs = new int[10];

    private String name;

    private List<KernelMessage> messages;


    /**
     * This is the constructor which takes in an instance of UserlandProcess and a priority type. Then it sets the priority type, the UserlandProcess, sets the
     * pid from the nextpid, and increments nextpid.
     *
     * @param up The UserlandProcess instance.
     * @param priority The priority level of the process.
     */
    public PCB(UserlandProcess up, OS.PriorityType priority) {
        this.priority = priority;
        this.up = up;
        pid = nextPid;
        nextPid++;
        for(int i = 0; i < devIDs.length; i++) {
            devIDs[i] = -1;
        }
        this.messages = new LinkedList<>();
        this.name = up.getClass().getSimpleName();
    }

    /**
     * This function returns process name.
     *
     * @return The process name.
     */
    public String getName() {
        return name;
    }
    public List<KernelMessage> getMessages() {
        return messages;
    }
    /**
     * This function returns the priority type of the process.
     *
     * @return The priority type of the process.
     */
    public OS.PriorityType getPriority() {
        return priority;
    }

    /**
     * This function sends a request for the process to stop.
     *
     */
    public void requestStop() {
        up.requestStop();
    }

    /**
     * This function stops the process from running.
     *
     */
    public void stop() { /* calls userlandprocess’ stop. Loops with Thread.sleep() until
ulp.isStopped() is true.  */
        up.stop();
        while(!up.isStopped()) {
            try {
                Thread.sleep(10);
            }
            catch(Exception e) {
                return;
            }
        }
    }

    /**
     * This function returns a boolean value, whether the process is finished or not.
     *
     * @return The boolean value, if the process is done or not.
     */
    public boolean isDone() { /* calls userlandprocess’ isDone() */
            return up.isDone();
    }

    /**
     *This function starts the process.
     */
    public void start() {
        up.start(); /* calls userlandprocess’ start() */
    }

    /**
     * This function sets the priority level of the process to a specific level.
     *
     * @param newPriority The priority level.
     */
    public void setPriority(OS.PriorityType newPriority) {
        priority = newPriority;
    }

}
