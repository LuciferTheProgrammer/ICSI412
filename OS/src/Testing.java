// This is the test process that contains a realtime long process, that gets demoted and another realtime process that sleeps
// which doesn't get demoted.
public class Testing extends UserlandProcess{
    @Override
    /**
     * This function creates two realtime processes, BlastPast which is a long realtime process and HelloWorld which is realtime process
     * that sleeps.
     */
    public void main () {
        System.out.println("Launching test processes...");
        OS.CreateProcess(new BlastPast(), OS.PriorityType.realtime);
        OS.CreateProcess(new HelloWorld(), OS.PriorityType.realtime);
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
