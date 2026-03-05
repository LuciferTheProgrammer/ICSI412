// This is the HelloWorld which is derived from the UserlandProcess, it just prints
// "Hello World" in an infinite loop. Also calls cooperate() to switch to another process. This is the realtime process
// that sleeps.
public class HelloWorld extends UserlandProcess{

    @Override
    /**
     * This function just prints/displays "Hello World" in an infinite loop. In addition,
     * it now calls Sleep(int x) with a requested time for the process to sleep and to switches to another process.
     */
    public void main(){
        while(true){
                System.out.println("Hello World");
                OS.Sleep(50);
        }
    }
}
