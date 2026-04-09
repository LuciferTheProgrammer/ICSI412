import java.util.*;
import java.time.Clock;

// This is the Scheduler, it decides which process gets the CPU next (process to run) and for how long that process can run.
public class Scheduler {

    // A Timer instance.
    private Timer timer;

    // A PCB instance, for current running process.
    private PCB currentlyRunning;

    // Clock to set timer to wake process.
    private Clock clock;

    // Sleeping process queue.
    private PriorityQueue<SleepPCB> sleepPCBs;

    // Internal class to define sleeping process.
    public static class SleepPCB{

        // Time duration for process to sleep.
        private long duration;

        // The PCB for the sleeping process.
        private PCB pcb;

        public PCB getPcb() {
            return pcb;
        }
    }

    // Interactive process queue.
    private Queue<PCB> interactive;

    // Background process queue.
    private Queue<PCB> background;

    // Realtime process queue.
    private Queue<PCB> realtime;

    // Random number for range.
    private Random rand;

    // The process that is stopped.
    private PCB stopped;

    // This is the reference to the Kernel.
    private Kernel referenceKernel;



    /**
     * This is the constructor which creates a Scheduler instance. This function sets the Timer instance to a fixed 250 milliseconds. The
     * Timer instance is used as an interrupt, so this interrupt occurs every 250 milliseconds. In addition, also creates the priority queues for realtime,
     * interactive, and background processes. It also creates a queue to hold sleeping processes. Finally, sets the random integer and the clock value.
     *
     */
    public Scheduler() {
        clock = Clock.systemDefaultZone();
        rand = new Random();
        interactive = new LinkedList<>();
        background = new LinkedList<>();
        realtime = new LinkedList<>();
        sleepPCBs = new PriorityQueue<>(Comparator.comparingLong(x -> x.duration));
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentlyRunning != null) {
                    stopped = currentlyRunning;
                    currentlyRunning.requestStop();
                }
            }
        }, 250,250);
    }

    /**
     * This function switches one process to another. It wakes a sleeping process which is put to a runnable priority queue,
     * stops the current process and puts the stopped process into the correct priority queue, while also keeping track of the number of
     * times the process had run to a timeout, if it's more than 5 times in a row, then the process is demoted to a lower level priority queue.
     * Finally, uses randomPick() method to figure out what queue to get the next process to run from. Also, now checks if the previous running process
     * is finished; if so, then it retrieves the generic mapping and waiting map/block, and finally removes the previously completed process from both.
     *
     */
    public void SwitchProcess() {
        wakeProcesses();
        boolean timedOut = false;
        PCB previous = currentlyRunning;
        if(previous != null) {
            if (previous.isDone()) {
                referenceKernel.FreeAllMemory(previous);
                referenceKernel.closeAllDev(previous);
                Map<Integer, PCB> mapping = referenceKernel.getPcbMap("mapPID");
                mapping.remove(previous.pid);
                Map<Integer, PCB> wait = referenceKernel.getPcbMap("wait");
                wait.remove(previous.pid);
            } else {
                if (previous == stopped) {
                    timedOut = true;
                }
                if (timedOut) {
                    previous.consecutiveTimeout++;
                    if (previous.consecutiveTimeout > 5) {
                        demoteProcess(previous);
                        previous.consecutiveTimeout = 0;
                    }
                } else {
                    previous.consecutiveTimeout = 0;
                }
                addPriorityQueue(previous);
            }
        }
        stopped = null;
        currentlyRunning = randomPick();
        Hardware.TLBClean();
    }


    /**
     * This method takes in the requested amount of time to sleep which is added to the current clock value, the minimum time for the process
     * to wake up. It also puts the sleeping process to a separate queue that holds other sleeping processes.
     *
     * @param mills The time to sleep.
     */
    public void Sleep(int mills) {
        PCB process = currentlyRunning;
        if (process != null) {
            process.consecutiveTimeout = 0;
            SleepPCB carrier = new SleepPCB();
            long currentTime = clock.millis();
            long sum = currentTime + mills;
            carrier.pcb = process;
            carrier.duration = sum;
            sleepPCBs.add(carrier);
            currentlyRunning = null;
        }
        else {
            return;
        }
    }

    /**
     * This method wakes up a sleeping process from the sleeping queue and then proceeds to add the woken process to the correct
     * priority queue.
     *
     */
    private void wakeProcesses() {
        long current = clock.millis();
        while(!(sleepPCBs.isEmpty()) && (sleepPCBs.peek().duration <= current)) {
            SleepPCB container = sleepPCBs.poll();
            PCB process = container.pcb;
            addPriorityQueue(process);
        }
    }

    /**
     * This method takes in a process and based on its priority level, adds it to the appropriate priority queue.
     *
     * @param sample The process.
     */
    public void addPriorityQueue(PCB sample) {
        OS.PriorityType priority = sample.getPriority();
        switch (priority) {
            case realtime -> realtime.add(sample);
            case background -> background.add(sample);
            case interactive -> interactive.add(sample);
        }
    }

    /**
     * This method takes in a process and based on its priority level demotes that process from a higher priority level
     * to a lower priority level. Demotes a realtime process to an interactive process and finally demotes an interactive process to
     * a background process.
     *
     * @param sample The process.
     */
    private void demoteProcess(PCB sample) {
        OS.PriorityType priority = sample.getPriority();
        switch (priority) {
            case realtime ->  {
                sample.setPriority(OS.PriorityType.interactive);
                System.out.println("DEMOTE: " + sample.getName() + " -> " + sample.getPriority() + ".");
            }
            case interactive -> {
                sample.setPriority(OS.PriorityType.background);
                System.out.println("DEMOTE: " + sample.getName() + " -> " + sample.getPriority() + ".");
            }
        }
    }

    /**
     * This method uses a probabilistic model to randomly pick a process from the three priority queues, one of each associated with the realtime,
     * interactive, and background. If there are realtime processes, 6/10 will run a realtime process, 3/10 will run an interactive process, and 1/10
     * will run a background process. Otherwise, if there are interactive processes, then it will 3/4 run interactive and 1/4 run background. If there are only,
     * background, then only the first of those will run. The function also has fallbacks for each case.
     *
     * @return The randomly picked process.
     */
    private PCB randomPick() {
            if(!realtime.isEmpty()) {
                int range = rand.nextInt(10);
                if (range < 6) {
                    return(realtime.poll());
                }
                else if (range < 9) {
                    if (!interactive.isEmpty()) {
                        return(interactive.poll());
                    }
                    if(!background.isEmpty()){
                        return(background.poll());
                    }
                    return(realtime.poll());
                }
                else {
                    if(!background.isEmpty()) {
                        return(background.poll());
                    }
                    if(!interactive.isEmpty()) {
                        return(interactive.poll());
                    }
                    return(realtime.poll());
                }
            }
            if(!interactive.isEmpty()) {
                int range = rand.nextInt(4);
                if(range < 3) {
                    return(interactive.poll());
                }
                else {
                    if(!background.isEmpty()) {
                        return(background.poll());
                    }
                    return(interactive.poll());
               }
            }
            if(!background.isEmpty()) {
                return(background.poll());
            }
            return null;
    }

    /**
     * This function returns the current running process.
     *
     * @return current running process.
     */
    public PCB getCurrentlyRunning() {
        return currentlyRunning;
    }

    /**
     * This function assigns the Kernel to be referenced by the Scheduler.
     *
     * @param k The Kernel to refer to.
     */
    public void referKernel(Kernel k) {
        referenceKernel = k;
    }

    /**
     * Sets the current running process to null.
     *
     */
    public void currentlyRunningNull() {
        currentlyRunning = null;
    }

    /**
     * This function takes in a String. Given the keyword, it returns either the realtime priority queue, background priority queue, or interactive
     * priority queue.
     *
     * @param chosen keyword.
     * @return The realtime, background, or interactive priority queue.
     */
    public Queue<PCB> getPriorityQueue(String chosen) {
        switch(chosen) {
            case "realtime" -> {return realtime;}
            case "background" -> {return background;}
            case "interactive" -> {return interactive;}
        }
        return null;
    }

    /**
     * This function returns the sleeping processes queue.
     *
     * @return The sleeping processes queue.
     */
    public Queue<SleepPCB> getSleepPCBs() {
        return sleepPCBs;
    }
}


