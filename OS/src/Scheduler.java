import java.util.*;
import java.time.Clock;

// This is the Scheduler, it decides which process gets the CPU next (process to run) and for how long that process can run.
public class Scheduler {

    // The Linked List of PCBs.
    private LinkedList<PCB> processes;

    // A Timer instance.
    private Timer timer;

    // A PCB instance, for current running process.
    public PCB currentlyRunning;

    Clock clock = Clock.systemDefaultZone();
    PriorityQueue<SleepPCB> sleepPCBs;

    private static class SleepPCB{
        long duration;
        PCB pcb;
    }
    private Queue<PCB> interactive;
    private Queue<PCB> background;
    private Queue<PCB> realtime;
    private Random rand;
    private PCB stopped;



    /**
     * This is the constructor which creates a Scheduler instance. This function sets the list of PCBs, sets the Timer instance to a fixed 250 milliseconds. The
     * Timer instance is used as an interrupt, so this interrupt occurs every 250 milliseconds.
     *
     */
    public Scheduler() {
        rand = new Random();
        interactive = new LinkedList<>();
        background = new LinkedList<>();
        realtime = new LinkedList<>();
        sleepPCBs = new PriorityQueue<>(Comparator.comparingLong(x -> x.duration));
        processes = new LinkedList<>();
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
     * This function switches one process to another. It puts the current running process to the very end of the list of processes and extracts the next process
     * from the head of the list, which then becomes the current running process.
     *
     */
    public void SwitchProcess() {
        wakeProcesses();
        boolean timedOut = false;
        PCB previous = currentlyRunning;
        if(previous != null) {
            if (previous == stopped) {
                timedOut = true;
            }
            if(timedOut) {
                previous.consecutiveTimeout++;
                if(previous.consecutiveTimeout >= 5) {
                    demoteProcess(previous);
                    previous.consecutiveTimeout = 0;
                }
            }
            else {
                previous.consecutiveTimeout = 0;
            }
            stopped = null;
            PCB nextProcess = randomPick();
            currentlyRunning = nextProcess;
        }
    }

    /**
     * This function takes in a PCB instance and proceeds to add that to the list of PCBs.
     *
     * @param sample The PCB instance.
     */
    public void addProcess(PCB sample) {
        processes.add(sample);
    }

    public void Sleep(int mills) {
        PCB process = currentlyRunning;
        if (process != null) {
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
    private void wakeProcesses() {
        long current = clock.millis();
        while(!(sleepPCBs.isEmpty()) && (sleepPCBs.peek().duration <= current)) {
            SleepPCB container = sleepPCBs.poll();
            PCB process = container.pcb;
            processes.add(process);
        }
    }
    public void addPriorityQueue(PCB sample) {
        OS.PriorityType priority = sample.getPriority();
        switch (priority) {
            case realtime -> realtime.add(sample);
            case background -> background.add(sample);
            case interactive -> interactive.add(sample);
        }
    }
    private void demoteProcess(PCB sample) {
        OS.PriorityType priority = sample.getPriority();
        switch (priority) {
            case realtime -> sample.setPriority(OS.PriorityType.interactive);
            case interactive -> sample.setPriority(OS.PriorityType.background);
        }
    }
    private PCB randomPick() {
        PCB holder = null;
            if(!realtime.isEmpty()) {
                int range = rand.nextInt(10);
                if (range < 6) {
                    holder = realtime.poll();
                } else if (range < 9) {
                    holder = interactive.poll();
                } else {
                    holder = background.poll();
                }
                return holder;
            }
            else if(!interactive.isEmpty()) {
                int range = rand.nextInt(4);
                if(range < 3) {
                    holder = interactive.poll();
                }
                else {
                    holder = background.poll();
                }
                return holder;
            }
            return background.poll();
    }
}


