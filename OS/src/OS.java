import java.util.ArrayList;
import java.util.List;

// This is the OS, the gateway between userland thread and kernel thread.
public class OS {

    // The instance of the Kernel.
    private static Kernel ki; // The one and only one instance of the kernel.

    // This is the list of parameters to a function.
    public static List<Object> parameters = new ArrayList<>();

    // The return value of a function.
    public static Object retVal;

    // An enum of what function to call.
    public enum CallType {SwitchProcess,SendMessage, Open, Close, Read, Seek, Write, GetMapping, CreateProcess, Sleep, GetPID, AllocateMemory, FreeMemory, GetPidByName, WaitForMessage, Exit}

    // An instance of the enum of the current function call.
    public static CallType currentCall;

    /**
     * This function starts the Kernel. If the Scheduler has a currently running process, that process is stopped.
     */
    private static void startTheKernel() {
        retVal = null;
        Scheduler holder = ki.getScheduler();
        PCB sample = null;
        if(holder!= null) {
            sample = holder.getCurrentlyRunning();
        }
        ki.start();
        if(sample != null) {
            sample.stop();
        }
        else {
            if(currentCall == CallType.CreateProcess || currentCall == CallType.Read || currentCall == CallType.Write ||
                    currentCall == CallType.Open || currentCall == CallType.GetPID || currentCall == CallType.GetPidByName ||
            currentCall == CallType.WaitForMessage || currentCall == CallType.SendMessage || currentCall == CallType.FreeMemory || currentCall == CallType.AllocateMemory ||
            currentCall == CallType.GetMapping) {
                while (retVal == null) {
                    try {
                        Thread.sleep(10);
                    }
                    catch (Exception e) {

                    }
                }
            }
        }
    }

    /**
     * This process switches the one process to another process. It clears the parameters, sets the current call and switch to the Kernel.
     */
    public static void switchProcess() {
        parameters.clear();
        currentCall = CallType.SwitchProcess;
        startTheKernel();
    }

    /**
     * This function is the startup, where it creates the Kernel, being initialized with an array instance of UserlandProcess and switches Process.
     *
     * @param init the array instance of UserlandProcess.
     */
    public static void Startup(UserlandProcess[] init) {
		// Create a kernel here
        ki = new Kernel(init);
        switchProcess();
    }

    // The enum for the priority types for processes.
    public enum PriorityType {realtime, interactive, background}

    /**
     * This function creates a process by taking in a UserlandProcess and its priority type. Then it proceeds to reset the parameters, adds the new parameters
     * to the parameter list, sets the current call, switches to the Kernel, and cast and return the value, an integer.
     *
     * @param up The UserlandProcess.
     * @param priority The priority of the the process.
     * @return the casted value, an integer.
     */
    public static int CreateProcess(UserlandProcess up, PriorityType priority) {
        parameters.clear();
        parameters.add(up);
        parameters.add(priority);
        currentCall = CallType.CreateProcess;
        startTheKernel();
        return (int) retVal;
    }

    /**
     * This function resets the parameters, sets the current call, switches to the Kernel, and cast and return the value, an integer.
     *
     * @return the cased value, an integer.
     */
    public static int GetPID() {
        parameters.clear();
        currentCall = CallType.GetPID;
        startTheKernel();
        return (int) retVal;
    }

    /**
     * This function resets the parameters, sets the current call, and switches to the Kernel.
     */
    public static void Exit() {
        parameters.clear();
        currentCall = CallType.Exit;
        startTheKernel();
    }

    /**
     * This function takes in an integer value, then proceeds to reset the parameters, adds the new parameters to the parameter list, sets the current call, and switches
     * to the Kernel.
     *
     * @param mills The time in milliseconds, an integer value.
     */
    public static void Sleep(int mills) {
        parameters.clear();
        parameters.add(mills);
        currentCall = CallType.Sleep;
        startTheKernel();
    }

    // Devices

    /**
     * This function takes in a String, then proceeds to reset the parameters, adds the new parameters to the parameter list, sets the current call,
     * switches to the Kernel, and finally cast and return the value, an integer value.
     *
     * @param s The String object.
     * @return The casted value, an integer value.
     */
    public static int Open(String s) {
        parameters.clear();
        parameters.add(s);
        currentCall = CallType.Open;
        startTheKernel();
        return ((int)retVal);
    }

