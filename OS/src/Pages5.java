public class Pages5 extends UserlandProcess{
    @Override
    public void main() {
        int pointer = OS.AllocateMemory(1024);
        if(pointer == -1){
            System.out.println("Memory could not be allocated for Pages5");
            OS.Exit();
        }
        Hardware.Write(pointer, (byte) 120);
        System.out.println("Pages5 wrote 120 to memory");
        OS.Sleep(200);
        byte data = Hardware.Read(pointer);
        int num = data;
        System.out.println("Pages5 read " + num + " from memory");
        if(num == 120){
            System.out.println("Pages5 isolation test passed");
        }
        else {
            System.out.println("Pages5 isolation test failed");
        }
        while(true) {
            OS.Sleep(50);
        }
    }
}
