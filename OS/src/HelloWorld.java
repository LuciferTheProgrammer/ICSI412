// This is the HelloWorld which is derived from the UserlandProcess, it just prints
// "Hello World" in an infinite loop. Also calls cooperate() to switch to another process.
public class HelloWorld extends UserlandProcess{

    @Override
    /**
     * This function just prints/displays "Hello World" in an infinite loop. In addition,
     * also calls cooperate() inside the loop to switch process.
     */
    public void main(){
        while(true){
            try {
                System.out.print("Hello World\n");
                cooperate();
                Thread.sleep(50);
            }
            catch(Exception e){
                //Nothing
            }
        }
    }
}
