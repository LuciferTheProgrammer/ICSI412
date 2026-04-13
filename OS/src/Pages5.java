// This is the Pages5 process which is derived from UserlandProcess. This process
// allocates a space in memory which returns a pointer to the starting address
// in the allocated block and then proceeds to write data into memory
// and reads the data back from memory. It tests read/write function of Hardware
// and also test memory allocation. This now implements a test where Pages4 process writes
// in memory, then sleeps while Pages5 process also writes in memory, then Pages4 proceeds to
// read data back from memory where it checks if it's the same data it initially wrote
// to memory, where it wasn't overwritten by Pages5. Pages5 also checks if the data it wrote initially is the
// same data it reads back. Now test FreeMemory as well to ensure the assigned memory
// space to the process is freed and is no longer reachable.
public class Pages5 extends UserlandProcess{

    @Override
    /**
     * This function tests the memory allocation functionality as well as read/write
     * functionality of the Hardware class. This is the same as Pages1 but now
     * implements a test to ensure that Pages4 process wasn't overwritten by Pages5 process,
     * and Pages5 process wasn't overwritten by Pages4 process, an isolation test. It also implements a
     * test for FreeMemory.
     */
    public void main() {
        int pointer = OS.AllocateMemory(1024);
        if(pointer == -1){
            System.out.println("Memory could not be allocated for Pages5");
            OS.Exit();
        }
        Hardware.Write(pointer, (byte) 120);
        System.out.println("Pages5 wrote 120 to memory");
        OS.Sleep(250);
        byte data = Hardware.Read(pointer);
        int num = data;
        System.out.println("Pages5 read " + num + " from memory");
        if(num == 120){
            System.out.println("Pages5 isolation test passed");
        }
        else {
            System.out.println("Pages5 isolation test failed");
        }
        OS.FreeMemory(pointer, 1024);
        System.out.println("Pages5 memory freed");
        System.out.println("Will now access memory. Should result in 'seg fault' due to memory" +
                " being freed");
        byte data2 = Hardware.Read(pointer);
        int num2 = data2;
        System.out.println("Pages5 read " + num2 + " from memory");
        while(true) {
            OS.Sleep(50);
        }
    }
}
