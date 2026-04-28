// This is the Piggy process which is derived from UserlandProcess. This process tests the functionalities of reading and writing, where it should be writing and reading the same
// value, using more than the allocated memory where the process uses a memory size of 100 * 1024 bytes (instantiated 20 times on Testing Process).
public class Piggy extends UserlandProcess {

    @Override
    /**
     * This function allocates a memory size of 100 * 1024 bytes for the Piggy process. Where this has 100 pages of memory and writes a specific value to each page, reads
     * each page back to validate that the data originally written to was the same and not changed. Running many instances of this process at once fills the physical
     * memory and in turn enforces paging and swapping to occur.
     */
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
