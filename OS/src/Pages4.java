public class Pages4 extends UserlandProcess{
    @Override
    public void main() {
        int pointer = OS.AllocateMemory(1024);
        if(pointer == -1){
            System.out.println("Memory could not be allocated for Pages4");
            OS.Exit();
        }
        Hardware.Write(pointer, (byte) 100);
        System.out.println("Pages4 wrote 100 to memory");
        OS.Sleep(200);
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
