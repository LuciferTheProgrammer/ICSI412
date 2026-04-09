import java.util.*;
// This is the Kernel is the single reference point for every kernel call. The Kernel also is the central manager process where
// it handles OS requests, by connecting OS calls to the scheduler and controls which process runs next.
public class Kernel extends Process  {

    // An instance for the Scheduler.
    private Scheduler scheduler;

    // This is the Virtual File System.
    private VirtualFileSystem vfs = new VirtualFileSystem();

    // Mapping from Process ID to Process. To search which PCB belongs to a target PID, used by SendMessage().
    private Map<Integer, PCB> mappingPID;

    // Mapping from Process ID to Process. Mainly used for processes that are blocked and waiting for a message, used by WaitForMessage().
    private Map<Integer, PCB> waiting;

    private Random rand;

    private boolean[] pagesUsed = new boolean[1024];

    /**
     * The constructor creates a Kernel instance by taking in an array of UserlandProcess, then proceeding to create a Scheduler, for every process
     * proceeds to create PCB for that process which takes in the process and priority type of the process (currently set to background), and adds the process
     * to correct priority queue. It also creates the mapping for process IDs and their corresponding processes and also the mapping for processes
     * that are blocked and waiting for a message, process IDs and their corresponding processes. Adds all current process IDs and their corresponding
     * processes to the generic mapping.
     *
     * @param startup The array instance of UserlandProcess.
     */
    public Kernel(UserlandProcess[] startup) {
	// implement here
        rand = new Random();
        scheduler = new Scheduler();
        mappingPID = new HashMap<>();
        waiting = new HashMap<>();
        for(var result: startup) {
            PCB pcb = new PCB(result, OS.PriorityType.background);
            mappingPID.put(pcb.pid, pcb);
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
                    case GetPidByName -> OS.retVal = GetPidByName((String) OS.parameters.get(0));
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
     * If the current process exits, then all devices open from the process are closed. The process is removed from the generic mapping
     * between process IDs and processes and also removed from waiting block/mapping. The scheduler sets the current process to null and switches to
     * another process.
     *
     */
    private void Exit() {
        PCB current = scheduler.getCurrentlyRunning();
        if(current == null) {
            scheduler.SwitchProcess();
            return;
        }
        FreeAllMemory(current);
        closeAllDev(current);
        mappingPID.remove(current.pid);
        waiting.remove(current.pid);
        scheduler.currentlyRunningNull();
        scheduler.SwitchProcess();
    }

    /**
     * This is method creates a process by taking in process and its priority level. Then it proceeds to create a process using those following parameters,
     * sets the timeout streak for the process to 0, adds that newly created process to the correct priority queue, and returns the process id.
     * Now also puts the process ID and its corresponding process into the generic mapping.
     *
     * @param up The process.
     * @param priority priority level.
     * @return The process id.
     */
    private int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
        PCB pcb = new PCB(up, priority);
        pcb.consecutiveTimeout = 0;
        mappingPID.put(pcb.pid, pcb);
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

    /**
     * This is the Open function which uses getCurrentlyRunning() and finds an empty (-1) entry in the PCB's array. If there isn't one present, then
     * return -1, it fails. Then calls vfs.Open(), If the result is -1, it fails. Otherwise, it puts the id from the vfs into the PCB's array and returns
     * that array index.
     * Opens a device/file with the given String and returns the device slot based from the current running process.
     *
     * @param s The String.
     * @return array index.
     */
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


    /**
     * This is the Close function which uses the PCB array to convert to vfs and sets the PCB array entry to -1.
     *
     * @param id The id, index.
     */
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

    /**
     * This is the Read function which gets an id from Userland which uses the PCB array to convert that id to what vfs expects
     * and then this is passed to the call of the vfs. Reads data from a device based on the given size and returns the data read from the device in bytes.
     *
     * @param id The id, index.
     * @param size The size of the data.
     * @return The read data.
     */
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

    /**
     * This is the Seek function which gets an id from Userland which uses the PCB array to convert that id to what vfs expects
     * and then this is passed to the call of the vfs. This reads data from a device up to a certain point without returning the data that is read.
     *
     * @param id The id, index.
     * @param to up to certain point.
     */
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

    /**
     * This is the Write function which gets an id from Userland which uses the PCB array to convert that id to what vfs expects
     * and then this is passed to the call of the vfs. This writes data to a device with the given data and returns a corresponding index.
     *
     * @param id The id, index.
     * @param data The data to write.
     * @return The index.
     */
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

    /**
     * This function takes in a Kernel Message which is then copied, sets the sending pid, retrieves the target pid and uses that to extract the corresponding
     * process from the generic mapping. Then, obtains the messages of the target process and adds the message to the message queue,
     * and finally removes the target process from the waiting map/block. It also adds the process into the appropriate process priority queue.
     *
     * @param km Kernel Message.
     */
    private void SendMessage(KernelMessage km) {
        if(km == null) {
            return;
        }
        PCB sendingPCB = scheduler.getCurrentlyRunning();
        if(sendingPCB == null) {
            return;
        }
        KernelMessage copied = new KernelMessage(km);
        copied.setSenderPID(sendingPCB.pid);
        int targetPID = copied.getTargetPID();
        PCB target = mappingPID.get(targetPID);
        if(target == null) {
            return;
        }
        List<KernelMessage> messages = target.getMessages();
        messages.add(copied);
        if(waiting.containsKey(targetPID)) {
            waiting.remove(targetPID);
            scheduler.addPriorityQueue(target);
        }
    }

    /**
     * This function checks to see if the current process has a message. If so, then it removes it off of the queue and returns it. If not, then this function
     * will deschedule and add the current process ID and its corresponding process into the waiting map/block.
     *
     * @return the Kernel Message.
     */
    private KernelMessage WaitForMessage() {
        PCB current = scheduler.getCurrentlyRunning();
        if(current == null) {
            return null;
        }
        List<KernelMessage> messages = current.getMessages();
        if(!messages.isEmpty()) {
            KernelMessage first = messages.remove(0);
            return first;
        }
        waiting.put(current.pid, current);
        scheduler.currentlyRunningNull();
        scheduler.SwitchProcess();
        return null;
    }

    /**
     * This function takes in a process name where it first checks the current running process and see if its process name is the equal to the given
     * name parameter; if so, then it returns that process id. If not, it then loops and checks processes in the priority queues such as the realtime,
     * interactive, and background, also checks in the sleeping processes queue, and finally also check in the waiting map/block. If it finds a matching process
     * name with the given name, then it returns that process id. If not, returns -1 to indicate no match was found.
     *
     * @param name The process name.
     * @return The process id.
     */
    private int GetPidByName(String name) {
        if(name == null)
            return -1;
        PCB cur = scheduler.getCurrentlyRunning();
        if(cur != null && cur.getName().equals(name)) {
            return cur.pid;
        }
        Queue<PCB> realtimeQueue = scheduler.getPriorityQueue("realtime");
        if(realtimeQueue != null) {
            for(PCB pcb : realtimeQueue) {
                if(pcb != null && pcb.getName().equals(name)) {
                    return pcb.pid;
                }
            }
        }
        Queue<PCB> interactiveQueue = scheduler.getPriorityQueue("interactive");
        if(interactiveQueue != null) {
            for(PCB pcb : interactiveQueue) {
                if(pcb != null && pcb.getName().equals(name)) {
                    return pcb.pid;
                }
            }
        }
        Queue<PCB> backgroundQueue = scheduler.getPriorityQueue("background");
        if(backgroundQueue != null) {
            for(PCB pcb : backgroundQueue) {
                if(pcb != null && pcb.getName().equals(name)) {
                    return pcb.pid;
                }
            }
        }
        Queue<Scheduler.SleepPCB> sleepQueue = scheduler.getSleepPCBs();
        if(sleepQueue != null) {
            for(Scheduler.SleepPCB sleep : sleepQueue) {
                if(sleep != null) {
                    PCB process = sleep.getPcb();
                    if(process != null && process.getName().equals(name)) {
                        return process.pid;
                    }
                }
            }
        }
        for(PCB pcb : waiting.values()) {
            if(pcb != null && pcb.getName().equals(name)) {
                return pcb.pid;
            }
        }
        return -1;// change this
    }

    private void GetMapping(int virtualPage) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if (pcb == null) {
            return;
        }
        int[] mapping = pcb.getMapping();
        if (virtualPage < 0 || virtualPage >= mapping.length) {
            System.out.println("seg fault");
            closeAllDev(pcb);
            mappingPID.remove(pcb.pid);
            waiting.remove(pcb.pid);
            scheduler.currentlyRunningNull();
            scheduler.SwitchProcess();
            return;
        }
        int physicalPage = mapping[virtualPage];
        if(physicalPage == -1) {
            System.out.println("seg fault");
            closeAllDev(pcb);
            mappingPID.remove(pcb.pid);
            waiting.remove(pcb.pid);
            scheduler.currentlyRunningNull();
            scheduler.SwitchProcess();
            return;
        }
        int randomInt = rand.nextInt(2);
        Hardware.modifyTLB(virtualPage, physicalPage, randomInt);
    }

    private int AllocateMemory(int size) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if(pcb == null) {
            return -1;
        }
        int required = size / 1024;
        int[] mapping = pcb.getMapping();
        int start = -1;
        for(int i = 0; i <= mapping.length - required; i++) {
            boolean hole = true;
            for(int k = 0; k < required; k++) {
                if(mapping[i + k] != -1) {
                    hole = false;
                    break;
                }
            }
            if(hole) {
                start = i;
                break;
            }
        }
        if(start == -1) {
            return -1;
        }
        for(int i = start; i < start + required; i++) {
            int physicalPage = -1;
            for(int k = 0; k < pagesUsed.length; k++) {
                if(!pagesUsed[k]) {
                    physicalPage = k;
                    break;
                }
            }
            if(physicalPage == -1) {
                for(int j = start; j < i; j++) {
                    int old = mapping[j];
                    pagesUsed[old] = false;
                    mapping[j] = -1;
                }
                return -1;
            }
            pagesUsed[physicalPage] = true;
            mapping[i] = physicalPage;
        }
        int space = start * 1024;
        return space;
    }

