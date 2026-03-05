// This class is the main driver file which creates an array instance of UserlandProcess and then creates an instance of the Testing process for
// the array to hold. Then this array process is used to initialize the OS startup. To test a long realtime process for demotion overtime and a realtime process
// that sleeps which doesn't get demoted.
public class Main {
    public static void main(String[] args) {
        UserlandProcess[] init = new UserlandProcess[1];
        init[0] = new Testing();
        OS.Startup(init);
    }
}
