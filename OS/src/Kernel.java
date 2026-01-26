public class Kernel extends Process  {
    public Kernel(UserlandProcess[] startup) {
	// implement here	
    }

    @Override
    public void main() {
            while (true) { // Warning on infinite loop is OK...
                switch (OS.currentCall) { // get a job from OS, do it
                    case SwitchProcess -> SwitchProcess();
                    /*
                    // Priority Schduler
                    case Exit -> Exit();
                    case CreateProcess ->  // Note how we get parameters from OS and set the return value
                            OS.retVal = CreateProcess((UserlandProcess) OS.parameters.get(0), (OS.PriorityType) OS.parameters.get(1));
                    case Sleep -> Sleep((int) OS.parameters.get(0));
                    case GetPID -> OS.retVal = GetPid();
                    // Devices
                    case Open ->
                    case Close ->
                    case Read ->
                    case Seek ->
                    case Write ->
                    // Messages
                    case GetPIDByName ->
                    case SendMessage ->
                    case WaitForMessage ->
                    // Memory
                    case GetMapping ->
                    case AllocateMemory ->
                    case FreeMemory ->
                     */
                }
                // TODO: Now that we have done the work asked of us, start some process then go to sleep.
            }
    }

    private void SwitchProcess() {}

    private void Exit() {
    }

    private int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
        return 0; // change this
    }

    private void Sleep(int mills) {
    }

    private int GetPid() {
        return 0; // change this
    }

    private int Open(String s) {
        return 0; // change this
    }

    private void Close(int id) {
    }

    private byte[] Read(int id, int size) {
        return null; // change this
    }

    private void Seek(int id, int to) {
    }

    private int Write(int id, byte[] data) {
        return 0; // change this
    }

    private void SendMessage(/*KernelMessage km*/) {
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

}
