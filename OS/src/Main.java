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
