// This is the BlastPast which is derived from the UserlandProcess, it is a realtime
// long process that just computes a counter and one that doesn't sleep.
public class BlastPast extends UserlandProcess{

    @Override
    /**
     * This function just computes a counter for a long time. Acts as a realtime long process.
     */
    public void main() {
        System.out.println("Blast Past Process: Realtime long process.");
        while(true) {
            int counter = 0;
               for(int i = 0; i < 1000000; i++) {
                   counter++;
               }
               //System.out.println("Blast Past long real time process");
               cooperate();
        }
    }
}
