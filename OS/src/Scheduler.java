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
    PriorityQueue<SleepPCB> sleepPCBs = new PriorityQueue<>();

    public static class SleepPCB{
        long duration;
        PCB pcb;
    }



    /**
     * This is the constructor which creates a Scheduler instance. This function sets the list of PCBs, sets the Timer instance to a fixed 250 milliseconds. The
     * Timer instance is used as an interrupt, so this interrupt occurs every 250 milliseconds.
     *
     */
    public Scheduler() {
        processes = new LinkedList<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentlyRunning != null) {
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
        if(currentlyRunning != null) {
            processes.add(currentlyRunning);
        }
        PCB nextProcess = processes.poll();
        if (nextProcess == null) {
            return;
        }
        currentlyRunning = nextProcess;
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
}


