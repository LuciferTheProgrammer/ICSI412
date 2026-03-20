public class Pong extends UserlandProcess{
    @Override
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
                Thread.sleep(50);
            }
            catch(Exception e) {
                // Do Nothing
            }
        }
    }
}
