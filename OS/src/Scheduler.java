import java.util.*;

public class Scheduler {
    private LinkedList<PCB> processes;
    private Timer timer;
    public PCB currentlyRunning;
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
    public void addProcess(PCB sample) {
        processes.add(sample);
    }
}