    /**
     * This function takes in an id, an integer value. Then it proceeds to reset the parameters, adds the new parameters to the parameter list, sets the
     * current call, and switches to the Kernel.
     *
     * @param id The id,an integer value.
     */
    public static void Close(int id) {
        parameters.clear();
        parameters.add(id);
        currentCall = CallType.Close;
        startTheKernel();
    }

    /**
     * This function takes in an id and size, both of which are integer values. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, switches to the Kernel, and cast and return a value, an array of type byte.
     *
     * @param id The id, an integer value.
     * @param size The size, an integer value.
     * @return The casted byte array.
     */
    public static byte[] Read(int id, int size) {
        parameters.clear();
        parameters.add(id);
        parameters.add(size);
        currentCall = CallType.Read;
        startTheKernel();
        return (byte[]) retVal;
    }

    /**
     * This function takes in an id and to, both of which are integer values. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, and switches to the Kernel.
     *
     * @param id The id, an integer value.
     * @param to The to, an integer value.
     */
    public static void Seek(int id, int to) {
        parameters.clear();
        parameters.add(id);
        parameters.add(to);
        currentCall = CallType.Seek;
        startTheKernel();
    }

    /**
     * This function takes in an id, an integer value, and data, a byte array. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, switches to the Kernel, and cast and return the value, an integer value.
     *
     * @param id The id, an integer value.
     * @param data The data, a byte array.
     * @return The casted value, an integer value.
     */
    public static int Write(int id, byte[] data) {
        parameters.clear();
        parameters.add(id);
        parameters.add(data);
        currentCall = CallType.Write;
        startTheKernel();
        return (int) retVal;
    }

    // Messages

    /**
     * This function takes in a KernelMessage instance. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, and switches to the Kernel.
     *
     * @param km The KernelMessage instance.
     */
    public static void SendMessage(KernelMessage km) {
        parameters.clear();
        parameters.add(km);
        currentCall = CallType.SendMessage;
        startTheKernel();
    }

    /**
     * This function proceeds to reset the parameters, sets the current call, switches to the Kernel, and returns a KernelMessage instance.
     *
     * @return The KernelMessage instance.
     */
    public static KernelMessage WaitForMessage() {
        parameters.clear();
        currentCall = CallType.WaitForMessage;
        startTheKernel();
        return (KernelMessage) retVal;
    }

    /**
     * This function takes in a String. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, switches to the Kernel, and cast and return a value, an integer value.
     *
     * @param name The String name of the process.
     * @return The casted value, an integer value.
     */
    public static int GetPidByName(String name) {
        parameters.clear();
        parameters.add(name);
        currentCall = CallType.GetPidByName;
        startTheKernel();
        return (int) retVal;
    }

    // Memory

    /**
     * This function takes in a virtual page, an integer value. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, and switches to the Kernel.
     *
     * @param virtualPage
     */
    public static void GetMapping(int virtualPage) {
        parameters.clear();
        parameters.add(virtualPage);
        currentCall = CallType.GetMapping;
        startTheKernel();
    }

    /**
     * This function takes in the size, an integer value. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, switches to the Kernel, and cast and return a value, an integer value.
     *
     * @param size The size of memory, an integer value.
     * @return The casted value, an integer value.
     */
    public static int AllocateMemory(int size) {
        if(size <= 0 || size % 1024 != 0) {
            return -1;
        }
        parameters.clear();
        parameters.add(size);
        currentCall = CallType.AllocateMemory;
        startTheKernel();
        return (int) retVal;
}

    /**
     * This function takes in a pointer and size, both of which are integers. Then it proceeds to reset the parameters, adds the new parameters to the parameter list,
     * sets the current call, switches to the Kernel, and cast and return a value, a boolean.
     *
     * @param pointer The pointer reference, an integer value.
     * @param size The size of memory, an integer value.
     * @return The casted value, a boolean.
     */
    public static boolean FreeMemory(int pointer, int size) {
        if(pointer < 0 || pointer % 1024 != 0 || size <= 0 || size % 1024 != 0) {
            return false;
        }
        parameters.clear();
        parameters.add(pointer);
        parameters.add(size);
        currentCall = CallType.FreeMemory;
        startTheKernel();
        return (boolean) retVal;
    }
}
