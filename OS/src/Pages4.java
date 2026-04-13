// This is the Pages4 process which is derived from UserlandProcess. This process
// allocates a space in memory which returns a pointer to the starting address
// in the allocated block and then proceeds to write data into memory
// and reads the data back from memory. It tests read/write function of Hardware
// and also test memory allocation. This now implements a test where Page4 process writes
// in memory, then sleeps while Page5 process also writes in memory, then Page4 proceeds to
// read data back from memory where it checks if it's the same data it initially wrote
// to memory, where it wasn't overwritten by Page5.
public class Pages4 extends UserlandProcess{

    @Override
    /**
     * This function tests the memory allocation functionality as well as read/write
     * functionality of the Hardware class. This is the same as Pages1 but now
     * implements a test to ensure that Page4 process wasn't overwritten by Page5 process,
     * an isolation test.
     */
    public void main() {
        int pointer = OS.AllocateMemory(1024);
        if(pointer == -1){
            System.out.println("Memory could not be allocated for Pages4");
            OS.Exit();
        }
        Hardware.Write(pointer, (byte) 100);
        System.out.println("Pages4 wrote 100 to memory");
        OS.Sleep(250);
        byte data = Hardware.Read(pointer);
        int num = data;
        System.out.println("Pages4 read " + num + " from memory");
        if(num == 100){
            System.out.println("Pages4 isolation test passed");
        }
        else {
            System.out.println("Pages4 isolation test failed");
        }
        while(true) {
                OS.Sleep(50);
        }
    }
}
