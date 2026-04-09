// This is the Ping process which is derived from the UserlandProcess. This process sends and waits for a message from the corresponding
// Pong process using the properties of Kernel Message. This tests the functionality of GetPidByName(), SendMessage(), KernelMessage, and WaitForMessage().
public class Ping extends UserlandProcess{

    @Override
    /**
     * This function gets the target pid of the target process by name, GetPidByName(), for the Pong process. Then sets the default values
     * for indicator, sender pid, and data and using those values it creates Kernel Message which is then sent, SendMessage(). It also waits for a response back from Pong,
     * WaitForMessage(). It then increments the indicator and using this new value creates a new Kernel Message which is again then sent to Pong and just proceeds into a loop.
     */
    public void main() {
        int target = OS.GetPidByName("Pong");
        System.out.println("I am PING, pong = " + target);
        int what = 0;
        int sender = 2;
        byte[] data = null;
        KernelMessage km = new KernelMessage(sender, target, what, data);
        OS.SendMessage(km);
        while(true) {
            try {
                KernelMessage response = OS.WaitForMessage();
                System.out.println("PING: " + response.toString());
                what = response.getIndicator() + 1;
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
