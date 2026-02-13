// This is the IdleProcess which is derived from the UserlandProcess, it sits idle in an infinite loop and calls cooperate() to switch
// to another process.
public class IdleProcess extends UserlandProcess {

    @Override
    /**
     * This function just sits idle in an infinite loop while also calling cooperate() inside the loop to
     * switch to another process.
     */
    public void main() {
        while (true) {
            try {
                cooperate();
                Thread.sleep(50);
            } catch (Exception e) { }
        }
    }
}
