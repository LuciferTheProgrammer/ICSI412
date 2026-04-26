public class Piggy extends UserlandProcess {
    @Override
    public void main() {
        boolean flag = true;
        int sizeToUse = 100 * 1024;
        int ref = OS.AllocateMemory(sizeToUse);
        if(ref == -1) {
            System.out.println("Could not allocate memory for Piggy");
            OS.Exit();
        }
        int processID = OS.GetPID();
        for(int i = 0; i < 100; i++) {
            int address = ref + i * 1024;
            byte data = (byte) (i + processID);
            Hardware.Write(address, data);
        }
        for(int i = 0; i < 100; i++) {
            int address = ref + i * 1024;
            byte returnedData = Hardware.Read(address);
            byte correctData = (byte) (i + processID);
            if(correctData != returnedData) {
                System.out.println("Read/Write Test failed for Piggy, process ID: " + processID + ". Correct data = " + correctData + " does not match " + "Real data = " + returnedData);
                System.out.println("Found in page: " + i);
                flag = false;
            }
        }
        if(flag) {
            System.out.println("Read/Write Test passed successfully for Piggy, process ID: " + processID);
        }
        else {
            System.out.println("Read/Write Test failed for Piggy");
        }
        while(true) {
            OS.Sleep(50);
        }
    }
}
