import java.util.*;

public class Test {
    public static List<Object> params = new ArrayList<>();
    public static void main(String[] args) {
        params.add(5);               // autoboxes to Integer, stored as Object
        // <-- compile error
        System.out.println((int) params.get(0));
    }
}
