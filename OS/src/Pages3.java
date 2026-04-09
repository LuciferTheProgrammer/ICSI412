public class Pages3 extends UserlandProcess{
    @Override
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
