// This is the Pages3 process which is derived from UserlandProcess. This process
// allocates a space in memory which returns a pointer to the starting address
// in the allocated block and then proceeds to write data that is beyond the scope
// of the assigned memory space (block), where it seg faults due to an invalid memory address.
// This implements a test that prints seg fault if the process is trying to access
// memory beyond its assigned block.
public class Pages3 extends UserlandProcess{

    @Override
    /**
     * This function tests the memory allocation functionality as well as read/write
     * functionality of the Hardware class. This is the same as Pages1 but now
     * implements a test when the process tries accessing a memory address beyond its allocated
     * block, memory out of bounds, where it should seg fault.
     */
    public void main() {
        int pointer = OS.AllocateMemory(1024);
        if(pointer == -1){
            System.out.println("Memory could not be allocated for Pages3");
            OS.Exit();
        }
        System.out.println("Pages3 will now access invalid memory address");
        byte data = (byte) 99;
        Hardware.Write(pointer + 1024, data);
        while (true) {
            try{
                OS.Sleep(50);
            }
            catch(Exception e){
            }
        }
    }
}
