
// This is the test process that contains a realtime long process, that gets demoted and another realtime process that sleeps
// which doesn't get demoted. Test Open, Close, Read, Write, and Seek.
public class Testing extends UserlandProcess{
    @Override
    /**
     * This function creates two realtime processes, BlastPast which is a long realtime process and HelloWorld which is realtime process
     * that sleeps. Now added to test functionality of Open, Close, Read, Write, and
     * Seek. Also testing Random Device, Fake File System, and Virtual File System. Now also creates Ping and Pong processes.
     * Now creates Pages1 - Pages5 processes. Now also implements instantiating Piggy process 20 times.
     */
    public void main () {
        int test1 = OS.Open("random 100");
        OS.Read(test1, 8);
        OS.Close(test1);
        int test2 = OS.Open("file data.dat");
        String sample = "HELLO";
        byte[] data = sample.getBytes();
        OS.Write(test2, data);
        OS.Seek(test2, 0);
        byte[] results = OS.Read(test2, 5);
        String container = new String(results);
        System.out.println("Data read from device on Testing process: " + container);
        OS.Seek(test2, 5);
        OS.Close(test2);
        int file1 = OS.Open("file data.dat");
        int file2 = OS.Open("file data.dat");
        String sample2 = "DDDDD";
        byte[] data2 = sample2.getBytes();
        OS.Write(file1, data2);
        OS.Seek(file2, 0);
        byte[] results2 = OS.Read(file2, 5);
        String container2 = new String(results2);
        System.out.println("Data read from device on Testing process: " + container2);
        OS.Seek(file2, 5);
        OS.Close(file1);
        OS.Close(file2);
        int[] deviceProcesses = new int[10];
        for(int i = 0; i < 10; i++) {
            deviceProcesses[i] = OS.Open("random");
        }
        int outlier = OS.Open("random");
        System.out.println("The 11th open device/file on Testing Process should be -1" + " and generated result is " + outlier);
        for(int i = 0; i < 10; i++) {
            OS.Close(deviceProcesses[i]);
        }
        System.out.println("Launching test processes...");
        for(int i = 0; i < 20; i++) {
            OS.CreateProcess(new Piggy(), OS.PriorityType.realtime);
        }
        //OS.CreateProcess(new Pages1(), OS.PriorityType.realtime);
        //OS.CreateProcess(new Pages2(), OS.PriorityType.realtime);
        //OS.CreateProcess(new Pages3(), OS.PriorityType.realtime);
        //OS.CreateProcess(new Pages4(), OS.PriorityType.realtime);
        //OS.CreateProcess(new Pages5(), OS.PriorityType.realtime);
        //OS.CreateProcess(new Ping(), OS.PriorityType.realtime);
        //OS.CreateProcess(new Pong(), OS.PriorityType.realtime);
        //OS.CreateProcess(new BlastPast(), OS.PriorityType.realtime);
        //OS.CreateProcess(new HelloWorld(), OS.PriorityType.realtime);
        //OS.CreateProcess(new GoodbyeWorld(), OS.PriorityType.background);

        while(true) {
            try {
                cooperate();
                Thread.sleep(50);
            }
            catch (Exception e) {
                // Do Nothing
            }
        }
    }
}
