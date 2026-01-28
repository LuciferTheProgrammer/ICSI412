public class PCB { // Process Control Block
    private static int nextPid = 1;
    public int pid;
    private OS.PriorityType priority;
    private UserlandProcess up;

    public PCB(UserlandProcess up, OS.PriorityType priority) {
        this.priority = priority;
        this.up = up;
        pid = nextPid;
        nextPid++;
    }

    public String getName() {
        return up.getClass().getSimpleName();
    }

    public OS.PriorityType getPriority() {
        return priority;
    }

    public void requestStop() {
        up.requestStop();
    }

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

    public boolean isDone() { /* calls userlandprocess’ isDone() */
            return up.isDone(); // Change
    }

    public void start() {
        up.start(); /* calls userlandprocess’ start() */
    }

    public void setPriority(OS.PriorityType newPriority) {
        priority = newPriority;
    }
}
