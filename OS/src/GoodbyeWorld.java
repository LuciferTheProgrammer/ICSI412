public class GoodbyeWorld extends UserlandProcess{
    public void main() {
        while(true) {
            try {
                System.out.print("Goodbye world");
                cooperate();
                Thread.sleep(50);
            }
            catch(Exception e){
                //Nothing
            }
        }
    }
}
