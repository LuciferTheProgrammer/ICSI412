// This class is the main driver file which creates three processes, HelloWorld, GoodbyeWorld, and IdleProcess which are
// derived from the UserlandProcess. Then, all three processes are stored in an UserlandProcess array instance which
// then starts the processes. The program proceeds to print a bunch of Hello Worlds and Goodbye Worlds, alternating between the two.
public class Main {
    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld();
        GoodbyeWorld goodbyeWorld = new GoodbyeWorld();
        IdleProcess idleProcess = new IdleProcess();
        UserlandProcess[] init = new UserlandProcess[3];
        init[0] = helloWorld;
        init[1] = goodbyeWorld;
        init[2] = idleProcess;
        OS.Startup(init);
    }
}
