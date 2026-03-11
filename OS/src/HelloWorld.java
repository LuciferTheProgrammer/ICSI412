// This is the HelloWorld which is derived from the UserlandProcess, it just prints
// "Hello World" in an infinite loop. Also calls cooperate() to switch to another process. This is the realtime process
// that sleeps. Test Open, Close, Read, Write, and Seek.
public class HelloWorld extends UserlandProcess{

    @Override
    /**
     * This function just prints/displays "Hello World" in an infinite loop. In addition,
     * it now calls Sleep(int x) with a requested time for the process to sleep and to switches to another process.
     * Now added to test functionality of Open, Close, Read, Write, and
     * Seek. Also testing Random Device, Fake File System, and Virtual File System.
     */
    public void main(){
        int file = OS.Open("file shared.dat");
        String s = "DZ";
        byte[] result = s.getBytes();
        OS.Write(file, result);
        OS.Seek(file, 0);
        byte[] sol = OS.Read(file, 2);
        OS.Close(file);
        System.out.println("Data read from device on HelloWorld process: " + new String(sol));
        while(true){
                System.out.println("Hello World");
                OS.Sleep(50);
        }
    }
}
