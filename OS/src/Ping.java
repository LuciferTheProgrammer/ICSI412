public class Ping extends UserlandProcess{
    @Override
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
                Thread.sleep(50);
            }
            catch(Exception e) {
                // Do Nothing
            }
        }

    }
}
