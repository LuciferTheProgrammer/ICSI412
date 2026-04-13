// This is the Pages2 process which is derived from UserlandProcess. This process
// allocates a space in memory which returns a pointer to the starting address
// in the allocated block and then proceeds to write 3 different data into memory
// and reads those data back from memory. It tests read/write function of Hardware
// and also test memory allocation. This now implements multiple allocations for this process.
public class Pages2 extends UserlandProcess{

    @Override
    /**
     * This function tests the memory allocation functionality as well as read/write
     * functionality of the Hardware class. This is the same as Pages1 but now implements
     * multiple allocations instead of just 1 allocation.
     */
    public void main() {
        int pointer1 = OS.AllocateMemory(1024);
        int pointer2 = OS.AllocateMemory(2048);
        if(pointer1 == -1 || pointer2 == -1){
            System.out.println("Memory could not be allocated for Pages2");
            OS.Exit();
        }
        byte data1 = (byte) 55;
        Hardware.Write(pointer1, data1);
        byte data2 = (byte) 66;
        Hardware.Write(pointer2, data2);
        byte data3 = (byte) 77;
        Hardware.Write(pointer2 + 1024, data3);
        byte res1 = Hardware.Read(pointer1);
        byte res2 = Hardware.Read(pointer2);
        byte res3 = Hardware.Read(pointer2 + 1024);
        if(res1 == 55 && res2 == 66 && res3 == 77){
            System.out.println("Pages2 multiple allocations with read/write test passed");
        }
        else {
            System.out.println("Pages2 multiple allocations with read/write test failed");
        }
        while (true) {
            try{
                OS.Sleep(50);
            }
            catch(Exception e){
            }
        }
    }
}
