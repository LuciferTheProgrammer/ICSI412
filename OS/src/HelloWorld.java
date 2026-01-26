public class HelloWorld extends UserlandProcess{
    public void main(){
        while(true){
            try {
                System.out.print("Hello World");
                cooperate();
                Thread.sleep(50);
            }
            catch(Exception e){
                //Nothing
            }
        }
    }
}
