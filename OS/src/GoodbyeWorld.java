// This is the GoodbyeWorld which is derived from the UserlandProcess, it just prints
// "Goodbye world" in an infinite loop. Also calls cooperate() to switch to another process.
public class GoodbyeWorld extends UserlandProcess{

    @Override
    /**
     * This function just prints/displays "Goodbye world" in an infinite loop. In addition,
     * also calls cooperate() inside the loop to switch process.
     */
    public void main() {
        while(true) {
            try {
                System.out.println("Goodbye world");
                cooperate();
                Thread.sleep(50);
            }
            catch(Exception e){
                //Nothing
            }
        }
    }
}
