// This is the Pages1 process which is derived from UserlandProcess. This process
// allocates a space in memory which returns a pointer to the starting address
// in the allocated block and then proceeds to write 3 different data into memory
// and reads those data back from memory. It tests read/write function of Hardware
// and also test memory allocation.
public class Pages1 extends UserlandProcess {

    @Override
    /**
     * This function tests the memory allocation functionality as well as read/write
     * functionality of the Hardware class. It allocates space in memory, where it then
     * uses the returned starting pointer to write data 3 times in memory, while incrementing
     * the pointer after each write. Then reads the written data back from memory and checks
     * if the written data matches the data that is read back.
     */
    public void main() {
        int pointer = OS.AllocateMemory(1024);
        if (pointer == -1) {
            System.out.println("Memory could not be allocated for Pages1");
            OS.Exit();
        }
        byte data1 = (byte) 10;
        Hardware.Write(pointer, data1);
        byte data2 = (byte) 20;
        Hardware.Write(pointer + 1, data2);
        byte data3 = (byte) 30;
        Hardware.Write(pointer + 2, data3);
        byte res1 = Hardware.Read(pointer);
        byte res2 = Hardware.Read(pointer + 1);
        byte res3 = Hardware.Read(pointer + 2);
        if(res1 == 10 && res2 == 20 && res3 == 30){
            System.out.println("Pages1 read/write test passed");
        }
        else {
            System.out.println("Pages1 read/write test failed");
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
