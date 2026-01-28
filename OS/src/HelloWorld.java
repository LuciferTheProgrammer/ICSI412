public class HelloWorld extends UserlandProcess{
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
