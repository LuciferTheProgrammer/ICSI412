public class Pages1 extends UserlandProcess {
    @Override
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