    private boolean FreeMemory(int pointer, int size) {
        PCB pcb = scheduler.getCurrentlyRunning();
        if(pcb == null) {
            return false;
        }
        int virtualPage = pointer / 1024;
        int free = size / 1024;
        int[] mapping = pcb.getMapping();
        int range = (virtualPage + free - 1);
        if(virtualPage < 0 || range >= mapping.length) {
            return false;
        }
        for(int i = virtualPage; i <= range; i++) {
            if(mapping[i] == -1) {
                return false;
            }
        }
        for(int i = virtualPage; i <= range; i++) {
            int physicalPage = mapping[i];
            pagesUsed[physicalPage] = false;
            mapping[i] = -1;
        }
        return true;
    }

    public void FreeAllMemory(PCB currentlyRunning) {
        int[] mapping = currentlyRunning.getMapping();
        for(int i = 0; i < mapping.length; i++) {
            if(mapping[i] != -1) {
                int physicalPage = mapping[i];
                pagesUsed[physicalPage] = false;
                mapping[i] = -1;
            }
        }
    }

    /**
     * This function is to retrieve the Scheduler.
     *
     * @return the Scheduler.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * This function closes all the devices under a process/PCB.
     *
     * @param pcb The process.
     */
    public void closeAllDev(PCB pcb) {
        for(int i = 0; i < 10; i++) {
            int vfsHolder = pcb.devIDs[i];
            if(vfsHolder != -1) {
                vfs.Close(vfsHolder);
                pcb.devIDs[i] = -1;
            }
        }
    }

    /**
     * This function takes in a String and returns the generic mapping or the waiting map.
     *
     * @param s String, keyword.
     * @return The generic mapping or waiting map.
     */
    public Map<Integer, PCB> getPcbMap(String s) {
        switch(s) {
            case "mapPID" -> {return mappingPID;}
            case "wait" -> {return waiting;}
        }
        return null;
    }
}
