// This is the Kernel is the single reference point for every kernel call. The Kernel also is the central manager process where
// it handles OS requests, by connecting OS calls to the scheduler and controls which process runs next.
public class Kernel extends Process  {

    // An instance for the Scheduler.
    private Scheduler scheduler;
    VirtualFileSystem vfs = new VirtualFileSystem();

    /**
     * The constructor creates a Kernel instance by taking in an array of UserlandProcess, then proceeding to create a Scheduler, for every process
     * proceeds to create PCB for that process which takes in the process and priority type of the process (currently set to background), and adds the process
     * to correct priority queue.
     *
     * @param startup The array instance of UserlandProcess.
     */
    public Kernel(UserlandProcess[] startup) {
	// implement here
        scheduler = new Scheduler();
        for(var result: startup) {
            PCB pcb = new PCB(result, OS.PriorityType.background);
            scheduler.addPriorityQueue(pcb);
        }
        scheduler.referKernel(this);
    }

    @Override
    /**
     * This is the main method which has an infinite loop where the Kernel gets the current OS call and its parameters if there are any, to see what type of service is being requested,
     * then it dispatches the request by calling a matching Kernel method. Finally, after handling the request it starts the next scheduled process and stops
     * itself so only one process is running.
     */
    public void main() {
            while (true) { // Warning on infinite loop is OK...
                switch (OS.currentCall) { // get a job from OS, do it
                    case SwitchProcess -> SwitchProcess();

                    // Priority Scheduler
                    case Exit -> Exit();
                    case CreateProcess ->  // Note how we get parameters from OS and set the return value
                            OS.retVal = CreateProcess((UserlandProcess) OS.parameters.get(0), (OS.PriorityType) OS.parameters.get(1));
                    case Sleep -> Sleep((int) OS.parameters.get(0)); //Change cast type to "Integer" if it fails.
                    case GetPID -> OS.retVal = GetPid();
                    // Devices
                    case Open -> OS.retVal = Open((String) OS.parameters.get(0));
                    case Close -> Close((int) OS.parameters.get(0)); //Change cast type to "Integer" if it fails.
                    case Read -> OS.retVal = Read((int) OS.parameters.get(0), (int) OS.parameters.get(1)); //Change cast type to "Integer" if it fails.
                    case Seek -> Seek((int) OS.parameters.get(0), (int) OS.parameters.get(1)); //Change cast type to "Integer" if it fails.
                    case Write -> OS.retVal = Write((int) OS.parameters.get(0), (byte[]) OS.parameters.get(1)); //Change cast type to "Integer" if it fails.
                    // Messages
                    case GetPIDByName -> OS.retVal = GetPidByName((String) OS.parameters.get(0));
                    case SendMessage -> SendMessage((KernelMessage) OS.parameters.get(0));
                    case WaitForMessage -> OS.retVal = WaitForMessage();
                    // Memory
                    case GetMapping -> GetMapping((int) OS.parameters.get(0)); //Change cast type to "Integer" if it fails.
                    case AllocateMemory -> OS.retVal = AllocateMemory((int) OS.parameters.get(0)); //Change cast type to "Integer" if it fails.
                    case FreeMemory -> OS.retVal = FreeMemory((int) OS.parameters.get(0), (int) OS.parameters.get(1)); //Change cast type to "Integer" if it fails.
                }
                // TODO: Now that we have done the work asked of us, start some process then go to sleep.
                PCB current = scheduler.getCurrentlyRunning();
                if(current != null) {
                    current.start();
                }
                this.stop();
            }
    }

    /**
     * This function switches one process to another.
     */
    private void SwitchProcess() {
        scheduler.SwitchProcess();
    }

    /**
     * This is the exit function which unschedules the current process so that it never gets to run again.
     *
     */
    private void Exit() {
        //scheduler.currentlyRunning = null;
        scheduler.currentlyRunningNull();
        scheduler.SwitchProcess();
    }

    /**
     * This is method creates a process by taking in process and its priority level. Then it proceeds to create a process using those following parameters,
     * sets the timeout streak for the process to 0, adds that newly created process to the correct priority queue, and returns the process id.
     * @param up The process.
     * @param priority priority level.
     * @return The process id.
     */
    private int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
        PCB pcb = new PCB(up, priority);
        pcb.consecutiveTimeout = 0;
        scheduler.addPriorityQueue(pcb);
        return pcb.pid; // change this
    }

    /**
     * This takes in a numeric value which puts the process to sleep and will not run again until the process is awake. While the process is asleep, switches
     * to another process.
     *
     * @param mills The time to sleep.
     */
    private void Sleep(int mills) {
        scheduler.Sleep(mills);
        scheduler.SwitchProcess();
    }

    /**
     * This function returns the process id.
     *
     * @return The process id.
     */
    private int GetPid() {
        //return scheduler.currentlyRunning.pid; // change this
        PCB pcb = scheduler.getCurrentlyRunning();
        return pcb.pid;
    }

    private int Open(String s) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if(pcb == null) {
            return -1;
        }
        int space = -1;
        for(int i = 0; i < 10; i++) {
            if (pcb.devIDs[i] == -1) {
                space = i;
                break;
            }
        }
        if(space == -1) {
            return -1;
        }
        int vfsHolder = vfs.Open(s);
        if(vfsHolder == -1) {
            return -1;
        }
        pcb.devIDs[space] = vfsHolder;
        return space;
    }



    private void Close(int id) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if(pcb == null) {
            return;
        }
        if(id < 0 || id >= 10) {
            return;
        }
        int vfsHolder = pcb.devIDs[id];
        if(vfsHolder == -1) {
            return;
        }
        vfs.Close(vfsHolder);
        pcb.devIDs[id] = -1;
    }

    private byte[] Read(int id, int size) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if (pcb == null) {
            return null;
        }
        if(id < 0 || id >= 10) {
            return null;
        }
        int vfsHolder = pcb.devIDs[id];
        if(vfsHolder == -1) {
            return null;
        }
        byte[] buffer = vfs.Read(vfsHolder, size);
        return buffer;
    }

    private void Seek(int id, int to) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if(pcb == null) {
            return;
        }
        if(id < 0 || id >= 10) {
            return;
        }
        int vfsHolder = pcb.devIDs[id];
        if(vfsHolder == -1) {
            return;
        }
        vfs.Seek(vfsHolder, to);
    }

    private int Write(int id, byte[] data) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if(pcb == null) {
            return -1;
        }
        if(id < 0 || id >= 10) {
            return -1;
        }
        int vfsHolder = pcb.devIDs[id];
        if(vfsHolder == -1) {
            return -1;
        }
        int result = vfs.Write(vfsHolder, data);
        return result;
    }

    private void SendMessage(KernelMessage km) {
    }

    private KernelMessage WaitForMessage() {
        return null;
    }

    private int GetPidByName(String name) {
        return 0; // change this
    }

    private void GetMapping(int virtualPage) {
    }

    private int AllocateMemory(int size) {
        return 0; // change this
    }

    private boolean FreeMemory(int pointer, int size) {
        return true;
    }

    private void FreeAllMemory(PCB currentlyRunning) {
    }

    /**
     * This function is to retrieve the Scheduler.
     *
     * @return the Scheduler.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }
    public void closeAllDev(PCB pcb) {
        for(int i = 0; i < 10; i++) {
            int vfsHolder = pcb.devIDs[i];
            if(vfsHolder != -1) {
                vfs.Close(vfsHolder);
                pcb.devIDs[i] = -1;
            }
        }
    }
}
