// This is the Pong process which is derived from the UserlandProcess. This process waits for a message from the Ping process and sends a response
// back using the properties of Kernel Message. This tests the functionality of GetPidByName(), SendMessage(), KernelMessage, and WaitForMessage().
public class Pong extends UserlandProcess{

    @Override
    /**
     * This function gets the target pid of the target process by name, GetPidByName(), for the Ping process. Then sets the default values
     * for indicator, sender pid, and data. It waits for a response back from Ping,
     * WaitForMessage(). It then gets the indicator from the response it got from Ping, and creates a new Kernel Message using the required parameters and then sends a
     * message/reply back to Ping, SendMessage(). Then, proceed into a loop.
     */
    public void main() {
        int target = OS.GetPidByName("Ping");
        System.out.println("I am PONG, ping = " + target);
        int what = 0;
        int sender = 3;
        byte[] data = null;
        while(true) {
            try {
                KernelMessage response = OS.WaitForMessage();
                System.out.println("PONG: " + response.toString());
                what = response.getIndicator();
                KernelMessage newMessage = new KernelMessage(sender, target, what, data);
                OS.SendMessage(newMessage);
                OS.Sleep(50);
            }
            catch(Exception e) {
                // Do Nothing
            }
        }
    }
}
